package util;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import service.AppBean;

@Configuration
public class BeansConfig {
	
	@Inject
	@Bean
	public AppBean init(String hello) {
		return new AppBean(hello);
	}
	
}
