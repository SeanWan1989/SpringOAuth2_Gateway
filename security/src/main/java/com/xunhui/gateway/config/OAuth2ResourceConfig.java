package com.xunhui.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@EnableResourceServer
public class OAuth2ResourceConfig extends ResourceServerConfigurerAdapter {

	private TokenExtractor tokenExtractor = new BearerTokenExtractor();

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId("test").stateless(false);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request,
					HttpServletResponse response, FilterChain filterChain)
					throws ServletException, IOException {
				// We don't want to allow access to a resource with no token so clear
				// the security context in case it is actually an OAuth2Authentication
				if (tokenExtractor.extract(request) == null) {
					SecurityContextHolder.clearContext();
				}
				filterChain.doFilter(request, response);
			}
		}, AbstractPreAuthenticatedProcessingFilter.class);
		http.csrf().disable();
		http.requestMatchers().antMatchers("/test/**")
				.and()
				.authorizeRequests()
				.antMatchers("/test/test").access("#oauth2.hasScope('read')")
				.antMatchers("/test/adminresource").access("#oauth2.hasScope('read') and #oauth2.hasScope('write')")
				.antMatchers("/test/userresource").access("#oauth2.hasScope('read') and #oauth2.clientHasRole('ROLE_ADMIN')");
	}
}