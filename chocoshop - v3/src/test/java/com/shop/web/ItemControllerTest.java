package com.shop.web;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc  // 1-1. MockMvc 테스트를 위해 @AutoConfigureMockMvc 어노테이션 추가
@Transactional
class ItemControllerTest {
	
	@Autowired
	private MockMvc mockMvc;  // 1-2. MockMvc 클래스를 이용하면 실제 객체와 비슷하지만 테스트에 필요한 기능만 가지는 가짜 객체를 만들 수 있습니다. 또 MockMvc 객체를 이용하면 웹 브라우저에서 요청하는 것처럼 테스트도 가능합니다.

	@Test
	@DisplayName("상품 등록 페이지 ADMIN 권한 테스트")
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void itemFormTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
			   .andDo(print())
			   .andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("상품 등록 페이지 USER 권한 테스트")
	@WithMockUser(username = "user", roles = "USER")
	public void itemFormNotAdminTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
			   .andDo(print())
			   .andExpect(status().isForbidden());
	}

}
