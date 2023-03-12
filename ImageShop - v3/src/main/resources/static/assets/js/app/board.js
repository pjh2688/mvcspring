/**
 * board.js
 */
$(document).ready(function(){

	var ACCESS_TOKEN = getCookie('access_token');
	
	function getCookie(key) {
		let result = null;
		let cookie = document.cookie.split(';');
		cookie.some(function(item) {
			item = item.replace(' ', '');
			
			let dic = item.split('=');
			if(key === dic[0]) {
				result = dic[1];
				return true;
			}
		});
		return result;
	}
	
	if(ACCESS_TOKEN != null) {
		console.log(ACCESS_TOKEN);
		
		var header = ACCESS_TOKEN.split('.')[0];
		var payload = ACCESS_TOKEN.split('.')[1];
		var signature = ACCESS_TOKEN.split('.')[2];
		
		console.log(header);
		console.log(payload);
		console.log(signature);
		
		console.log("복호화 : " + Base64.decode(payload));
		
		// 2023-02-12 -> 토큰에 있는 payload에 실어논 정보를 Base64로 디코드해서 가져와 세팅(base64.min.js 필요)
		var login_user = JSON.parse(Base64.decode(payload));

		console.log(login_user.userName);
		
		$("#boardWriter").val(login_user.userName);
	}
	
	// 2023-02-14 -> 여기까지
	/**
	 *  6-1. 목록 조회
	 */
	$("#boardListBtn").on("click", function(){
		console.log("boardListBtn");
		
		if(ACCESS_TOKEN != null) {
		
			$.ajax({
				type : "GET",
				url : "/api/boards",
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					console.log(res);
				
					console.log(res.data.length);
				
					var html = `
						<!-- /* 게시글 영역 */ -->
						<div class="table-responsive clearfix">
							<table class="table table-hover">
								<thead>
									<tr>
										<th colspan="2">no</td>
										<th colspan="6">boardNo</th>
										<th colspan="12">title</th>
										<th colspan="14">content</th>
										<th colspan="5">writer</th>
										<th colspan="8">등록일</th>
										<th colspan="8">수정일</th>
									</tr>
								</thead>
									
								<!-- /* 게시글 리스트 rending 영역 */ -->
								<tbody id="list">
								</tbody>
							</table>
						</div>
					`;
				
					document.getElementById('content').innerHTML = html;
				
					html = ``;
				
					if(!res.data.length) {
						html = '<td colspan="53">등록된 게시글이 없습니다.</td>';
					} else {
						for(var i = 0; i < res.data.length; i++) {
							html += `
								<tr>
									<td colspan="2">${res.data.length - i}</td>
									<td colspan="6">${res.data[i].boardNo}</td>
									<td colspan="12">${res.data[i].title}</td>
									<td colspan="14">${res.data[i].content}</td>
									<td colspan="5">${res.data[i].writer}</td>
									<td colspan="8">${res.data[i].createdDate}</td>
									<td colspan="8">${res.data[i].modifiedDate == null ? '' : res.data[i].modifiedDate}</td>
								</tr>
							`;
						}
					}
					document.getElementById('list').innerHTML = html;
				},
				error : function(res) {
					console.log(res);
				
					alert(res.responseJSON.message);

				}
			});
		} else {
			alert("로그인을 해주세요.");
			return;
		}
		
	});
	

	/**
	 *  6-2. 상세 조회
	 */
	$("#boardReadBtn").on("click", function(){
		console.log("boardReadBtn");
		
		if(ACCESS_TOKEN != null) {
		
			var boardNo = $("#boardNo").val();
		
			if(!boardNo.trim()) {
				alert("boardNo를 입력해주세요.");
				$("#board_form #boardNo").focus();
				return false;
			}
		
			$.ajax({
				type : "GET",
				url : "/api/boards/" + boardNo,
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					console.log(res);
				
					$("#boardTitle").val(res.data.title);
					$("#boardContent").val(res.data.content);
				},
				error : function(res) {
					console.log(res);
				
					alert(res.responseJSON.message);
					
					$("#boardNo").val("");
					$("#boardTitle").val("");
					$("#boardContent").val("");
				
				}
			});
		} else {
			alert("로그인을 해주세요.");
			return;
		}
	});
	
	// 2023-02-12 -> 게시글 등록 api까지 완성
	/**
	 *  6-3. 등록 
	 */
	$("#boardRegisterBtn").on("click", function() {
		
		if(ACCESS_TOKEN != null) {
		
			var boardObject = {
				title : $("#boardTitle").val(),
				content : $("#boardContent").val(),
				writer : $("#boardWriter").val()
			};
			
			console.log(JSON.stringify(boardObject));
			
			if(isValid()) {
			
				$.ajax({
					type : "POST",
					url : "/api/boards",
					data : JSON.stringify(boardObject),
					contentType : "application/json; charset=UTF-8",
					headers: {
						"Authorization" : "Bearer " + ACCESS_TOKEN
					},
					success : function(res) {
						if(res.code == 1) {
							alert("Created")
						}
						$("#boardTitle").val("");
						$("#boardContent").val("");
					},
					error : function(res) {
						console.log(res);
					
						alert(res.responseJSON.message);
					}
				});	
			}
		} else {
			alert("로그인을 해주세요.");
			return;
		}
	});
	
	/**
	 *  6-5. 삭제
	 */
	$("#boardDeleteBtn").on("click", function(){
		console.log("boardDeleteBtn");
		
		if(ACCESS_TOKEN != null) {
		
			var boardNo = $("#boardNo").val();
		
			if(!boardNo.trim()) {
				alert("boardNo를 입력해주세요.");
				$("#board_form #boardNo").focus();
				return false;
			}
		
			$.ajax({
				type : "DELETE",
				url : "/api/boards/" + boardNo,
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					if(res.code == 1) {
						alert("Deleted");
						location.reload();
					}
				},
				error : function(res) {
					console.log(res);
				
					alert(res.responseJSON.message);
					
				}
			});	
		
		} else {
			alert("로그인을 해주세요.");
			return;
		}
		
	});
	
	/**
	 *  6-6. 수정 -> 2023-02-16 : 수정 api 완료.
	 */
	$("#boardModifyBtn").on("click", function(){
		console.log("boardModifyBtn");
		
		if(ACCESS_TOKEN != null) {
		
			if(isValid()) {
				var boardNo = $("#boardNo").val();
			
//				alert("boardNo = " + boardNo);
			
				var boardObject = {
					title : $("#boardTitle").val(),
					content : $("#boardContent").val(),
					writer : $("#boardWriter").val()	
				};
			
				console.log(JSON.stringify(boardObject));
			
				$.ajax({
					type : "PUT",
					url : "/api/boards/" + boardNo,
					data : JSON.stringify(boardObject),
					contentType : "application/json; charset=UTF-8",
					headers: {
						"Authorization" : "Bearer " + ACCESS_TOKEN
					},
					success : function(res) {
						console.log(res);
					
						if(res.code == 1) {
							alert("Modified")
							location.reload();
						}
					
					},
					error : function(res) {
						console.log(res);
				
						alert(res.responseJSON.message);
							
					}
				});	
			}
		
		} else {
			alert("로그인을 해주세요.");
			return;
		}
		
	});
	
	/* 6-4. 유효성 검사 */
	function isValid() {
		
		var boardTitle = $("#boardTitle").val();
		var boardContent = $("#boardContent").val();
		var boardWriter = $("#boardWriter").val();
			
		if(!boardTitle.trim()) {
			alert("제목을 입력해주세요.");
			$("#board_form #boardTitle").focus();
			return false;
		}
			
		if(!boardContent.trim()) {
			alert('내용을 입력해주세요.');
			$("#board_form #boardContent").focus();
			return false;
		}
			
		if(!boardWriter.trim()) {
			alert('작성자를 입력해주세요.');
			$("#board_form #boardWriter").focus();
			return false;
				
		}
			
		return true;
	}
	
	/**
	 *  6-7. 입력값 리셋
	 */
	$("#boardResetBtn").on("click", function(){
		console.log("boardResetBtn");
		
		$("#boardNo").val("");
		$("#boardTitle").val("");
		$("#boardContent").val("");
		
	});	
});