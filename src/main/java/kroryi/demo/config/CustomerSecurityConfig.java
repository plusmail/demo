package kroryi.demo.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import kroryi.demo.Service.CustomerUserDetailsService;
import kroryi.demo.Service.OAuth2UserService;
import kroryi.demo.security.APILoginSuccessHandler;
import kroryi.demo.security.CustomErrorHandlerConfig;
import kroryi.demo.security.filter.APILoginFilter;
import kroryi.demo.security.filter.RefreshTokenFilter;
import kroryi.demo.security.filter.TokenCheckFilter;
import kroryi.demo.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomerSecurityConfig {

    private final DataSource dataSource;
    private final CustomerUserDetailsService userDetailsService;
    private final OAuth2UserService oAuth2UserService;
    private final JWTUtil jwtUtil;

    @Autowired
    private CustomErrorHandlerConfig.HandlerExceptionResolver customErrorHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("----------보안 환경 설정");
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        AuthenticationManager authenticationManager =
                authenticationManagerBuilder.build();

        http.authenticationManager(authenticationManager);

        APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        APILoginSuccessHandler successHandler = new APILoginSuccessHandler(jwtUtil);
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);


        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(
                tokenCheckFilter(jwtUtil)
                , UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(
                new RefreshTokenFilter("/refreshToken", jwtUtil)
                , TokenCheckFilter.class
        );

        http
                .csrf(csrf -> csrf.disable())
                .rememberMe(me -> me
                        .key("12345678")
                        .tokenRepository(persistentTokenRepository())
                        .userDetailsService(userDetailsService)
                        .tokenValiditySeconds(60 * 60 * 24 * 30)
                )
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/error","/generateToken","/file/**", "/member/login", "/member/join", "/swagger-ui.html", "/api-docs/**", "/api/**", "/swagger-ui/**").permitAll() //permitAll 모든 접근 허요
                                .anyRequest().authenticated() // 모든 사이트 다 막고 시작

//                                .anyRequest().permitAll() // 모든 사이트 다 막고 시작
                ) // 모든 요청에 대한 인증 필요
                .formLogin(form -> form
                        .loginPage("/member/login") // 로그인 페이지로 이동
                        .defaultSuccessUrl("/board/list", true) // 이것을 생략하면 http://localhost:8080/
                        .permitAll()  // 모든 사용자에 로그인 페이지 접근 허용
                )
                .oauth2Login(login -> login
                        .loginPage("/member/login")
                        .successHandler(successHandler())
                        .userInfoEndpoint()
                        .userService(oAuth2UserService)

                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler(accessDeniedHandler()))
                .logout(LogoutConfigurer::permitAll
                );
        // 6.1 바뀐 부분 기존에는 http.formLogin()
        return http.build();
    }

//    @Bean
//    public AccessDeniedHandler accessDeniedHandler(){
//        return new Custom403Handler();
//    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            ResponseEntity<Map<String, String>> responseEntity =
                    customErrorHandler.handle403(accessDeniedException);
            response.setStatus(responseEntity.getStatusCodeValue());
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper()
                    .writeValueAsString(responseEntity.getBody()));
        };
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

            String id = defaultOAuth2User.getAttributes().get("id").toString();
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            response.sendRedirect("/");

//            PrintWriter writer = response.getWriter();
//            writer.println(body);
//            writer.flush();
        });
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

    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil){
        return new TokenCheckFilter(jwtUtil);
    }

}
