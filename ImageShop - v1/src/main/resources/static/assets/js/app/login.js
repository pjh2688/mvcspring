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
	
	$("#logoutBtn").on("click", function(){
		console.log("logoutBtn");
		
		location.href = "/logout";
	});
	
	
	
});
