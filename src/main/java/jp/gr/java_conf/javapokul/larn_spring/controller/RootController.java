package jp.gr.java_conf.javapokul.larn_spring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

	@RequestMapping("/")
	public String index() {
		return "起動中だよ";
	}
}
