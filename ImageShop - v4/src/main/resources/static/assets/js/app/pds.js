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
			var itemId = $("#pdsItemId").val();
			
			console.log(itemId);
			
			if(!itemId.trim()) {
				alert("itemId를 입력해주세요.");
				$("#pds_form #pdsItemId").focus();
				return false;
			}
			
			
		
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
			
			var fileList = $("#pdsInputFile")[0].files;
			
			var itemObject = {
				itemId : $("#pdsItemId").val(),
				itemName : $("#pdsItemName").val(),
				description : $("#pdsItemDescription").val()
			}
			
			console.log(itemObject);
			console.log(JSON.stringify(itemObject));
			
			var formData = new FormData();
			formData.append("item", JSON.stringify(itemObject));
			formData.append("files", fileList);
			console.log(formData.get('item'));
			console.log(formData.get('files'));
			
			for(var i = 1; i < fileList.length + 1; i++) {
				formData.append("file_" + i, fileList[i-1]);
			}
			
			console.log(formData.get("file_1"));
			console.log(formData.get("file_2"));
			
			
			
		
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
		
		if(ACCESS_TOKEN != null) {
		
		
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
	

});