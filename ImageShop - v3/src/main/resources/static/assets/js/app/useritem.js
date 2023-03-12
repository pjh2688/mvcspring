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
			
			
		} else {
			alert("로그인을 해주세요.");
			return;
		}
		
	});

	/**
	 *  9-3. 등록 
	 */
	$("#userItemRegisterBtn").on("click", function(){
		console.log("userItemRegisterBtn");
		
		if(ACCESS_TOKEN != null) {
		
			
		} else {
			alert("로그인을 해주세요");
			return;
		}
	});
	
	/**
	 *  9-5. 삭제
	 */
	$("#userItemDeleteBtn").on("click", function(){
		console.log("userItemDeleteBtn");
		
	});
	
	/**
	 *  9-6. 수정
	 */
	$("#userItemModifyBtn").on("click", function(){
		console.log("userItemModifyBtn");
		
	});
	
	/* 9-4. 유효성 검사 */
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
	 *  9-7. 입력값 리셋
	 */
	$("#userItemResetBtn").on("click", function(){
		console.log("userItemResetBtn");
		
		location.reload();
		
	});	
});