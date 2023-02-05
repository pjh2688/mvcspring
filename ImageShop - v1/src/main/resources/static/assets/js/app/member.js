/**
 * member.js
 */
$(document).ready(function(){
	// 2023-01-18 -> 토큰 발급 후 다른 자바스크립트 파일에서 토큰 공유하는 방법 고안해야함.
	
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
	 *  4-1. job list 정보 조회 -> 2022-12-30 : 2266p
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
		error : function(res) {
			alert(res.message);
			
			
		}
	});
	
	function getJobList(list) {
		let result = `<option value="${list.value}">${list.label}(${list.value})</option>`;
		return result;
	}
	
	// 2022-12-30 -> 등록API까지 완료.
	/**
	 *  5-1. 목록 조회
	 */
	$("#memberListBtn").on("click", function(){
		console.log("memberListBtn");
		
		$.ajax({
			type : "GET",
			url : "/api/users",
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
									<th colspan="2">user_no</th>
									<th colspan="3">user_id</th>
									<th colspan="2">user_name</th>
									<th colspan="5">job</th>
									<th colspan="2">coin</th>
									<th colspan="4">등록일</th>
									<th colspan="4">수정일</th>
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
					html = '<td colspan="5">등록된 게시글이 없습니다.</td>';
				} else {
					for(var i = 0; i < res.data.length; i++) {
						html += `
							<tr>
								<td colspan="2">${res.data.length - i}</td>
								<td colspan="3">${res.data[i].userId}</td>
								<td colspan="2">${res.data[i].userName}</td>
								<td colspan="5">${res.data[i].job}</td>
								<td colspan="2">${res.data[i].coin}</td>
								<td colspan="4">${res.data[i].createdDate}</td>
								<td colspan="4">${res.data[i].modifiedDate == null ? '' : res.data[i].modifiedDate}</td>
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
	 *  5-2. 상세 조회 -> 2023-01-02 -> 여기까지
	 */
	$("#memberReadBtn").on("click", function(){
		console.log("memberReadBtn");
		
		resetSelectBox();
		
		var userNo = $("#userNo").val();
		
		if(!userNo.trim()) {
			alert("userNo를 입력해주세요.");
			$("#join_form #userNo").focus();
			return false;
		}
			
		$.ajax({
			type : "GET",
			url : "/api/users/" + userNo,
			contentType : "application/json; charset=UTF-8",
			headers: {
				"Authorization" : "Bearer " + ACCESS_TOKEN
			},
			success : function(res) {
				console.log(res);
				
				$("#userId").val(res.data.userId);
				$("#userName").val(res.data.userName);
				$("#job").val(res.data.job);
				
				if(res.data.authList[0]) {
					$("#memberAuth0").val(res.data.authList[0].auth);
				}
				
				if(res.data.authList[1]) {
					$("#memberAuth1").val(res.data.authList[1].auth);
				}
				
				if(res.data.authList[2]) {
					$("#memberAuth2").val(res.data.authList[2].auth);
				}
				
			},
			error : function(res) {
				console.log(res);
				
				alert(res.responseJSON.message);
					
				$("#userNo").val("");
				$("#userId").val("");
				$("#userPw").val("");
				$("#userName").val("");
				
				$("#memberAuth0 option:eq(0)").prop("selected", true);
				$("#memberAuth1 option:eq(0)").prop("selected", true);
				$("#memberAuth2 option:eq(0)").prop("selected", true);
			}
		});
	});
	
	// 5-3. 셀렉트 박스 값 리셋.
	function resetSelectBox() {
		
		$("#memberAuth0 option:eq(0)").prop("selected", true);
		$("#memberAuth1 option:eq(0)").prop("selected", true);
		$("#memberAuth2 option:eq(0)").prop("selected", true);
	}
	
	
	/**
	 *  5-4. 등록 
	 */
	$("#memberRegisterBtn").on("click", function(){
		
		var userNoVal = $("#userNo").val();

		var userObject = {
			userId : $("#userId").val(),
			userPw : $("#userPw").val(),
			userName : $("#userName").val(),
			job : $("#job").val()
			
		};
				
		console.log(JSON.stringify(userObject));
		
		if(isValid()) {
		
			$.ajax({
				type : "POST",
				url : "/api/users",
				data : JSON.stringify(userObject),
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					if(res == 1) {
						alert("Created")
					}
					$("#userId").val("");
					$("#userPw").val("");
					$("#userName").val("");
				},
				error : function(res) {
					console.log(res);
					
					alert(res.responseJSON.message);
				}
			});	
			
		}

		/* 5-5. 유효성 검사 */
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
		
	});

	
	/**
	 *  5-6. 삭제
	 */
	$("#memberDeleteBtn").on("click", function(){
		console.log("memberDeleteBtn");
		
		var userNo = $("#userNo").val();
		
		if(!userNo.trim()) {
			alert("userNo를 입력해주세요.");
			$("#join_form #userNo").focus();
			return false;
		}
		
		$.ajax({
			type : "DELETE",
			url : "/api/users/" + userNo,
			contentType : "application/json; charset=UTF-8",
			headers: {
				"Authorization" : "Bearer " + ACCESS_TOKEN
			},
			success : function(res) {
				if(res == 1) {
					alert("Delete")
				}
				
				location.href = "/viewMember";
			},
			error : function(res) {
				console.log(res);
				
				alert(res.responseJSON.message);
					
			}
		});	
		
	});
	
	/**
	 *  5-7. 수정
	 */
	$("#memberModifyBtn").on("click", function(){
		console.log("memberModifyBtn");
		
		if(isValid()) {
			var userNoVal = $("#userNo").val();
			
			var userObject = {
				userId: $("#userId").val(),
				userPw: $("#userPw").val(),
				userName: $("#userName").val(),
				job: $("#job").val(),
				authList: [
					{
						userNo: userNoVal,
						auth: $("#memberAuth0").val()
					},
					{
						userNo: userNoVal,
						auth: $("#memberAuth1").val()
					},
					{
						userNo: userNoVal,
						auth: $("#memberAuth2").val()
					}
				]
			};
			
			console.log(JSON.stringify(userObject));
			
			$.ajax({
				type : "PUT",
				url : "/api/users/" + userNoVal,
				data : JSON.stringify(userObject),
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					console.log(res);
					
					if(res.code == 1) {
						alert("Modified")
					}
					
				},
				error : function(res) {
					console.log(res);
					
					alert(res.responseJSON.message);
					
						
				}
			});	
			
		}
		
	
		/* 5-8. 유효성 검사 */
		function isValid() {
		
			var userNo = $("#userNo").val();
			var userId = $("#userId").val();
			var userPw = $("#userPw").val();
			var userName = $("#userName").val();
				
			if(!userNo.trim()) {
				alert("userNo을 입력해주세요.");
				$("#member_form #userNo").focus();
				return false;
			}
			
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
	});
	
	
	/**
	 *  5-9. 입력값 리셋
	 */
	$("#memberResetBtn").on("click", function(){
		console.log("memberResetBtn");
		
		$("#userNo").val("");
		$("#userId").val("");
		$("#userPw").val("");
		$("#userName").val("");
		
		$("#memberAuth0 option:eq(0)").prop("selected", true);
		$("#memberAuth1 option:eq(0)").prop("selected", true);
		$("#memberAuth2 option:eq(0)").prop("selected", true);
	
	});	
});