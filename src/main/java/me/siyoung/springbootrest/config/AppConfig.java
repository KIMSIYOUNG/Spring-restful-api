package me.siyoung.springbootrest.config;

import me.siyoung.springbootrest.accounts.Account;
import me.siyoung.springbootrest.accounts.AccountRole;
import me.siyoung.springbootrest.accounts.AccountService;
import me.siyoung.springbootrest.common.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.PasswordAuthentication;
import java.util.Set;

@Configuration
public class AppConfig {

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    public ApplicationRunner applicationRunner(){
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account keesun = Account.builder()
                        .email(appProperties.getAdminUsername())
                        .password(appProperties.getAdminPassword())
                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();
                accountService.saveAccount(keesun);
            }
        };
    }
}
