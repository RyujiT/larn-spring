package jp.gr.java_conf.javapokul.larn_spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

	/** ロガー. */
	private final Logger logger = LoggerFactory.getLogger(RootController.class);

	@RequestMapping("/")
	public String index() {
		logger.debug("ルートが呼び出されたよ");
		return "起動中だよ";
	}
}
