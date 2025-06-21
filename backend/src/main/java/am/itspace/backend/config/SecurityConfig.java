package am.itspace.backend.config;

import am.itspace.backend.filter.JwtAuthenticationTokenFilter;
import am.itspace.backend.security.JwtAuthenticationEntryPoint;
import am.itspace.backend.service.impl.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailsService userDetailsService;
  private final JwtAuthenticationEntryPoint authenticationEntryPoint;
  private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
  private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(authenticationEntryPoint)
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(auth -> auth
            // Uncomment if you want to allow Swagger UI
            // .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
            // .requestMatchers("/v3/api-docs/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/user/login", "/user/register").permitAll()
            .requestMatchers(HttpMethod.GET, "/product/all", "/product/{id}").permitAll()
            .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/cart/add").hasAuthority("USER")
            .requestMatchers(HttpMethod.DELETE, "/cart/remove/**").hasAuthority("USER")
            .requestMatchers(HttpMethod.GET, "/cart/me").hasAuthority("USER")
            .requestMatchers(HttpMethod.POST, "/product/create").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/product/update/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/product/delete/**").hasAuthority("ADMIN").anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .logout()
        .logoutUrl("/user/logout")
        .addLogoutHandler(logoutHandler)
        .logoutSuccessHandler((request, response, authentication) ->
            SecurityContextHolder.clearContext());
    return httpSecurity.build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}

