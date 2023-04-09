/**
 * useritem.js
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
	 *  9-1. 목록 조회
	 */
	$("#userItemListBtn").on("click", function(){
		console.log("userItemListBtn");
		
		if(ACCESS_TOKEN != null) {
			
			$.ajax({
				type : "GET",
				url : "/api/useritems",
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
										<th colspan="4">no</td>
										<th colspan="11">userItemNo</td>
										<th colspan="8">userNo</th>
										<th colspan="8">itemId</th>
										<th colspan="14">itemName</th>
										<th colspan="7">price</th>
										<th colspan="12">description</th>
										<th colspan="14">등록일</th>
										<th colspan="14">수정일</th>
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
						html = '<td colspan="53">등록된 아이템이 없습니다.</td>';
					} else {
						for(var i = 0; i < res.data.length; i++) {
							html += `
								<tr>
									<td colspan="4">${res.data.length - i}</td>
									<td colspan="11">${res.data[i].userItemNo}</td>
									<td colspan="8">${res.data[i].userNo}</td>
									<td colspan="8">${res.data[i].itemId}</td>
									<td colspan="14">${res.data[i].itemName}</td>
									<td colspan="7">${res.data[i].price}</td>
									<td colspan="12">${res.data[i].description}</td>
									<td colspan="14">${res.data[i].createdDate}</td>
									<td colspan="14">${res.data[i].modifiedDate == null ? '' : res.data[i].modifiedDate}</td>
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
	 *  9-2. 상세 조회
	 */
	$("#userItemReadBtn").on("click", function(){
		console.log("userItemReadBtn");
		
		if(ACCESS_TOKEN != null) {
			var userItemNo = $("#userItemNo").val();
			
			if(!userItemNo.trim()) {
				alert("userItemNo를 입력해주세요.");
				$("#userItem_form #userItemNo").focus();
				return false;
			}
		
			$.ajax({
				type : "GET",
				url : "/api/useritems/" + userItemNo,
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					console.log(res);
				
					$("#userNo").val(res.data.userNo);
					$("#itemId").val(res.data.itemId);
					$("#itemName").val(res.data.itemName);
					$("#price").val(res.data.price);
					$("#description").val(res.data.description);
					
					$("#picture").empty();
					
					var fileName = document.getElementById('fileName');
					fileName.innerText = res.data.pictureName;
					
					$("#itemInputFile").hide();
					
					var str = "<img src=/upload/" + res.data.pictureUrl + " width=100 height=100>";
					
					$("#picture").append(str);
					
					fileName.onclick = function() {
						console.log("file 다운로드");
						
						var request = new XMLHttpRequest();
						
						console.log(request);
						
						request.open("GET", "/api/items/download/" + res.data.itemId + "/" + "profile", true);
						request.responseType = "blob";
						request.setRequestHeader("Authorization", "Bearer " + ACCESS_TOKEN);
						
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
	 *  9-3. 입력값 리셋
	 */
	$("#userItemResetBtn").on("click", function(){
		console.log("userItemResetBtn");
		
		location.reload();
		
	});	
});