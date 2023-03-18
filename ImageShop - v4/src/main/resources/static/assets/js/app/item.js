/**
 * item.js
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
	 *  7-1. 목록 조회
	 */
	$("#itemListBtn").on("click", function(){
		console.log("itemListBtn");
		
		if(ACCESS_TOKEN != null) {
		
			$.ajax({
				type : "GET",
				url : "/api/items",
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
										<th colspan="4">itemId</td>
										<th colspan="10">itemName</th>
										<th colspan="8">price</th>
										<th colspan="14">description</th>
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
						html = '<td colspan="53">등록된 아이템이 없습니다.</td>';
					} else {
						for(var i = 0; i < res.data.length; i++) {
							html += `
								<tr>
									<td colspan="2">${res.data.length - i}</td>
									<td colspan="4">${res.data[i].itemId}</td>
									<td colspan="10">${res.data[i].itemName}</td>
									<td colspan="8">${res.data[i].price}</td>
									<td colspan="14">${res.data[i].description}</td>
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
	 *  7-2. 상세 조회
	 */
	$("#itemReadBtn").on("click", function(){
		console.log("itemReadBtn");
		
		if(ACCESS_TOKEN != null) {
			
			var itemId = $("#itemId").val();
		
			if(!itemId.trim()) {
				alert("itemId를 입력해주세요.");
				$("#item_form #itemId").focus();
				return false;
			}
		
			$.ajax({
				type : "GET",
				url : "/api/items/" + itemId,
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					console.log(res);
				
					$("#itemName").val(res.data.itemName);
					$("#price").val(res.data.price);
					$("#description").val(res.data.description);
					
					$("#preview01").empty();
					
					var fileName01 = document.getElementById('fileName01');
					fileName01.innerText = res.data.pictureName;
					fileName01.onclick = function() {
						console.log("file01 다운로드");
						
						var request = new XMLHttpRequest();
						
						console.log(request);
						
						request.open("GET", "/api/items/download/" + itemId + "/" + "profile", true);
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
					
					var str1 = "<img src=/upload/" + res.data.pictureUrl + " width=100 height=100>";
					
					$("#preview01").append(str1);
					
					$("#preview02").empty();
					
					var fileName02 = document.getElementById('fileName02');
					fileName02.innerText = res.data.previewName;
					fileName02.onclick = function() {
						console.log("file02 다운로드");
				
						var request = new XMLHttpRequest();
						
						console.log(request);
						
						request.open("GET", "/api/items/download/" + itemId + "/" + "preview", true);
						request.responseType = "blob";
						request.setRequestHeader("Authorization", "Bearer " + ACCESS_TOKEN);
						
						request.onload = function(event) {
							var responseHeader = request.getResponseHeader("Content-Disposition");
							var type = request.getResponseHeader("Content-Type");
							
							console.log("type : " + type);
							
							console.log(responseHeader);
							
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
			
					var str2 = "<img src=/upload/" + res.data.previewUrl + " width=100 height=100>";
					
					$("#preview02").append(str2);
					
					// 2023-02-27 -> 이미지 불러오기 성공
	
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

	// 2023-03-09 -> 약간 수정.
	/**
	 *  7-3. 등록 
	 */
	$("#itemRegisterBtn").on("click", function(){
		console.log("itemRegisterBtn");
		
		if(ACCESS_TOKEN != null) {
			
			var itemId = $("#itemId").val();
		
			var itemObject = {
				itemName : $("#itemName").val(),
				price : $("#price").val(),
				description : $("#description").val()
			};
		
			console.log(itemObject);
		
			var picture_file = $("input[name=picture]")[0].files[0];
		
			console.log(picture_file);
		
			var preview_file = $("input[name=picture]")[1].files[0];
		
			console.log(preview_file);
		
			var formData = new FormData();
		
			formData.append("item", JSON.stringify(itemObject));
			formData.append("picture_file", picture_file);
			formData.append("preview_file", preview_file);
		
			console.log(formData.get('item'));
			console.log(formData.get('picture_file'));
			console.log(formData.get('preview_file'));
		
			if(!itemId.trim()) {
				$.ajax({
					type : "POST",
					url : "/api/items",
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
				alert("itemId를 비워주세요");
				location.reload();
			}
			
		} else {
			alert("로그인을 해주세요");
			return;
		}
	});
	
	/**
	 *  7-5. 삭제
	 */
	$("#itemDeleteBtn").on("click", function(){
		console.log("itemDeleteBtn");
		
	});
	
	/**
	 *  7-6. 수정
	 */
	$("#itemModifyBtn").on("click", function(){
		console.log("itemModifyBtn");
		
		if(ACCESS_TOKEN != null) {
			
			var itemObject = {
				itemId : $("#itemId").val(),
				itemName : $("#itemName").val(),
				price : $("#price").val(),
				description : $("#description").val()
			};
		
			console.log(itemObject);
		
			var picture_file = $("input[name=picture]")[0].files[0];
		
			console.log(picture_file);
		
			var preview_file = $("input[name=picture]")[1].files[0];
		
			console.log(preview_file);
		
			var formData = new FormData();
		
			formData.append("item", JSON.stringify(itemObject));
			formData.append("picture_file", picture_file);
			formData.append("preview_file", preview_file);
		
			console.log(formData.get('item'));
			console.log(formData.get('picture_file'));
			console.log(formData.get('preview_file'));
		
			$.ajax({
				type : "PUT",
				url : "/api/items/" + $("#itemId").val(),
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
		} else {
			alert("로그인을 해주세요");
			return;
		}
		
	});
	
	/* 7-4. 유효성 검사 */
	function isValid() {
		
		var itemName = $("#itemName").val();
		var price = $("#price").val();
		var description = $("#description").val();
			
		if(!itemName.trim()) {
			alert("상품명을 입력해주세요.");
			$("#item_form #itemName").focus();
			return false;
		}
			
		if(!price.trim()) {
			alert('상품 가격을 입력해주세요.');
			$("#item_form #price").focus();
			return false;
		}
			
		if(!description.trim()) {
			alert('상품 상세를 입력해주세요.');
			$("#item_form #description").focus();
			return false;
				
		}
			
		return true;
	}
	
	/**
	 *  7-8. 아이템 파일 이미지 다운도르
	 */
	$("#itemDownloadBtn").on("click", function(){
		console.log("itemDownloadBtn");
		
	});
	
	/**
	 *  7-9. 아이템 구입
	 */
	$("#itemBuyBtn").on("click", function(){
		console.log("itemBuyBtn");
		
		if(ACCESS_TOKEN != null) {
			
			var itemId = $("#itemId").val();
		
			if(!itemId.trim()) {
				alert("itemId를 입력해주세요.");
				$("#item_form #itemId").focus();
				return false;
			}
		
			$.ajax({
				type : "GET",
				url : "/api/items/buy/" + itemId,
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					console.log(res);
					
					if(res.code == 1) {
						alert(res.message);
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
	 *  7-7. 입력값 리셋
	 */
	$("#itemResetBtn").on("click", function(){
		console.log("itemResetBtn");
		
		location.reload();
		
	});	
	
	
	
});