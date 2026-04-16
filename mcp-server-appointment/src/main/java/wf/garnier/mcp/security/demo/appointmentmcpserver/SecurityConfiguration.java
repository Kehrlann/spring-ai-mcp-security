package wf.garnier.mcp.security.demo.appointmentmcpserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Bean
    SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults())
                .cors(cors -> cors.configurationSource(new AllowAllCorsConfigurationSource()))
                .build();
    }
}
