package jp.gr.java_conf.javapokul.learn_spring.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * トークンリクエストモデル.<br>
 * リクエストにトークンを含む場合、本モデルを継承する必要がある.<br>
 * 本クラスを継承したモデルを受け付けると、{@linkplain RequestConvertProcessor}にて
 * トークンチュエックおよびユーザー名へ変換処理が行われる.
 * @see {@linkplain RequestConvertProcessor}
 * @author rtaba
 */
@Getter
@Setter
public abstract class TokenRequest extends Request {
	/** ユーザー名. */
	@JsonIgnore
	protected String userName;
}
