/**
 * notice.js
 */
$(document).ready(function(){

	var ACCESS_TOKEN = getCookie('access_token');
	var login_user;
	
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
		login_user = JSON.parse(Base64.decode(payload));

		console.log(login_user.userName);

	}
	
	// 2023-02-14 -> 여기까지
	/**
	 *  7-1. 목록 조회
	 */
	$("#noticeListBtn").on("click", function(){
		console.log("noticeListBtn");
		
		$.ajax({
			type : "GET",
			url : "/api/notices",
			contentType : "application/json; charset=UTF-8",
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
									<th colspan="6">noticeNo</th>
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
								<td colspan="6">${res.data[i].noticeNo}</td>
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
		
	});
	

	/**
	 *  7-2. 상세 조회
	 */
	$("#noticeReadBtn").on("click", function(){
		console.log("noticeReadBtn");
		
		var noticeNo = $("#noticeNo").val();
		
		if(!noticeNo.trim()) {
			alert("noticeNo를 입력해주세요.");
			$("#notice_form #noticeNo").focus();
			return false;
		}
		
		$.ajax({
			type : "GET",
			url : "/api/notices/" + noticeNo,
			contentType : "application/json; charset=UTF-8",
			success : function(res) {
				console.log(res);
				
				$("#noticeTitle").val(res.data.title);
				$("#noticeContent").val(res.data.content);
			},
			error : function(res) {
				console.log(res);
				
				alert(res.responseJSON.message);
					
				$("#noticeNo").val("");
				$("#noticeTitle").val("");
				$("#noticeContent").val("");
				
			}
		});

	});
	
	/**
	 *  7-3. 등록 
	 */
	$("#noticeRegisterBtn").on("click", function(){
		console.log("noticeRegisterBtn");
		
		// 참고 : 이미 security에서 admin계정만 가능하도록 해놨기 때문에 엑세스 토큰이 null인지만 체크하면 된다.
		if(ACCESS_TOKEN != null) {
			var login_username = login_user.userName;
		
			var noticeObject = {
				title : $("#noticeTitle").val(),
				content : $("#noticeContent").val(),
				writer : login_username	
			};
						
			console.log(JSON.stringify(noticeObject));
						
			if(isValid()) {
						
				$.ajax({
					type : "POST",
					url : "/api/notices",
					data : JSON.stringify(noticeObject),
					contentType : "application/json; charset=UTF-8",
					headers: {
						"Authorization" : "Bearer " + ACCESS_TOKEN
					},
					success : function(res) {
						console.log(res);
						if(res.code == 1) {
							alert("Created")
						}
						$("#noticeTitle").val("");
						$("#noticeContent").val("");
					},
					error : function(res) {
						console.log(res);
								
						alert(res.responseJSON.message);
					}
				});	
			}
		} else {
			alert("관리자 계정으로 로그인을 해주세요");
			return false;
		}

	});
	
	/**
	 *  7-5. 삭제
	 */
	$("#noticeDeleteBtn").on("click", function(){
		console.log("noticeDeleteBtn");
		
		if(ACCESS_TOKEN != null) {
			var noticeNo = $("#noticeNo").val();
			
			if(!noticeNo.trim()) {
				alert("noticeNo를 입력해주세요.");
				$("#notice_form #noticeNo").focus();
				return false;
			}
			
			$.ajax({
				type : "DELETE",
				url : "/api/notices/" + noticeNo,
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
			alert("관리자 계정으로 로그인을 해주세요");
			return false;
		}
		
	});
	
	/**
	 *  7-6. 수정
	 */
	$("#noticeModifyBtn").on("click", function(){
		console.log("noticeModifyBtn");
		
		if(ACCESS_TOKEN != null) {
			if(isValid()) {
				var login_username = login_user.userName;
				
				var noticeNo = $("#noticeNo").val();
				
				var noticeObject = {
					title : $("#noticeTitle").val(),
					content : $("#noticeContent").val(),
					writer : login_username	
				};
							
				console.log(JSON.stringify(noticeObject));
			
				$.ajax({
					type : "PUT",
					url : "/api/notices/" + noticeNo,
					data : JSON.stringify(noticeObject),
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
			alert("관리자 계정으로 로그인을 해주세요");
			return false;
		}
		
	});
	
	/* 7-4. 유효성 검사 */
	function isValid() {
		
		var noticeTitle = $("#noticeTitle").val();
		var noticeContent = $("#noticeContent").val();
		
	
		if(!noticeTitle.trim()) {
			alert("제목을 입력해주세요.");
			$("#notice_form #noticeTitle").focus();
			return false;
		}
			
		if(!noticeContent.trim()) {
			alert('내용을 입력해주세요.');
			$("#notice_form #noticeContent").focus();
			return false;
		}
			
		return true;
	}
	
	/**
	 *  7-7. 입력값 리셋
	 */
	$("#noticeResetBtn").on("click", function(){
		console.log("noticeResetBtn");
		
		$("#noticeNo").val("");
		$("#noticeTitle").val("");
		$("#noticeContent").val("");
		
	});	
});