package jp.gr.java_conf.javapokul.learn_spring.controller;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.gr.java_conf.javapokul.learn_spring.entity.MApiKey;
import jp.gr.java_conf.javapokul.learn_spring.repository.MApiKeyRepository;

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

	@RequestMapping("/updateAPIKey")
	public MApiKey updateAPIKey(String key) {
		logger.debug("[S]call RootController#updateAPIKey(String)");

		if(!StringUtils.hasText(key)) {
			throw new IllegalArgumentException("更新対象のキーを指定してください。");
		}

		MApiKey target = null;
		try {
			target = repository.getOne(key);
		} catch(EntityNotFoundException e) {
			throw new IllegalArgumentException(MessageFormat.format(
					"更新対象のキーが見つかりません。[key={0}]", key), e);	
		}

		Calendar newExpirationDate = Calendar.getInstance();
		newExpirationDate.add(Calendar.YEAR, 1);
		target.setExpirationDate(newExpirationDate.getTime());

		repository.save(target);

		logger.debug("[E]call RootController#updateAPIKey(String)");
		return target;
	}
}
