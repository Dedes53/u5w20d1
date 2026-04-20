package federicolepore.u5w19d4.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        // bisogna disabilitare le sessioni in quanto JWT è stateless, e quindi non le utilizza
        httpSecurity.sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.formLogin(formLogin -> formLogin.disable());
        httpSecurity.csrf(csrf -> csrf.disable()); // disabilitiamo questa protezione perché non ci servirà
        httpSecurity.authorizeHttpRequests(req -> req.requestMatchers("/**").permitAll()); // diciamo di non intervenire nell'autenticazione delle nostre richieste, in quanto verranno implementate in modo custom

        return httpSecurity.build();
    }
}
