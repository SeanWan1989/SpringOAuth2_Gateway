package com.xunhui.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@EnableAuthorizationServer
@Configuration
public class OAuth2ServerConfig extends AuthorizationServerConfigurerAdapter {
	@Value("${security.datasource.url}")
	private  String dataSourceUrl;
	@Value("${security.datasource.username}")
	private  String dataSourceUsername;
	@Value("${security.datasource.password}")
	private  String dataSourcePassword;

	@Bean
	public DriverManagerDataSource dataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl(dataSourceUrl);
		dataSource.setUsername(dataSourceUsername);
		dataSource.setPassword(dataSourcePassword);
		return dataSource;
	}

	@Override
	public void configure(final ClientDetailsServiceConfigurer clients)
			throws Exception {
		clients.jdbc(dataSource()).build();
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer)
			throws Exception {
		oauthServer
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()");
	}



	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {
		JdbcTokenStore jdbcTokenStore = new JdbcTokenStore(dataSource());
		endpoints.tokenStore(jdbcTokenStore);
	}
}
