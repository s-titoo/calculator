package com.titoo.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import com.hazelcast.client.config.ClientConfig;

@SpringBootApplication
@EnableCaching
public class CalculatorApplication {
	
	private static final String CONSTANT = "constant";
	public static void main(String[] args) {
		SpringApplication.run(CalculatorApplication.class, args);
	}
	@Bean
	public ClientConfig hazelcastClientConfig() {
		ClientConfig clientConfig = new ClientConfig();
		// Use address 'hazelcast' (Service name) for Kubernetes deployment
		// and 'localhost' for local deployment via Ansible
		clientConfig.getNetworkConfig().addAddress("localhost");
		return clientConfig;
	}
}
