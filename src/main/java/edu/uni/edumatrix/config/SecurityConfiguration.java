package edu.uni.edumatrix.config;

import edu.uni.edumatrix.config.jwt.JWTAuthEntryHandler;
import edu.uni.edumatrix.config.jwt.JWTSecurityFilter;
import edu.uni.edumatrix.service.UserService;
import edu.uni.edumatrix.util.commons.RequestDataProvider;
import edu.uni.edumatrix.util.exceptions.http.AccessDeniedExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.List;
import java.util.stream.Stream;

import static edu.uni.edumatrix.config.jwt.JWTSecurityFilter.EXACT_MATCH_ENDPOINTS;
import static edu.uni.edumatrix.config.jwt.JWTSecurityFilter.PREFIX_MATCH_ENDPOINTS;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfiguration {
    private final UserService userService;
    private final GlobalConfig globalConfig;
    private final RequestDataProvider requestDataProvider;

    public SecurityConfiguration(UserService userService, GlobalConfig globalConfig, RequestDataProvider requestDataProvider) {
        this.userService = userService;
        this.globalConfig = globalConfig;
        this.requestDataProvider = requestDataProvider;
    }

    List<String> allPublicMatchers = Stream.concat(
            EXACT_MATCH_ENDPOINTS.stream(),
            PREFIX_MATCH_ENDPOINTS.stream().map(p -> p + "**")
    ).toList();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authenticationManager) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new JWTAuthEntryHandler())
                        .accessDeniedHandler(new AccessDeniedExceptionHandler())
                )
                .authorizeHttpRequests(req -> req
                        .requestMatchers(allPublicMatchers.toArray(String[]::new)).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JWTSecurityFilter(userService,globalConfig, requestDataProvider, authenticationManager),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
