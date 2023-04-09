/**
 * setup-admin.js
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
	 *  2023-01-05 -> 여기까지
	 */
	$.ajax({
		type : "GET",
		url : "/api/codes/job",
		contentType : "application/json; charset=UTF-8",
		success : function(res) {
		
			res.data.forEach((list) => {
				let jobList = getJobList(list);
				
				$("#job").append(jobList);
				
			});
		},
		error : function(xhr, textStatus, error) {
			alert(xhr.responseJSON.message);
			
		}
	});
	
	function getJobList(list) {
		let result = `<option value="${list.value}">${list.label}(${list.value})</option>`;
		return result;
	}
	
	$("#adminRegisterBtn").on("click", function() {
		console.log("adminRegisterBtn");
		
		var userObject = {
			userId : $("#adminId").val(),
			userPw : $("#adminPw").val(),
			userName: $("#adminName").val(),
			job : $("#job").val()
		}
		
		console.log(JSON.stringify(userObject));
		
		if(ACCESS_TOKEN != null) {
		
			if(isValid()) {
			
				$.ajax({
					type : "POST",
					url : "/api/users/setup",
					headers: {
						"Authorization" : "Bearer " + ACCESS_TOKEN
					},
					data : JSON.stringify(userObject),
					contentType : "application/json; charset=UTF-8",
					success : function(res) {
						if(res == 1) {
							alert("Created")
						}
						$("#adminId").val("");
						$("#adminPw").val("");
						$("#adminName").val("");
					},
					error : function(xhr, textStatus, error) {
						alert(xhr.responseJSON.message);
						
					}
				});	
			}
		} else {
			alert("관리자 계정으로 로그인을 해주세요.");
			return;
		}

		/* 1-4. 유효성 검사 */
		function isValid() {
			
			var adminId = $("#adminId").val();
			var adminPw = $("#adminPw").val();
			var adminName = $("#adminName").val();
				
			if(!adminId.trim()) {
				alert("아이디를 입력해주세요.");
				$("#member_form #adminId").focus();
				return false;
			}
				
			if(!adminPw.trim()) {
				alert('비밀번호를 입력해주세요.');
				$("#member_form #adminPw").focus();
				return false;
			}
				
			if(!adminName.trim()) {
				alert('이름을 입력해주세요.');
				$("#member_form #adminName").focus();
				return false;
					
			}
				
			return true;
		}
		
		
	});
	
	$("#adminResetBtn").on("click", function() {
		console.log("adminResetBtn");
		
		$("#adminId").val("");
		$("#adminPw").val("");
		$("adminName").val("");
	});
	
});