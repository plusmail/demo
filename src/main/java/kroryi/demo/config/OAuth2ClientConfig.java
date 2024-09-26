package kroryi.demo.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;

@Log4j2
@Configuration
public class OAuth2ClientConfig {


    @Bean
    public DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        OAuth2AuthorizationCodeGrantRequestEntityConverter requestEntityConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();

        // Customizing the request entity converter to use POST
        tokenResponseClient.setRequestEntityConverter(requestEntityConverter);

        return tokenResponseClient;
    }
}
