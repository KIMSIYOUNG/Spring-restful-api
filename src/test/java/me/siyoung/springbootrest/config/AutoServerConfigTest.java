package me.siyoung.springbootrest.config;

import me.siyoung.springbootrest.accounts.AccountService;
import me.siyoung.springbootrest.common.BaseControllerTest;
import me.siyoung.springbootrest.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class AutoServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트 ")
    public void getAuthToken(){


    }

}