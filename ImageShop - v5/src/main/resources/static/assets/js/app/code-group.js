/**
 *  code-group.js
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
	
	/**
	 *  1-1. 목록 조회
	 */
	$("#codeGroupListBtn").on("click", function(){
		console.log("codeGroupListBtn");
		
		$.ajax({
			type : "GET",
			url : "/api/codegroups",
			contentType : "application/json; charset=UTF-8",
			success : function(res) {
				console.log(res);
				
				var html = `
					<!-- /* 게시글 영역 */ -->
					<div class="table-responsive clearfix">
						<table class="table table-hover">
							<thead>
								<tr>
									<th colspan="1">번호</th>
									<th colspan="2">groupCode</th>
									<th colspan="1">groupName</th>
									<th colspan="2">등록일</th>
									<th colspan="2">수정일</th>
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
				
				if(!res.length) {
					html = '<td colspan="5">등록된 게시글이 없습니다.</td>';
				} else {
					for(var i = 0; i < res.length; i++) {
						html += `
							<tr>
								<td colspan="1">${res.length - i}</td>
								<td colspan="2">${res[i].groupCode}</td>
								<td colspan="1">${res[i].groupName}</td>
								<td colspan="2">${res[i].createdDate}</td>
								<td colspan="2">${res[i].modifiedDate == null ? '' : res[i].modifiedDate}</td>
							</tr>
						`;
					}
				}
				document.getElementById('list').innerHTML = html;
				
			},
			error : function(xhr, textStatus, error) {
				alert(xhr.responseJSON.message);
			
			}
		});
		
	});
	

	/**
	 *  1-2. 상세 조회
	 */
	$("#codeGroupReadBtn").on("click", function(){
		console.log("codeGroupReadBtn");
		
		if($("#groupCode").val() == '') {
			alert("그룹코드를 입력해주세요.");
			return false;
		}
		
		let groupCode = $("#groupCode").val();
		
		$.ajax({
			type : "GET",
			url : "/api/codegroups/" + groupCode,
			contentType : "application/json; charset=UTF-8",
			success : function(res) {
				console.log(res);
				
				$("#groupName").val(res.groupName);
				
			},
			error : function(xhr, textStatus, error) {
				alert(xhr.responseJSON.message);
				
				$("#groupCode").val("");
				$("#groupName").val("");
			}
		});
	});
	
	/**
	 *  1-3. 등록
	 */
	$("#codeGroupRegisterBtn").on("click", function(){
		console.log("codeGroupRegisterBtn");
		
		if(ACCESS_TOKEN != null) {
			
		
			if($("#groupCode").val() == '') {
				alert("그룹코드를 입력해주세요.");
				return false;
			}
		
			if($("#groupName").val() == '') {
				alert("그룹이름을 입력해주세요.");
				return false;
			}
		
			var codeGroupObject = {
				groupCode : $("#groupCode").val(),
				groupName : $("#groupName").val()
			};
		
			alert(JSON.stringify(codeGroupObject));
		
			$.ajax({
				type : "POST",
				url : "/api/codegroups",
				data : JSON.stringify(codeGroupObject),
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					if(res == 1) {
						alert("Created")
					}
				},
				error : function(xhr, textStatus, error) {
					alert(xhr.responseJSON.message);
				
					$("#groupCode").val("");
					$("#groupName").val("");
				}
			});
		} else {
			alert("관리자 계정으로 로그인을 해주세요.");
			return;
		}
	});
	
	/**
	 *  1-4. 삭제
	 */
	$("#codeGroupDeleteBtn").on("click", function(){
		console.log("codeGroupDeleteBtn");
		
		if(ACCESS_TOKEN != null) {
		
			if($("#groupCode").val() == '') {
				alert("그룹코드를 입력해주세요.");
				return false;
			}
		
			$.ajax({
				type : "DELETE",
				url : "/api/codegroups/" + $("#groupCode").val(),
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					if(res == 1) {
						alert("DELETE");
					}
				},
				error : function(xhr, textStatus, error) {
					alert(xhr.responseJSON.message);
				
					$("#groupCode").val("");
					$("#groupName").val("");
				}
			});
		} else {
			alert("관리자 계정으로 로그인을 해주세요.");
			return;
		}
	});
	
	/**
	 *  1-5. 수정
	 */
	$("#codeGroupModifyBtn").on("click", function(){
		console.log("codeGroupModifyBtn");
		
		if(ACCESS_TOKEN != null) {
			if($("#groupCode").val() == '') {
				alert("그룹코드를 입력해주세요.");
				return false;
			}
		
			if($("#groupName").val() == '') {
				alert("그룹이름을 입력해주세요.");
				return false;
			}
		
			var codeGroupObject = {
				groupCode : $("#groupCode").val(),
				groupName : $("#groupName").val()
			};
		
			$.ajax({
				type : "PUT",
				url : "/api/codegroups/" + $("#groupCode").val(),
				data : JSON.stringify(codeGroupObject),
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					if(res == 1) {
						alert("Modified");
					}
				},
				error : function(xhr, textStatus, error) {
					alert(xhr.responseJSON.message);
				
					$("#groupCode").val("");
					$("#groupName").val("");
				}
			});
		
		} else {
			alert("관리자 계정으로 로그인을 해주세요.");
			return;
		}
	});
	
	/**
	 *  1-6. 입력값 리셋
	 */
	$("#codeGroupResetBtn").on("click", function(){
		console.log("codeGroupResetBtn");
		
		$("#groupCode").val("");
		$("#groupName").val("");
	});
	
});