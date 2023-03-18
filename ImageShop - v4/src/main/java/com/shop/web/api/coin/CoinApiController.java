package com.shop.web.api.coin;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.config.auth.PrincipalDetails;
import com.shop.domain.coin.ChargeCoin;
import com.shop.domain.coin.PayCoin;
import com.shop.domain.member.Member;
import com.shop.service.coin.CoinService;
import com.shop.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CoinApiController {
	
	private final CoinService coinService;
	private final MessageSource messageSource;

	@PostMapping("/coins/charge/{amount}")
	public ResponseEntity<?> register(@PathVariable("amount") Integer amount, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
		log.info("코인 충전 API");
		
		coinService.chargeCoin(amount, principalDetails.getMember());
		
		String message = messageSource.getMessage("coin.chargingComplete", null, Locale.KOREAN);
		
		return new ResponseEntity<>(new CMRespDto<>(1, message, null), HttpStatus.OK);
	}
	
	// 2023-03-04 -> 여기까지 완료
	@GetMapping("/coins/charge")
	public ResponseEntity<?> chargeList(@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
		log.info("코인 충전 list API");
		
		Member loginUser = principalDetails.getMember();	
		List<ChargeCoin> result = coinService.findChargeCoinByUserNo(loginUser.getUserNo());
		
		return new ResponseEntity<>(new CMRespDto<>(1, "코인 충전 정보 리스트 불러오기 성공", result), HttpStatus.OK);
	}
	
	@GetMapping("/coins/pay")
	public ResponseEntity<?> payList(@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
		log.info("코인 지불 list API");
		
		Member loginUser = principalDetails.getMember();	
		List<PayCoin> result = coinService.findPayCoinByUserNo(loginUser.getUserNo());
		
		return new ResponseEntity<>(new CMRespDto<>(1, "코인 사용 리스트 불러오기 성공", result), HttpStatus.OK);
	}
}

/*
 *  -2023-03-05-
 *  https://rudalson.tistory.com/entry/git-push%EC%97%90%EC%84%9C-Permission-denied-%ED%95%B4%EA%B2%B0
 *   -> 깃허브 실습중
 */
