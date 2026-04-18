package wf.garnier.mcp.security.demo.appointmentmcpserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import static org.springaicommunity.mcp.security.server.config.McpServerOAuth2Configurer.mcpServerOAuth2;

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

	@Bean
	SecurityFilterChain mcpSecurityFilterChain(HttpSecurity http,
			@Value("${ISSUER_URI:http://localhost:9000}") String issuerUri) {
		return http.authorizeHttpRequests(authz -> {
			authz.anyRequest().authenticated();
		})
			.with(mcpServerOAuth2().authorizationServer(issuerUri))
			.cors(cors -> cors.configurationSource(new AllowAllCorsConfigurationSource()))
			.csrf(CsrfConfigurer::disable)
			.build();
	}

	@Bean
	@Order(1)
	SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
		return http.securityMatcher("/", "/api/**", "/oauth2/**", "/login/**", "/logout/**", "/**/*.css")
			.authorizeHttpRequests(authz -> {
				authz.anyRequest().authenticated();
			})
			.oauth2Login(Customizer.withDefaults())
			.cors(cors -> cors.configurationSource(new AllowAllCorsConfigurationSource()))
			.build();
	}

}
