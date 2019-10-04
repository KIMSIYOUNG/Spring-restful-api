package me.siyoung.springbootrest.config;

import me.siyoung.springbootrest.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

//이러면 스프링부트가 자동으로 Autoconfigure 해주는 스프링시큐리티 설정이 사라짐
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountService accountService;
    @Autowired
    PasswordEncoder passwordEncoder;
    //토큰저장소 생성
    @Bean
    public TokenStore tokenStore(){
        return new InMemoryTokenStore();
    }

    //빈으로 등록해야한대
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //UserDetailService와 PasswordEncoder는 우리꺼로
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }




    //시큐리티를 적용하지 않고자 하는 곳(닥스, 정적리소스들)
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().mvcMatchers("/docs/index.html");
//        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
////        web.ignoring().mvcMatchers("/api/**");
//    }

//    (필터를 탄 다음에, 검사하는거고 위에는 필터에 적용대상에서 제외되니까)
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .anonymous()
//                    .and()
//                .formLogin()
//                    .and()
//                .authorizeRequests()
//                    .mvcMatchers(HttpMethod.GET,"/api/**").authenticated()
//                    .anyRequest().authenticated();
//    }
}
