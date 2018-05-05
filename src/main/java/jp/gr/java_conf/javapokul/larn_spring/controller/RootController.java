package jp.gr.java_conf.javapokul.larn_spring.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.gr.java_conf.javapokul.larn_spring.entity.MApiKey;
import jp.gr.java_conf.javapokul.larn_spring.repository.MApiKeyRepository;

/*
 * FIXME あとで削除!
 */
@RestController
public class RootController {

	/** ロガー. */
	private final Logger logger = LoggerFactory.getLogger(RootController.class);

	@Autowired
	private MApiKeyRepository repository;

	@RequestMapping("/")
	public String index() {
		logger.debug("ルートが呼び出されたよ");
		return "起動中だよ";
	}

	@RequestMapping("/showAPIKeys")
	public List<MApiKey> showAPIKeys() {
		logger.debug("[S]call RootController#showAPIKeys()");

		List<MApiKey> results = repository.findAll();
		
		logger.debug("[E]call RootController#showAPIKeys()");
		return results;
	}
}
