/**
 * coin.js
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
	 *  8-1. 목록 조회
	 */
	$("#coinChargeListBtn").on("click", function(){
		console.log("coinChargeListBtn");
		
		if(ACCESS_TOKEN != null) {
			$.ajax({
				type : "GET",
				url : "/api/coins/charge",
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
										<th colspan="4">historyNo</th>
										<th colspan="3">userNo</th>
										<th colspan="3">amount</th>
										<th colspan="6">등록일</th>
										<th colspan="6">수정일</th>
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
						html = '<td colspan="53">등록된 게시글이 없습니다.</td>';
					} else {
						for(var i = 0; i < res.data.length; i++) {
							html += `
								<tr>
									<td colspan="2">${res.data.length - i}</td>
									<td colspan="4">${res.data[i].historyNo}</td>
									<td colspan="3">${res.data[i].userNo}</td>
									<td colspan="3">${res.data[i].amount}</td>
									<td colspan="6">${res.data[i].createdDate}</td>
									<td colspan="6">${res.data[i].modifiedDate == null ? '' : res.data[i].modifiedDate}</td>
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
	 *  8-2. 등록 -> 2023-03-03 : 코인 충전까지 완료.
	 */
	$("#coinChargeBtn").on("click", function(){
		console.log("coinChargeBtn");
		
		if(ACCESS_TOKEN != null) {
		
			var amount = $("#coinAmount").val();
			
			if(isValid()) {
				$.ajax({
					type : "POST",
					url : "/api/coins/charge/" + amount,
					contentType : "application/json; charset=UTF-8",
					headers: {
						"Authorization" : "Bearer " + ACCESS_TOKEN
					},
					success : function(res) {
						if(res.code == 1) {
							alert(res.message);
						}
						$("#coinAmount").val(1000);
					},
					error : function(res) {
						console.log(res);
					
						alert(res.responseJSON.message);
					}
				});	
			}
			
		} else {
			alert("로그인을 해주세요.");
			return;
		}
	});
	
	/* 8-3. 유효성 검사 */
	function isValid() {
		
		var amount = $("#coinAmount").val();
			
		if(!amount.trim()) {
			alert("충전하실 코인 수량을 입력해주세요.");
			$("#coin_form #coinAmount").focus();
			return false;
		} else if(amount < 1000) {
			alert("충전하실려면 1000원이상 하셔야 합니다.");
			$("#coin_form #coinAmount").val(1000);
			$("#coin_form #coinAmount").focus();
			return false;
		}
		
		return true;
	}
	

	/**
	 *  8-4. 입력값 리셋
	 */
	$("#coinResetBtn").on("click", function(){
		console.log("coinResetBtn");
		
		location.reload();
		
	});
	
	/**
	 *  8-5. 코인 지출 내역
	 */
	$("#coinPayListBtn").on("click", function(){
		console.log("coinPayListBtn");
		
		if(ACCESS_TOKEN != null) {
			$.ajax({
				type : "GET",
				url : "/api/coins/pay",
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
										<th colspan="4">historyNo</th>
										<th colspan="3">userNo</th>
										<th colspan="4">itemId</th>
										<th colspan="3">amount</th>
										<th colspan="6">등록일</th>
										<th colspan="6">수정일</th>
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
						html = '<td colspan="53">등록된 게시글이 없습니다.</td>';
					} else {
						for(var i = 0; i < res.data.length; i++) {
							html += `
								<tr>
									<td colspan="2">${res.data.length - i}</td>
									<td colspan="4">${res.data[i].historyNo}</td>
									<td colspan="3">${res.data[i].userNo}</td>
									<td colspan="4">${res.data[i].itemId}</td>
									<td colspan="3">${res.data[i].amount}</td>
									<td colspan="6">${res.data[i].createdDate}</td>
									<td colspan="6">${res.data[i].modifiedDate == null ? '' : res.data[i].modifiedDate}</td>
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
});