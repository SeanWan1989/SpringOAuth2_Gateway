package com.xunhui.gateway;

import com.xunhui.gateway.config.OAuth2ResourceConfig;
import com.xunhui.gateway.config.OAuth2ServerConfig;
import com.xunhui.gateway.config.WebSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@Import({ OAuth2ResourceConfig.class,OAuth2ServerConfig.class, WebSecurityConfig.class})
@EnableZuulProxy
public class ApiGatewayApplication extends WebMvcConfigurerAdapter{

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
}
