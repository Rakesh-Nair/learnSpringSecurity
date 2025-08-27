package com.security.learnSpringSecurity.config;

import com.security.learnSpringSecurity.security.CustomAccessDeniedHandler;
import com.security.learnSpringSecurity.security.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("prod")
public class ProjectSecurityProdConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession").maximumSessions(1).maxSessionsPreventsLogin(true).expiredUrl("/expired"))
                .requiresChannel(rrc -> rrc.anyRequest().requiresSecure()).
                csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests((requests) -> requests.
                requestMatchers("/myAccount","/myBalance","/myCards","/myLoans").authenticated()
                .requestMatchers("/notices","/contact","/error","/register","/invalidSession","/expired").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(cae -> cae.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()).accessDeniedPage("/denied"));
        return http.build();
    }

/*    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource){
        //UserDetails user = User.withUsername("user").password("{noop}EazyBytes@12345").authorities("read").build();
        //UserDetails admin = User.withUsername("admin").password("{bcrypt}$2a$12$AsO2XYAnOAUCyKxNWwLhqei6xedU/bifptJH1EFXrv/icf9j5XeWy").authorities("write").build();
        return new JdbcUserDetailsManager(dataSource);
    }*/

    @Bean
    public PasswordEncoder passwordEncoder(){
        //return new BCryptPasswordEncoder();
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
