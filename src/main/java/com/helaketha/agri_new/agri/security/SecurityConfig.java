package com.helaketha.agri_new.agri.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthenticationConverter
    ) throws Exception {

        http
                // Disable CSRF (REST API)
                .csrf(AbstractHttpConfigurer::disable)

                // Enable CORS
                .cors(cors -> {})

                // Stateless API
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Authorization rules (ORDER MATTERS)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // SECURED ENDPOINTS - Require OAuth2 authentication
                        .requestMatchers("/api/farmers/**").permitAll()
                        .requestMatchers("/api/harvester-drivers/**").permitAll()
                        .requestMatchers("/api/tractor-drivers/**").permitAll()
                        .requestMatchers("/api/fertilizer-suppliers/**").permitAll()
                        .requestMatchers("/api/services/**").permitAll()
                        .requestMatchers("/api/provider-schedules/**").permitAll()

                        // SECURED ENDPOINTS
                        .requestMatchers("/api/**").authenticated()

                        // EVERYTHING ELSE
                        .anyRequest().permitAll()
                )

                // OAuth2 Resource Server (JWT) - Keycloak
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(principalConverter(jwtAuthenticationConverter))
                        )
                );

        return http.build();
    }

    /**
     * Convert Keycloak roles → Spring Security authorities
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        // Keycloak role location
        authoritiesConverter.setAuthoritiesClaimName("realm_access.roles");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }

    private Converter<Jwt, AbstractAuthenticationToken> principalConverter(JwtAuthenticationConverter delegate) {
        return new Converter<>() {
            @Override
            public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
                AbstractAuthenticationToken delegateToken = delegate.convert(jwt);
                if (delegateToken == null) {
                    return null;
                }
                String username = jwt.getClaimAsString("preferred_username");
                if (username == null) {
                    username = jwt.getSubject();
                }

                // CHANGE HERE: Extract "sub" instead of "sid"
                String keycloakUserId = jwt.getSubject(); // .getSubject() returns the 'sub' claim

                UserPrincipal principal = new UserPrincipal(username, keycloakUserId, delegateToken.getAuthorities());
                Collection<? extends GrantedAuthority> authorities = delegateToken.getAuthorities();
                return new KeycloakAuthenticationToken(jwt, authorities, principal);
            }
        };
    }

    /**
     * CORS configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}