package kroryi.demo.config;


import kroryi.demo.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomerSecurityConfig {

    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("----------보안 환경 설정");
        // Spring boot 3부터 람다식으로 사용해야 함.
        http
                // .csrf().disable() 2버전 방식
                // .csrf( c -> c.disable()) 3 버전 방식
                .csrf(csrf -> csrf.disable())
                .rememberMe(me->me
                        .key("12345678")
                        .tokenRepository(persistentTokenRepository())
                        .userDetailsService(userDetailsService)
                        .tokenValiditySeconds(60*60*24*30)
                )
                .authorizeHttpRequests(
                        authorize -> authorize.anyRequest().authenticated()
                ) // 모든 요청에 대한 인증 필요
                .formLogin(form -> form
                        .loginPage("/member/login") // 로그인 페이지로 이동
                        .permitAll()  // 모든 사용자에 로그인 페이지 접근 허용
                )
                .logout(LogoutConfigurer::permitAll
                );
        // 6.1 바뀐 부분 기존에는 http.formLogin()
        return http.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {

        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;

    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("----------웹 환경 설정 -----------");
        // resources/static 경로를 일반 보안이 설정에서 제외 시키는 것
        return (web) -> web
                .ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }


}
