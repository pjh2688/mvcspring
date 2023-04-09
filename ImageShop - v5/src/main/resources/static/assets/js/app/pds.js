/**
 * pds.js
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
		
	}
	
	/**
	 *  10-1. 목록 조회
	 */
	$("#pdsItemListBtn").on("click", function(){
		console.log("pdsItemListBtn");
		
		if(ACCESS_TOKEN != null) {
			$.ajax({
				type : "GET",
				url : "/api/pds",
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
										<th colspan="6">pdsFileId</td>
										<th colspan="6">pdsItemId</th>
										<th colspan="14">fileName</th>
										<th colspan="6">downCnt</th>
										<th colspan="10">등록일</th>
										<th colspan="10">수정일</th>
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
						html = '<td colspan="53">등록된 파일이 없습니다.</td>';
					} else {
						for(var i = 0; i < res.data.length; i++) {
							html += `
								<tr>
									<td colspan="2">${res.data.length - i}</td>
									<td colspan="6">${res.data[i].pdsFileId}</td>
									<td colspan="6">${res.data[i].pdsItemId}</td>
									<td colspan="14">${res.data[i].fileName}</td>
									<td colspan="6">${res.data[i].downCnt}</td>
									<td colspan="10">${res.data[i].createdDate}</td>
									<td colspan="10">${res.data[i].modifiedDate == null ? '' : res.data[i].modifiedDate}</td>
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
	 *  10-2. 단건 조회
	 */
	$("#pdsItemReadBtn").on("click", function(){
		console.log("pdsItemReadBtn");
		
		if(ACCESS_TOKEN != null) {
			var pdsItemId = $("#pdsItemId").val();
			
			console.log(pdsItemId);
			
			if(!pdsItemId.trim()) {
				alert("pdsItemId를 입력해주세요.");
				$("#pds_form #pdsItemId").focus();
				return false;
			}
			
			$.ajax({
				type : "GET",
				url : "/api/pds/" + pdsItemId,
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					
					console.log(res);
				
					$("#pdsItemId").val(res.data.pdsItemId);
					$("#pdsItemName").val(res.data.pdsItemName);
					$("#pdsItemDescription").val(res.data.description);
					
					$.ajax({
						type : "GET",
						url : "/api/pds/" + pdsItemId + "/files",
						contentType : "application/json; charset=UTF-8",
						headers: {
							"Authorization" : "Bearer " + ACCESS_TOKEN
						},
						success : function(res) {
							// 2023-03-22 -> 여기까지 완료.
							console.log(res);
							console.log(res.data.length);
							
							$("#pdsInputFile").hide();
							
							$("#file_list").empty();
						
							// 다음에 파일 업로드 갯수 제한해줘야된다. 2개로
							for(var i = 0; i < res.data.length; i++) {
								// 내일 여기서부터 다시-> 데이터는 가져오니 다운로드만 연결해주자. onclick동작 item.js 참고
										
								$("#file_list").append("<a onclick='' id='fileName" + i  +"'>" + res.data[i].fileName + "</a>");
								// 2023-04-04 -> 삭제 버튼 부터 다시
								$("#file_list").append("<a onclick='' id='deleteFileBtn" + i  +"'>" + res.data[i].pdsFileId + "</a>");		
								$("#file_list").append("<br/>");
								
							}
													
							$("#file_list").append("<input type='file' id='pdsUpdateFile' name='pds' multiple='multiple'>");
							
							// 지금은 파일 2개 0, 1번 밖에 안됨. 수정해야함.
							var fileName0 = document.getElementById('fileName0');
							
							var deleteFileBtn0 = document.getElementById('deleteFileBtn0');
							
							console.log(deleteFileBtn0);
							
							if(deleteFileBtn0 != null) {
								deleteFileBtn0.onclick = function(event) {
									var pdsFileId = res.data[0].pdsFileId;
									
									console.log(pdsFileId + "번 파일 삭제");
									
									$.ajax({
										type : "DELETE",
										url : "/api/pds/" + pdsFileId + "/file",
										contentType : "application/json; charset=UTF-8",
										headers: {
											"Authorization" : "Bearer " + ACCESS_TOKEN
										},
										success : function(res) {
											
											// 2023-04-05 -> 여기까지 
											alert(res.message);
											location.href = '/viewPds';
										},
										error : function(res) {
											console.log(res);
											alert(res.responseJSON.message);
										}
									});
									
								}
							}
							
							if(fileName0 != null) {
								fileName0.onclick = function(event) {
									console.log("파일 다운로드");
									
									var request = new XMLHttpRequest();
									request.open("GET", "/api/pds/download/" + res.data[0].pdsFileId, true);
									request.responseType = "blob";
									request.setRequestHeader("Authorization", "Bearer " + ACCESS_TOKEN);
									
									console.log(request);
									
									request.onload = function(event) {
										var responseHeader = request.getResponseHeader("Content-Disposition");
										var type = request.getResponseHeader("Content-Type");
										
										console.log("type : " + type);
										
										console.log(responseHeader);
										
										// response header에 셋팅된 Content-Disposition의 값을 22번째부터 읽어서 가져와라.
										var downloadFilename = responseHeader.substring(22, responseHeader.length - 1);
										
										var decodedDownloadFilename = decodeURIComponent(escape(downloadFilename));
										
										console.log(decodedDownloadFilename);
										
										var blob = request.response;
										
										console.log(blob.size);
										
										var link = document.createElement('a');
										
										link.href = window.URL.createObjectURL(blob);
										link.download = decodedDownloadFilename;
										link.click();
				
									}
									
									request.send();
								}
							} 
							
							var fileName1 = document.getElementById('fileName1');
							
							if(fileName1 != null) {
								fileName1.onclick = function(event) {
									console.log("파일 다운로드");
									
									var request = new XMLHttpRequest();
									request.open("GET", "/api/pds/download/" + res.data[1].pdsFileId, true);
									request.responseType = "blob";
									request.setRequestHeader("Authorization", "Bearer " + ACCESS_TOKEN);
									
									console.log(request);
									
									request.onload = function(event) {
										var responseHeader = request.getResponseHeader("Content-Disposition");
										var type = request.getResponseHeader("Content-Type");
										
										console.log("type : " + type);
										
										console.log(responseHeader);
										
										// response header에 셋팅된 Content-Disposition의 값을 22번째부터 읽어서 가져와라.
										var downloadFilename = responseHeader.substring(22, responseHeader.length - 1);
										
										var decodedDownloadFilename = decodeURIComponent(escape(downloadFilename));
										
										console.log(decodedDownloadFilename);
										
										var blob = request.response;
										
										console.log(blob.size);
										
										var link = document.createElement('a');
										
										link.href = window.URL.createObjectURL(blob);
										link.download = decodedDownloadFilename;
										link.click();
				
									}
									
									request.send();
								}	
							}
							
						},
						error : function(res) {
							console.log(res);
							alert(res.responseJSON.message);
						}
						
						
					});
					
	
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
	 *  10-3. 등록 
	 */
	$("#pdsItemRegisterBtn").on("click", function(){
		console.log("pdsItemRegisterBtn");
		
		// 2023-03-17 -> 여기서부터 막힘
		if(ACCESS_TOKEN != null) {
			
			var itemObject = {
				pdsItemId : $("#pdsItemId").val(),
				pdsItemName : $("#pdsItemName").val(),
				description : $("#pdsItemDescription").val()
			}
	
			var fileList = $("#pdsInputFile")[0].files;
			
			var formData = new FormData();
			formData.append("item", JSON.stringify(itemObject));
			
			for(var i = 0; i < fileList.length; i++) {
				formData.append("files", fileList[i]);
			}
			
			console.log(formData.get('item'));
			console.log(formData.get('files'));
			
			$.ajax({
				type : "POST",
				url : "/api/pds",
				data: formData,
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				processData: false,
				contentType: false,
				success : function(res) {
					if(res.code == 1) {
						alert("Created");
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
	 *  10-4. 삭제
	 */
	$("#pdsItemDeleteBtn").on("click", function(){
		console.log("pdsItemDeleteBtn");
		
		if(ACCESS_TOKEN != null) {
		
		
		} else {
			alert("로그인을 해주세요.");
			return;
		}
	});

	/**
	 *  10-5. 수정 
	 */
	$("#pdsItemModifyBtn").on("click", function(){
		console.log("pdsItemModifyBtn");
		
		var pdsItemId = $("#pdsItemId").val();
		
		if(ACCESS_TOKEN != null) {
			var pdsItemObject = {
				pdsItemId : $("#pdsItemId").val(),
				pdsItemName : $("#pdsItemName").val(),
				description : $("#pdsItemDescription").val()
			};
			
			console.log(pdsItemObject);
			
			var formData = new FormData();
			formData.append("item", JSON.stringify(pdsItemObject));
			
			if(document.getElementById('pdsUpdateFile')) {
				var fileList = $("#pdsUpdateFile")[0].files;
				
				for(var i = 0; i < fileList.length; i++) {
					formData.append("files", fileList[i]);
				}
				
				console.log(formData.get('files'));
				
			}
			
			console.log(formData.get('item'));
			
			// 파일을 수정하면 첨부파일 전체가 수정됨. 이거 나중에 손보자.
			if(isValid()) {  
				$.ajax({
					type : "PUT",
					url : "/api/pds/" + $("#pdsItemId").val(),
					data: formData,
					headers: {
						"Authorization" : "Bearer " + ACCESS_TOKEN
					},
					processData: false,
					contentType: false,
					success : function(res) {
						if(res.code == 1) {
							alert("Modified");
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
	

	/**
	 *  10-3. 입력값 리셋
	 */
	$("#pdsItemResetBtn").on("click", function(){
		console.log("pdsItemResetBtn");
		
		location.reload();
		
	});
	
	/* 7-4. 유효성 검사 */
	function isValid() {
		
		var pdsItemId = $("#pdsItemId").val();
		var pdsItemName = $("#pdsItemName").val();
		var description = $("#pdsItemDescription").val();
			
		if(!pdsItemId.trim()) {
			alert("pdsItemId를 입력해주세요.");
			$("#pds_form #pdsItemId").focus();
			return false;
		}
			
		if(!pdsItemName.trim()) {
			alert('pdsItemName을 입력해주세요.');
			$("#pds_form #pdsItemName").focus();
			return false;
		}
			
		if(!description.trim()) {
			alert('상품 상세를 입력해주세요.');
			$("#pds_form #description").focus();
			return false;
				
		}
			
		return true;
	}
	
	

});