package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

public class AllowAllCorsConfigurationSource implements CorsConfigurationSource {

	private final CorsConfiguration config;

	public AllowAllCorsConfigurationSource() {
		config = new CorsConfiguration();
		config.setAllowedOriginPatterns(List.of("*"));
		config.setAllowedMethods(List.of("*"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);
	}

	@Override
	public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

		return config;
	}

}
