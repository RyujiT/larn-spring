package jp.gr.java_conf.javapokul.learn_spring.config.webmvc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jp.gr.java_conf.javapokul.learn_spring.interceptor.webmvc.RequestConvertProcessor;

/**
 * WebMVC関連定義クラス.
 * @author rtaba
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	/** リクエストモデル変換プロセッサー. */
	@Autowired
	private RequestConvertProcessor convertor;

	/**
	 * メソッド引数の名前解決ハンドラーを追加する.
	 * @see {@linkplain super#addArgumentResolvers(List)}
	 */
	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(convertor);
    }
}
