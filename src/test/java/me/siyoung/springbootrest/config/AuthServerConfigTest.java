package me.siyoung.springbootrest.config;

import me.siyoung.springbootrest.accounts.Account;
import me.siyoung.springbootrest.accounts.AccountRole;
import me.siyoung.springbootrest.accounts.AccountService;
import me.siyoung.springbootrest.common.BaseControllerTest;
import me.siyoung.springbootrest.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        //given

        String username = "tldud2404@gmail.com";
        String password = "tldud";
        Account tldud = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(tldud);

        String clientId = "myApp";
        String clinetPw = "pass";

        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId,clinetPw))
                .param("username",username)
                .param("password",password)
                .param("grant_type","password")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }



}