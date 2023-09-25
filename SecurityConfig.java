package vps9d5e18f7.vps.ovh.net.studi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails adminUser = User.withUsername("admin")
            .passwordEncoder(password -> passwordEncoder().encode("adminpassword"))
            .password("adminpassword")
            .roles("ADMIN")
            .build();

        UserDetails user = User.withUsername("user")
            .passwordEncoder(password -> passwordEncoder().encode("userpassword"))
            .password("userpassword")
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(adminUser, user);
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
        	.cors() // Activer la configuration CORS
        	.and()
            .authorizeRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
                    .requestMatchers(new AntPathRequestMatcher("/api/products")).permitAll() // Autorisez l'accès à l'API sans authentification
                    .anyRequest().permitAll()
            )
            .formLogin() // Supprimez .loginPage("/login")
            .and()
            .logout() // Configure logout behavior here
            .logoutUrl("/logout") // Spécifiez l'URL de déconnexion personnalisée si nécessaire
            .logoutSuccessUrl("/") // L'URL où vous souhaitez rediriger après la déconnexion
            .permitAll(); // Autoriser l'accès à la page de déconnexion
        
        http.csrf().disable(); // Désactivez la protection CSRF
        return http.build();
    }
}