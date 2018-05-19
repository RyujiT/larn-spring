package jp.gr.java_conf.javapokul.learn_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * メール送信コントローラー.<br>
 * メールを送信する機能を提供する.<br>
 * 注意）バリデーションは未実装
 * @author rtaba
 */
@Slf4j
@RestController
public class MailController {

	/** メール送信モジュール. */
	@Autowired
	private MailSender sender;

	/**
	 * メールを送信する.
	 * @param to 宛先アドレス
	 * @param subject タイトル
	 * @param text 本文
	 * @return 送信情報
	 */
	@RequestMapping("/mail/send")
	public String sendMail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		
		StringBuilder result = new StringBuilder("[").append(message.toString()).append("]");
		try {
			sender.send(message);
			result.insert(0, "メールを送信しました。");
			log.info(result.toString());
		} catch(MailParseException e) {
			result.insert(0, "メールの送信に失敗しました。");
			log.info(result.toString());
		} catch(MailAuthenticationException e) {
			result.insert(0, "メール送信時に認証エラーが発生しました。");
			log.error(result.toString(), e);
		} catch(MailException e) {
			result.insert(0, "メール送信時に想定外のエラーが発生しました。");
			log.error(result.toString(), e);
		}

		return result.toString();
	}
}
