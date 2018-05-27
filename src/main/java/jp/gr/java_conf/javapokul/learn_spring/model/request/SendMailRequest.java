package jp.gr.java_conf.javapokul.learn_spring.model.request;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * メール送信リクエストモデル.
 * @author rtaba
 */
@Getter
@Setter
public class SendMailRequest extends TokenRequest {

	/** 宛先. */
	@NotNull(message="宛先を入力してください。")
	private String to;

	/** 件名. */
	private String subject;

	/** 本文. */
	private String text;
}
