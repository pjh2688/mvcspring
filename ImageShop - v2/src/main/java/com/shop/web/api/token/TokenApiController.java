package com.shop.web.api.token;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenApiController {

	@PostMapping("/api/token")
	public ResponseEntity<?> token() {
		
		String result = "성공";
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
