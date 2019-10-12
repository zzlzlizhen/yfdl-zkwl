package com.remote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class ZkwlWxapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZkwlWxapiApplication.class, args);
	}

}
