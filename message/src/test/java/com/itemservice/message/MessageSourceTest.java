package com.itemservice.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@SpringBootTest
public class MessageSourceTest {

	@Autowired
	MessageSource messageSource;
	
	@Test
	void helloMessage() {
		String result = messageSource.getMessage("hello", null, null);
		assertThat(result).isEqualTo("안녕");
	}
	
	@Test
	void notFoundMessageCode() {
//		messageSource.getMessage("no_code", null, null);  // -> 이걸 실행하면 메시지가 없으니 예외가 발생.
		assertThatThrownBy(() -> messageSource.getMessage("no_code", null, null))
			.isInstanceOf(NoSuchMessageException.class);
	}
	
	@Test
	void notFoundMessageCodeDefaultMessage() {
		String result = messageSource.getMessage("no_code", null, "기본 메시지", null);
		assertThat(result).isEqualTo("기본 메시지");
	}
	
	@Test
	void argumentMessage() {
		String message = messageSource.getMessage("hello.name", new Object[]{"Spring"}, null);
		assertThat(message).isEqualTo("안녕 Spring");
	}
	
	@Test
	void defaultLang() {
		assertThat(messageSource.getMessage("hello", null, null)).isEqualTo("안녕");
		assertThat(messageSource.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
	}
	
	@Test
	void englishLang() {
		assertThat(messageSource.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
	}
	
	
}
