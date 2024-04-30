package com.study.auth.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.study.auth.security.service.LoginService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        // 특정 URL에 대한 권한 설정.
        http.authorizeHttpRequests((authorizeRequests) -> {
            authorizeRequests.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN"); // ROLE_은 붙이면 안 된다. hasAnyRole()을 사용할 때 자동으로 ROLE_이 붙기 때문이다.
            authorizeRequests.requestMatchers("/admin/**").hasRole("USER"); // ROLE_은 붙이면 안 된다. hasAnyRole()을 사용할 때 자동으로 ROLE_이 붙기 때문이다.
            authorizeRequests.requestMatchers("/").permitAll(); // "/" 경로는 모든 권한에서 접근 가능하도록 세팅
        });

        http.formLogin((formLogin) -> {
            formLogin.loginPage("/");
            formLogin.usernameParameter("userId");
            formLogin.successHandler(new SimpleUrlAuthenticationSuccessHandler("/admin"));
            formLogin.permitAll();
        });

        return http.build();
    }
}
