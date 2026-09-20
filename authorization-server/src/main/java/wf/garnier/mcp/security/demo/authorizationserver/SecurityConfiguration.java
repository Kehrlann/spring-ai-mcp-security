package wf.garnier.mcp.security.demo.authorizationserver;

import java.util.List;
import java.util.function.Consumer;

import org.springaicommunity.mcp.security.authorizationserver.config.LocalhostWildcardPortValidator;
import org.springaicommunity.mcp.security.authorizationserver.config.McpAuthorizationServerConfigurer;
import org.springaicommunity.mcp.security.common.url.DefaultUrlValidator;
import wf.garnier.mcp.security.demo.authorizationserver.user.DemoUser;
import wf.garnier.mcp.security.demo.authorizationserver.user.DemoUserDetailsService;

import org.springframework.boot.security.oauth2.server.authorization.autoconfigure.servlet.OAuth2AuthorizationServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.DelegatingRegisteredClientRepository;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.client.metadata.ClientIdMetadataDocumentRegisteredClientRepository;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.client.metadata.DefaultClientMetadataValidator;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientRegistrationAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.web.cors.CorsConfiguration;
import static org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationValidator.DEFAULT_SCOPE_VALIDATOR;
import static org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientRegistrationAuthenticationValidator.DEFAULT_JWK_SET_URI_VALIDATOR;
import static org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientRegistrationAuthenticationValidator.DEFAULT_REDIRECT_URI_VALIDATOR;

@Configuration
class SecurityConfiguration {

	private final Consumer<OAuth2ClientRegistrationAuthenticationContext> ALL_SCOPES_ALLOWED_VALIDATOR = DEFAULT_REDIRECT_URI_VALIDATOR
		.andThen(DEFAULT_JWK_SET_URI_VALIDATOR);

	@Bean
	Customizer<HttpSecurity> httpSecurityCustomizer() {
		return http -> {
			http.cors(cors -> cors.configurationSource(_ -> configurationSource()));
			http.csrf(CsrfConfigurer::disable);
		};
	}

	@Bean
	Customizer<McpAuthorizationServerConfigurer> mcpCustomizer() {
		return mcpAuthServer -> {
			mcpAuthServer.authorizationServer(authServer -> authServer.oidc(Customizer.withDefaults()));
			// Claude Code uses `localhost` for the domain of its redirect_uris
			mcpAuthServer.authorizationCodeRequestValidator(
					new LocalhostWildcardPortValidator().andThen(DEFAULT_SCOPE_VALIDATOR));

			// DCR
			mcpAuthServer.dynamicClientRegistration(true);
			mcpAuthServer.dynamicClientRegistrationValidator(ALL_SCOPES_ALLOWED_VALIDATOR);

			// CIMD
			mcpAuthServer.cimd(true);
		};
	}

	@Bean
	RegisteredClientRepository dcrRegisteredClientRepository(OAuth2AuthorizationServerProperties properties) {
		var clients = new OAuth2AuthorizationServerPropertiesMapper(properties).asRegisteredClients();
		var cimdRepo = new ClientIdMetadataDocumentRegisteredClientRepository();
		var urlValidator = new DefaultUrlValidator(true);
		cimdRepo.setClientIdUrlValidator(urlValidator);
		cimdRepo.setMetadataValidator(new DefaultClientMetadataValidator(urlValidator));
		return new DelegatingRegisteredClientRepository(
				List.of(cimdRepo),
				new InMemoryRegisteredClientRepository(clients));
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
			ctx.getClaims().claim(StandardClaimNames.EMAIL, user.getUserEmail());
			if (ctx.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
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
