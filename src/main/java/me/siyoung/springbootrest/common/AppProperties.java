package me.siyoung.springbootrest.common;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "my-app")
@Getter
@Setter
public class AppProperties {

    private String adminUsername;

    private String adminPassword;

    private String userUsername;

    private String userPassword;

    private String clinetId;

    private String clientSecret;
}
