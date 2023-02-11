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
	
	console.log(ACCESS_TOKEN);
	
	/**
	 *  6-1. 목록 조회
	 */
	$("#boardListBtn").on("click", function(){
		console.log("boardListBtn");
		
	});
	

	/**
	 *  6-2. 상세 조회
	 */
	$("#boardReadBtn").on("click", function(){
		console.log("boardReadBtn");
		
		
	});
	
	/**
	 *  6-3. 등록 
	 */
	$("#boardRegisterBtn").on("click", function(){
		
		
	});
	
	/* 6-4. 유효성 검사 */
	function isValid() {
		
		var userId = $("#userId").val();
		var userPw = $("#userPw").val();
		var userName = $("#userName").val();
			
		if(!userId.trim()) {
			alert("아이디를 입력해주세요.");
			$("#member_form #userId").focus();
			return false;
		}
			
		if(!userPw.trim()) {
			alert('비밀번호를 입력해주세요.');
			$("#member_form #userPw").focus();
			return false;
		}
			
		if(!userName.trim()) {
			alert('이름을 입력해주세요.');
			$("#member_form #userName").focus();
			return false;
				
		}
			
		return true;
	}

	
	/**
	 *  6-5. 삭제
	 */
	$("#boardDeleteBtn").on("click", function(){
		console.log("boardDeleteBtn");
		
	});
	
	/**
	 *  6-6. 수정
	 */
	$("#boardModifyBtn").on("click", function(){
		console.log("boardModifyBtn");
		
	});
	
	/**
	 *  6-7. 입력값 리셋
	 */
	$("#boardResetBtn").on("click", function(){
		console.log("boardResetBtn");
		
		$("#boardNo").val("");
		$("#boardTitle").val("");
		$("#boardContent").val("");
		$("#boardWriter").val("");
		
	});	
});