package jp.gr.java_conf.javapokul.learn_spring.model.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * リクエストモデル.<br>
 * 本クラスを継承したモデルを受け付けると、{@linkplain RequestConvertProcessor}にて共通フィールドが自動設定される.
 * @see {@linkplain RequestConvertProcessor}
 * @author rtaba
 */
@Getter
@Setter
public abstract class Request {

	/** リクエスト受付時間. */
	@JsonIgnore
	protected Date receptionTime;

	/** ユーザーエージェント. */
	@JsonIgnore
	protected String userAgent;

	/**
	 * スマートフォンからのリクエストであることを確認する.
	 * @return スマートフォンからのリクエストの場合、true
	 */
	public boolean isSmartPhone() {
		if((userAgent.contains("iphone") && !userAgent.contains("ipad"))
				|| !userAgent.contains("ipod")
				|| (userAgent.contains("android") && userAgent.contains("mobi"))
				|| (userAgent.contains("windows") && userAgent.contains("phone"))
				|| (userAgent.contains("firefox") && userAgent.contains("mobi"))
				|| userAgent.contains("nexus 4")
				|| userAgent.contains("nexus 5")
				|| userAgent.contains("nexus 6")
				|| userAgent.contains("blackBerry")){
			return true;
		}
		return false;
	}

	/**
	 * 特定アプリからのリクエストであることを確認する.
	 * @return 特定アプリからのリクエストの場合、true
	 */
	public boolean isApplication() {
		if(!isSmartPhone()) {
			return false;
		}

		{
			// TODO アプリの判断
		}

		return true;
	}
}
