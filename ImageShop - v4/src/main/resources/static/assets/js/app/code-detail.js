/**
 *  code-detail.js
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
	 *  2-1. 그룹코드 정보 조회 -> 2022-12-26 : 2266p
	 */
	if(ACCESS_TOKEN != null) {
		$.ajax({
			type : "GET",
			url : "/api/codes/codegroup",
			contentType : "application/json; charset=UTF-8",
			headers: {
				"Authorization" : "Bearer " + ACCESS_TOKEN
			},
			success : function(res) {
		
				res.data.forEach((codegroup) => {
					let codegroupItem = getCodeGroupItem(codegroup);
				
					$("#codeGroupCode").append(codegroupItem);
				
				});
			},
			error : function(xhr, textStatus, error) {
				alert(xhr.responseJSON.message);
			
			}
		});
	} else {
		alert("로그인을 해주세요.");
		location.href = "/loginForm";
	}
	
	function getCodeGroupItem(codegroup) {
		let item = `<option value="${codegroup.value}">${codegroup.label}(${codegroup.value})</option>`;
		
		return item;
	}
	/**
	 *  2-2. codeDetail 정보 등록
	 */
	$("#codeDetailRegisterBtn").on("click", function(){
		console.log("#codeDetailRegisterBtn");
		
		if(ACCESS_TOKEN != null) {
		
			if($("#codeValue").val() == '') {
				alert("codeValue값을 입력해주세요.");
				return false;
			}
		
			if($("#codeName").val() == '') {
				alert("codeName값을 입력해주세요.");
				return false;
			}
		
			var codeDetailObject = {
				groupCode : $("#codeGroupCode").val(),
				codeValue : $("#codeValue").val(),
				codeName : $("#codeName").val()
			}
		
			console.log(JSON.stringify(codeDetailObject));
		
			$.ajax({
				type : "POST",
				url : "/api/codedetails",
				data : JSON.stringify(codeDetailObject),
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					if(res == 1) {
						alert("Created")
					}
					$("#codeValue").val("");
					$("#codeName").val("");
				},
				error : function(xhr, textStatus, error) {
					alert(xhr.responseJSON.message);
				
					$("#codeValue").val("");
					$("#codeName").val("");
				}
			});
		} else {
			alert("로그인을 해주세요.");
			location.href = "/loginForm";
		}
		
		
	});
	
	/**
	 *  2-3. codeDetail List 조회
	 */
	$("#codeDetailListBtn").on("click", function(){
		console.log("#codeDetailListBtn");
		
		$.ajax({
			type : "GET",
			url : "/api/codedetails",
			contentType : "application/json; charset=UTF-8",
			success : function(res) {
				console.log(res);
				
				console.log(res.data.length);
				
				var html = `
					<!-- /* 게시글 영역 */ -->
					<div class="table-responsive clearfix">
						<table class="table table-hover">
							<thead>
								<tr>
									<th colspan="2">번호</th>
									<th colspan="3">groupCode</th>
									<th colspan="2">codeValue</th>
									<th colspan="5">codeName</th>
									<th colspan="2">userYn</th>
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
								<td colspan="3">${res.data[i].groupCode}</td>
								<td colspan="2">${res.data[i].codeValue}</td>
								<td colspan="5">${res.data[i].codeName}</td>
								<td colspan="2">${res.data[i].useYn}</td>
								<td colspan="4">${res.data[i].createdDate}</td>
								<td colspan="4">${res.data[i].modifiedDate == null ? '' : res.data[i].modifiedDate}</td>
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
	 *  2-4. codeDetail 상세 정보 조회 -> 2022-12-27 : 여기서부터 다시
	 */
	$("#codeDetailReadBtn").on("click", function(){
		console.log("#codeDetailReadBtn");
		
		if($("#codeValue").val() == '') {
			alert("codeValue값을 입력해주세요.");
			return false;
		}
		
		let groupCode = $("#codeGroupCode").val();
		let codeValue = $("#codeValue").val();
		
		$.ajax({
			type : "GET",
			url : "/api/codedetails/" + groupCode + "/" + codeValue,
			contentType : "application/json; charset=UTF-8",
			success : function(res) {
				console.log(res);
				
				$("#codeName").val(res.data.codeName);
				
			},
			error : function(xhr, textStatus, error) {
				alert(xhr.responseJSON.message);
				
				$("#codeValue").val("");
				$("#codeName").val("");
			}
		});
	});
	
	/**
	 *  2-5. codeDetail 정보 삭제
	 */
	$("#codeDetailDeleteBtn").on("click", function(){
		console.log("#codeDetailDeleteBtn");
		
		if(ACCESS_TOKEN != null) {
		
			if($("#codeValue").val() == '') {
				alert("codeValue값을 입력해주세요.");
				return false;
			}	
		
			let groupCode = $("#codeGroupCode").val();
			let codeValue = $("#codeValue").val();
		
			$.ajax({
				type : "DELETE",
				url : "/api/codedetails/" + groupCode + "/" + codeValue,
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
				
					$("#codeValue").val("");
					$("#codeName").val("");
				}
			});
		} else {
			alert("관리자 계정으로 로그인을 해주세요.");
			return;
		}
	});
	
	/**
	 *  2-6. codeDetail 정보 수정
	 */
	$("#codeDetailModifyBtn").on("click", function(){
		console.log("#codeDetailModifyBtn");
		
		if(ACCESS_TOKEN != null) {
		
			if($("#codeValue").val() == '') {
				alert("codeValue값을 입력해주세요.");
				return false;
			}
		
			if($("#codeName").val() == '') {
				alert("codeName값을 입력해주세요.");
				return false;
			}
		
			var codeDetailObject = {
				groupCode : $("#codeGroupCode").val(),
				codeValue : $("#codeValue").val(),
				codeName : $("#codeName").val()
			};
		
			let groupCode = $("#codeGroupCode").val();
			let codeValue = $("#codeValue").val();
		
			$.ajax({
				type : "PUT",
				url : "/api/codedetails/" + groupCode + "/" + codeValue,
				data : JSON.stringify(codeDetailObject),
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
				
					$("#codeValue").val("");
					$("#codeName").val("");
				}
			});
		} else {
			alert("관리자 계정으로 로그인을 해주세요.");
			return;
		}
	});
	
	/*
	 * 2-7. codeDetail 입력폼으로 reset
	 */
	$("#codeDetailResetBtn").on("click", function(){
		console.log("#codeDetailResetBtn");
		
		$("#codeValue").val("");
		$("#codeName").val("");
	});
	
});