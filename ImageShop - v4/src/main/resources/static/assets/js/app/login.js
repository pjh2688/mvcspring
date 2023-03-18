/**
 *	login.js 
 */

$(document).ready(function(){
	
	var ACCESS_TOKEN = "";

	$("#loginBtn").on("click", function(){
		console.log("loginBtn");
		
		if(!$("#userId").val().trim()) {
			alert("userId를 입력해주세요.");
			$("#login_form #userId").focus();
			return false;
		} 
		
		if(!$("#userPw").val().trim()) {
			alert("userPw를 입력해주세요.");
			$("#login_form #userPw").focus();
			return false;
		}	
		
		var loginObject = {
			username : $("#userId").val(),
			password : $("#userPw").val()
				
		};
		
		console.log(JSON.stringify(loginObject));
		
		$.ajax({
			type : "POST",
			url : "/login",
			data : JSON.stringify(loginObject),
			contentType : "application/json; charset=UTF-8",
			success : function(data, textStatus, request) {
				var responseHeader = request.getResponseHeader('Authorization');
	
				ACCESS_TOKEN = responseHeader.substr(7);
				
				console.log("엑세스 토큰 : " + ACCESS_TOKEN);
		
			},
			error : function(res) {
				console.log(res);
				alert(res.responseJSON.message);
				
					
			}
		});	
		
		
	});
	
	$("#myInfoBtn").on("click", function(){
		console.log("myInfoBtn");
		
		if(!$("#userId").val().trim()) {
			alert("userId를 입력해주세요.");
			$("#login_form #userId").focus();
			return false;
		} 
		
		if(!$("#userPw").val().trim()) {
			alert("userPw를 입력해주세요.");
			$("#login_form #userPw").focus();
			return false;
		}	
		
		$.ajax({
			type : "GET",
			url : "/api/users",
			contentType : "application/json; charset=UTF-8",
			headers: {
				"Authorization" : "Bearer " + ACCESS_TOKEN
			},
			success : function(res) {
				
				console.log(res);
				
				
			},
			error : function(res) {
				console.log(res);
				alert(res.responseJSON.message);
				
					
			}
		});	
	});
	
	ACCESS_TOKEN = getCookie('access_token');
	
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
	
	$("#logoutBtn").on("click", function(){
		
		if(ACCESS_TOKEN == null) {
			alert("로그인을 해주세요");
			return false;
		}
		
		location.href = "/logout";
	});
	// 2023-02-07 -> 따로 수정한건 거의 없음.
	
	
});
