package wf.garnier.mcp.security.demo.authorizationserver;

import java.util.List;

import wf.garnier.mcp.security.demo.authorizationserver.user.DemoUser;
import wf.garnier.mcp.security.demo.authorizationserver.user.DemoUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

	@Bean
	Customizer<HttpSecurity> httpSecurityCustomizer() {
		return http -> {
			http.cors(cors -> cors.configurationSource(_ -> configurationSource()));
		};
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new DemoUserDetailsService(new DemoUser("daniel", "pw", "daniel@example.com"),
				new DemoUser("alice", "pw", "alice@example.com"), new DemoUser("bob", "pw", "bob@example.com"));
	}

	@Bean
	OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
		return ctx -> {
			DemoUser user = (DemoUser) ctx.getPrincipal().getPrincipal();
			ctx.getClaims().subject(user.getUserEmail());
			if (ctx.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
				ctx.getClaims().claim(StandardClaimNames.EMAIL, user.getUserEmail());
				ctx.getClaims().claim(StandardClaimNames.EMAIL_VERIFIED, true);
				ctx.getClaims().claim(StandardClaimNames.NAME, user.getUsername());
			}
		};
	}

	// Demo only, don't do this in prod
	private CorsConfiguration configurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(List.of("*"));
		configuration.setAllowedMethods(List.of("*"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setExposedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		return configuration;

	}

}
