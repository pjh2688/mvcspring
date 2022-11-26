package com.tistory.service.post;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.tistory.domain.category.Category;
import com.tistory.domain.category.CategoryRepository;
import com.tistory.domain.love.Love;
import com.tistory.domain.love.LoveRepository;
import com.tistory.domain.post.Post;
import com.tistory.domain.post.PostRepository;
import com.tistory.domain.user.User;
import com.tistory.domain.visit.Visit;
import com.tistory.domain.visit.VisitRepository;
import com.tistory.handler.exception.CustomApiException;
import com.tistory.handler.exception.CustomException;
import com.tistory.web.dto.love.LoveDto;
import com.tistory.web.dto.post.PostDetailRespDto;
import com.tistory.web.dto.post.PostInfoDto;
import com.tistory.web.dto.post.PostMainRespDto;
import com.tistory.web.dto.post.PostRespDto;
import com.tistory.web.dto.post.PostUpdateDto;
import com.tistory.web.dto.post.PostWriteReqDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
	
	private final PostRepository postRepository;
	private final CategoryRepository categoryRepository;
	private final VisitRepository visitRepository;
	private final LoveRepository loveRepository;
	
	@Value("${thumnail.path}")
    private String thumnailFolder;

	public void writePost(PostWriteReqDto postWriteReqDto, User principal) {
		String thumnail = null;
		
		if (!(postWriteReqDto.getThumnailFile() == null || postWriteReqDto.getThumnailFile().isEmpty())) {
			String originalFileName = postWriteReqDto.getThumnailFile().getOriginalFilename();
			
			// 1-4. 확장자 추출
			String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
			
			// 1-5. UUID[Universally Unique IDentifier] : 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준 규약. 범용고유식별자.
			UUID uuid = UUID.randomUUID();
			
			// 1-6. UUID + 확장자 명으로 저장
			String thumnailFileName = uuid.toString() + extension;
			
			// 1-7. 실제 파일이  저장될 경로 + 파일명 저장.
			Path thumnailFilePath = Paths.get(thumnailFolder + thumnailFileName);
			
			// 1-8. 파일을 실제 저장 경로에 저장하는 로직 -> 통신 or I/O -> 예외가 발생할 수 있다(try-catch로 묶어서 처리)
			try {
				Files.write(thumnailFilePath, postWriteReqDto.getThumnailFile().getBytes());
			} catch (Exception e) {
				// 1-10. 1MB를 넘어가면 -1을 반환
				throw new MaxUploadSizeExceededException(600);
			}
						
			// 1-7. 썸네일 파일 이름 저장.
			thumnail = thumnailFileName;
		}
					
		// 1-8. 카테고리 있는지 확인
		Optional<Category> categoryOp = categoryRepository.findById(postWriteReqDto.getCategoryId());

		// 1-9
		if (categoryOp.isPresent()) {
			Post post = postWriteReqDto.toEntity(thumnail, principal, categoryOp.get());
			postRepository.save(post);
		} else {
			throw new CustomApiException("해당 카테고리가 존재하지 않습니다.");
		}
					
	}
	
	public PostRespDto viewPost(Long pageOwnerId, Pageable pageable, User principal) {
		Page<Post> postsEntity = postRepository.findByUserId(pageOwnerId, pageable);
		
		List<Category> categorysEntity = categoryRepository.findAll();
		
		List<Integer> pageNumbers = new ArrayList<>();
		
        for (int i = 0; i < postsEntity.getTotalPages(); i++) {
        	// 2-1. 뷰로 보낼 pageNumber 데이터를 0부터 시작하는게 아니라 1부터 시작하게 +1해준다.
            pageNumbers.add(i + 1);
        }
        
        Visit visitEntity = visitIncrease(pageOwnerId, principal.getId());

        PostRespDto postRespDto = new PostRespDto(
                postsEntity,
                categorysEntity,
                pageOwnerId,
                postsEntity.getNumber() - 1,
                postsEntity.getNumber() + 1,           
                pageNumbers,
                visitEntity.getTotalCount(),
    			postsEntity.getNumber() - 1 != -1 ? true : false,
    			postsEntity.getNumber() + 1 != postsEntity.getTotalPages() ? true : false 
        		);
        
        return postRespDto;
	}
	
	public PostRespDto viewPostByCategoryId(Long pageOwnerId, Long categoryId, Pageable pageable, User principal) {
		
		Page<Post> postsEntity = postRepository.findByUserIdAndCategoryId(pageOwnerId, categoryId, pageable);
				
		List<Category> categorysEntity = categoryRepository.findAll();

		List<Integer> pageNumbers = new ArrayList<>();
		
        for (int i = 0; i < postsEntity.getTotalPages(); i++) {
        	pageNumbers.add(i + 1);
        }
        
        Visit visitEntity = visitIncrease(pageOwnerId, principal.getId());

        PostRespDto postRespDto = new PostRespDto(
                postsEntity,
                categorysEntity,
                pageOwnerId,
                postsEntity.getNumber() - 1,
                postsEntity.getNumber() + 1,
                pageNumbers,
                visitEntity.getTotalCount(),
    			postsEntity.getNumber() - 1 != -1 ? true : false,
    			postsEntity.getNumber() + 1 != postsEntity.getTotalPages() ? true : false 
                );

        return postRespDto;
	}
	
	public PostMainRespDto viewPostByPopular(User principal, Pageable pageable) {
		
		Page<Post> postsEntity = postRepository.findByPopular(principal.getId(), pageable);
		
		List<Integer> pageNumbers = new ArrayList<>();
		
        for (int i = 0; i < postsEntity.getTotalPages(); i++) {
        	pageNumbers.add(i + 1);
        }
        
        PostMainRespDto postMainRespDto = new PostMainRespDto(
        			postsEntity, 
        			postsEntity.getNumber() - 1, 
        			postsEntity.getNumber() + 1, 
        			pageNumbers,
        			postsEntity.getNumber() - 1 != -1 ? true : false,
        			postsEntity.getNumber() + 1 != postsEntity.getTotalPages() ? true : false 
        			);
               
        return postMainRespDto;
	}
	
	public PostMainRespDto viewPostByPopular(Pageable pageable) {
		
		Page<Post> postsEntity = postRepository.findByPopularAll(pageable);
		
		List<Integer> pageNumbers = new ArrayList<>();
		
        for (int i = 0; i < postsEntity.getTotalPages(); i++) {
        	pageNumbers.add(i + 1);
        }
        
        PostMainRespDto postMainRespDto = new PostMainRespDto(
        			postsEntity, 
        			postsEntity.getNumber() - 1, 
        			postsEntity.getNumber() + 1, 
        			pageNumbers,
        			postsEntity.getNumber() - 1 != -1 ? true : false,
        			postsEntity.getNumber() + 1 != postsEntity.getTotalPages() ? true : false 
        			);
               
        return postMainRespDto;
	}
	
	
	public PostDetailRespDto viewPostDetail(Long id) {
		PostDetailRespDto postDetailRespDto = new PostDetailRespDto();

        Post postEntity = null;
        
        Optional<Post> postOp = postRepository.findById(id);
        
        if (postOp.isPresent()) {
            postEntity = postOp.get();
        } else {
            throw new CustomApiException("해당 게시글이 존재하지 않습니다");
        }

        visitIncrease(postEntity.getUser().getId(), 0L);

        postDetailRespDto.setPost(postEntity);
        postDetailRespDto.setPageOwner(false);
        postDetailRespDto.setLoveId(0L);
        postDetailRespDto.setLove(false);
       
        return postDetailRespDto;
	}
	
	public PostDetailRespDto viewPostDetail(Long id, User principal) {
		PostDetailRespDto postDetailDto = new PostDetailRespDto();

        Post postEntity = null;
        
        Optional<Post> postOp = postRepository.findById(id);
        
        if (postOp.isPresent()) {
            postEntity = postOp.get();
        } else {
            throw new CustomApiException("해당 게시글이 존재하지 않습니다");
        }
        
        boolean isAuth = authCheck(postEntity.getUser().getId(), principal.getId());
    
        visitIncrease(postEntity.getUser().getId(), principal.getId());

        postDetailDto.setPost(postEntity);
        postDetailDto.setPageOwner(isAuth);
      
        Optional<Love> loveOp = loveRepository.mFindByUserIdAndPostId(principal.getId(), id);
        
        if(loveOp.isPresent()) {
            Love loveEntity = loveOp.get();
            postDetailDto.setLoveId(loveEntity.getId());
            postDetailDto.setLove(true);
        } else {
        	postDetailDto.setLove(false);
        }

        return postDetailDto;
	}
	
	public Post updatePost(PostUpdateDto postUpdateDto, Long postId, User principal) {
		
		Post updatePost = null;

		// 2-1. 카테고리 있는지 확인
		Optional<Category> categoryOp = categoryRepository.findById(postUpdateDto.getCategoryId());
		
		// 2-2. 해당 게시글 있는지 확인
		Optional<Post> postOp = postRepository.findById(postId);
		
        // 2-3. 게시글이 존재하면
		if (postOp.isPresent()) {
			
			// 2-4. 게시글 get
			Post post = postOp.get();
			post.setTitle(postUpdateDto.getTitle());
			post.setContent(postUpdateDto.getContent());
			post.setUser(principal);
			
			if(categoryOp.isPresent()) {
				post.setCategory(categoryOp.get());
			} else {
	            throw new CustomException("해당 카테고리가 존재하지 않습니다.");
	        }
			
            updatePost = postRepository.save(post);
            
        } else {
            throw new CustomException("해당 게시글이 존재하지 않습니다.");
        }
		
		return updatePost;
	}
	
	public void deletePost(Long postId, User principal) {
		Post postEntity = null;
		
		// 5-1. 게시글 번호로 DB에서 해당 게시글을 불러온다.
		Optional<Post> postOp = postRepository.findById(postId);
		
		// 5-2. 해당 게시글이 존재할 때만 get
		if (postOp.isPresent()) {
            postEntity = postOp.get();
            
        } else {
        	// 5-3. 아니면 예외를 던져준다.
            throw new CustomApiException("해당 게시글이 존재하지 않습니다");
        }
		
		// 5-4. 포스팅을 올린 사람이 페이지주인이라면
		if(authCheck(postEntity.getUser().getId(), principal.getId())) {	
			// 5-5. 포스팅 정보를 삭제하기 전에 해당 포스팅에 등록된 좋아요 정보가 있는지 확인
			Optional<Love> loveOp = loveRepository.findByPostId(postId);
			
			// 5-6. 좋아요 정보가 있다면
			if(loveOp.isPresent()) {
				// 5-7. 영속성 컨텍스트에서 불러와서 삭제
				Love loveEntity = loveOp.get();
				loveRepository.delete(loveEntity);
			}
			
			// 5-8. 포스팅 삭제
			postRepository.deleteById(postId);
		} else {
			// 5-9. 페이지 주인이 아니라면 권한이 없는 사용자라고 예외를 던져준다.
			throw new CustomApiException("삭제 권한이 없습니다.");
		}			
	}
	
	public LoveDto lovePost(Long postId, User principal) {
		
		// 6-1. 
		Post postEntity = null;
		
		// 6-2. 게시글 번호로 DB에서 해당 게시글을 불러온다.
		Optional<Post> postOp = postRepository.findById(postId);
		
		// 6-3. 해당 게시글이 존재할 때만 get
		if (postOp.isPresent()) {
            postEntity = postOp.get();
        } else {
        	// 6-4. 아니면 예외를 던져준다.
            throw new CustomApiException("해당 게시글이 존재하지 않습니다");
        }
		
		// 6-5. 좋아요 엔티티 생성.
		Love love = new Love();
		
		// 6-6. 좋아요 엔티티에 User 엔티티 셋팅(연결)
		love.setUser(principal);
		
		// 6-7. 좋아요 엔티티에 Post 엔티티 셋팅(연결)
		love.setPost(postEntity);
		
		// 6-8. 영속화하고 영속화된 엔티티를 loveEntity라는 이름으로 저장.
		Love loveEntity = loveRepository.save(love);
		
		// 6-9. 뷰에다가는 엔티티를 넘겨줄 수 없으니 뷰에다 넘겨줄 Dto객체에 love엔티티의 id 셋팅.
		LoveDto loveDto = new LoveDto();
		loveDto.setLoveId(loveEntity.getId());
		
		// 6-10. 좋아요정보에 셋팅해서 보낼 포스팅 정보를 Dto로 만들어 셋
		PostInfoDto postInfoDto = new PostInfoDto();
		postInfoDto.setPostId(postEntity.getId());
		postInfoDto.setTitle(postEntity.getTitle());
		
		loveDto.setPost(postInfoDto);

		return loveDto;
	}
	
	public LoveDto unLovePost(Long loveId, User principal) {
		// 7-1.
		Love loveEntity = null;
		
		// 7-2. 게시글 번호로 DB에서 해당 게시글의 좋아요 정보를 불러온다.
		Optional<Love> loveOp = loveRepository.findById(loveId);
				
		// 7-3. 해당 게시글에 좋아요 ID로 등록된 좋아요가 존재할 때만 get
		if (loveOp.isPresent()) {
			// 6-1. 
			loveEntity = loveOp.get();
			
			LoveDto loveDto = new LoveDto();
			loveDto.setLoveId(loveEntity.getId());
			
			loveRepository.deleteById(loveId);
			
			return loveDto;
		} else {
			// 7-4. 아니면 예외를 던져준다.
		    throw new CustomApiException("해당 게시글이 존재하지 않습니다");
		}
	
	}
	
	private Visit visitIncrease(Long pageOwnerId, Long principalId) {
        Optional<Visit> visitOp = visitRepository.findById(pageOwnerId);
        
        if (visitOp.isPresent()) {
            Visit visitEntity = visitOp.get();
            Long totalCount = visitEntity.getTotalCount();
            
            if(pageOwnerId != principalId) {
                visitEntity.setTotalCount(totalCount + 1);
            }
            return visitEntity;
        } else {
        	
            throw new CustomException("일시적 문제가 생겼습니다. 관리자에게 문의해주세요.");
        }
    }
	
	private boolean authCheck(Long principalId, Long pageOwnerId) {
        boolean isAuth = false;
        if (principalId == pageOwnerId) {
            isAuth = true;
        } else {
            isAuth = false;
        }
        return isAuth;
    }
}
