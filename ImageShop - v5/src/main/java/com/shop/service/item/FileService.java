package com.shop.service.item;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService {
	
	@Value("${uploadPath}")
	private String uploadPath;
	
	@Value("${uploadFilePath}")
	private String uploadFilePath;

	public String uploadFile(String originalFileName, byte[] fileData) throws Exception {
	
		// 1-1. 파일명 중복을 피하기 위해 uuid 생성
		UUID uuid = UUID.randomUUID();
				
		// 1-2. 확장자 가져오기.
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		// 1-3. uuid + 확장자 이름으로 저장파일 이름 생성
		String savedFileName = uuid.toString() + extension;
	
		// 1-4. 파일이 업로드될 총 경로.
		String fileUploadFullUrl = uploadPath + "/" + savedFileName;
	
		// 1-5. FileOutputStream 클래스는 바이트 단위의 출력을 내보내는 클래스입니다. 생성자에 파일이 업로드될 총 경로 위치와 파일의 이름을 넘겨주고 파일 출력 스트림을 만든다.
		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
		
		// 1-6. 파일 데이터 바이트 값을 파일 출력 스트림에 입력한다.
		fos.write(fileData);
				
		// 1-7. 파일 출력 스트림을 닫고
		fos.close();
				
		// 1-8. 업로드된 파일의 이름을 반환한다.
		return savedFileName;
	}
	
	public String uploadFileByPds(String originalFileName, byte[] fileData) throws Exception {
		
		// 1-1. 파일명 중복을 피하기 위해 uuid 생성
		UUID uuid = UUID.randomUUID();
				
		// 1-2. 확장자 가져오기.
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		// 1-3. uuid + 확장자 이름으로 저장파일 이름 생성
		String savedFileName = uuid.toString() + extension;
	
		// 1-4. 파일이 업로드될 총 경로.
		String fileUploadFullUrl = uploadFilePath + "/" + savedFileName;
	
		// 1-5. FileOutputStream 클래스는 바이트 단위의 출력을 내보내는 클래스입니다. 생성자에 파일이 업로드될 총 경로 위치와 파일의 이름을 넘겨주고 파일 출력 스트림을 만든다.
		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
		
		// 1-6. 파일 데이터 바이트 값을 파일 출력 스트림에 입력한다.
		fos.write(fileData);
				
		// 1-7. 파일 출력 스트림을 닫고
		fos.close();
				
		// 1-8. 업로드된 파일의 이름을 반환한다.
		return savedFileName;
	}
	
	public void deleteFile(String pictureFileName, String previewFileName) throws Exception {
		
		String pictureFileUploadFullUrl = uploadPath + "/" + pictureFileName;
		String previewFileUploadFullUrl = uploadPath + "/" + previewFileName;
		
		File findPictureFile = new File(pictureFileUploadFullUrl);
		File findPreviewFile = new File(previewFileUploadFullUrl);
		
		if(findPictureFile.exists()) {
			findPictureFile.delete();
		} 
		
		if(findPreviewFile.exists()) {
			findPreviewFile.delete();
		} 
		
 	}
	
	public void deletePdsFile(String savedFileName) throws Exception {
		String pdsFileUrl = uploadFilePath + "/" + savedFileName;
		
		File findPdsFile = new File(pdsFileUrl);
		
		if(findPdsFile.exists()) {
			findPdsFile.delete();
		}
	}

}
