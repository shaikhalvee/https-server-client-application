package com.shaikhalvee.httpexampleserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goodbye")
public class HttpServerController {

	@GetMapping("/{name}")
	public String goodBye(@PathVariable String name) {
		return "Goodbye " + name + "!!!";
	}
}
