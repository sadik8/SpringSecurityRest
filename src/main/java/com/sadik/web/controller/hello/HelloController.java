package com.sadik.web.controller.hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HelloController {
	
	@RequestMapping("/hello")
	public String hello() {
		return "Hello Sadik";
	}
}
