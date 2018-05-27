package jp.gr.java_conf.javapokul.learn_spring.interceptor.webmvc;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.gr.java_conf.javapokul.learn_spring.entity.MApiKey;
import jp.gr.java_conf.javapokul.learn_spring.entity.MUser;
import jp.gr.java_conf.javapokul.learn_spring.model.request.Request;
import jp.gr.java_conf.javapokul.learn_spring.model.request.TokenRequest;
import jp.gr.java_conf.javapokul.learn_spring.repository.MApiKeyRepository;
import jp.gr.java_conf.javapokul.learn_spring.repository.MUserRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * HTTPリクエストパラメータを{@linkplain Request}モデルへ変換する.<br>
 * 下記の機能を内包している.
 * <ul>
 * <li>GETパラメータおよびPOSTパラメータ(JSONのみ)をパースし、{@linkplain Request}へ変換する</li>
 * <li>トークン(クエリ文字列)をユーザー名に変換して、{@linkplain Request}へ設定する</li>
 * <li>現在日時（リクエスト受付日時）を{@linkplain Request}へ設定する</li>
 * <li>HTTPヘッダーからUser-Agentを取得し、{@linkplain Request}へ設定する</li>
 * </ul>
 * @author rtaba
 */
@Component
@Slf4j
public class RequestConvertProcessor extends ModelAttributeMethodProcessor {

	/** APIキーマスタリポジトリ. */
	@Autowired
	private MApiKeyRepository apiKeyRepository;

	/** ユーザーマスタリポジトリ. */
	@Autowired
	private MUserRepository userRepository;

	/**
	 * コンストラクタ.<br>
	 * {@link ModelAttribute}の付与を強制しない.
	 * @see {@linkplain ModelAttributeMethodProcessor#ModelAttributeMethodProcessor(boolean)}
	 */
	public RequestConvertProcessor() {
		super(true);
	}

	/**
	 * 対象パラメータのバインドをサポートしていることを確認する.
	 * @param parameter メソッドパラメータ
	 * @return バインドをサポートしている場合、trueを返却する
	 * @see {@linkplain ModelAttributeMethodProcessor#supportsParameter(MethodParameter)}
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return Request.class.isAssignableFrom(parameter.getParameterType());
	}

	/**
	 * コントローラーへバインドするリクエストモデルを生成する.<br>
	 * リクエストモデルを生成し、読み取ったリクエストパラメータをバインドして返却する.）
	 * @param requestModelConstructor メソッドパラメータのコンストラクタ
	 * @param attributeName メソッドパラメータのパラメータ名
	 * @param binderFactory データバインダのファクトリ
	 * @param webRequest リクエスト
	 * @return バインドするパラメータ
	 * @throws UnsupportedOperationException サポートしていない不正なリクエストを受け付けた場合にスローする
	 * @see {@linkplain super#constructAttribute(Constructor, String, WebDataBinderFactory, NativeWebRequest)}
	 */
	@Override
	protected Object constructAttribute(
			Constructor<?> requestModelConstructor,
			String attributeName,
			WebDataBinderFactory binderFactory,
			NativeWebRequest webRequest) throws UnsupportedOperationException {

		if(StandardCharsets.UTF_8.name().equals(webRequest.getHeader(HttpHeaders.ACCEPT_CHARSET))) {
			throw new UnsupportedOperationException(MessageFormat.format(
					"不正なリクエストを受け付けました。[{0}]", getRequestHeaders(webRequest)));
		}

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		if(Objects.isNull(request)) {
			throw new UnsupportedOperationException(MessageFormat.format(
					"不正なリクエストを受け付けました。[{0}]", getRequestHeaders(webRequest)));
		}

		Request requestModel = null;
		String contentType = webRequest.getHeader(HttpHeaders.CONTENT_TYPE);
		// ------------------------------------------------------------------------------------------
		// POSTリクエスト（JSON）の変換
		// ------------------------------------------------------------------------------------------
		if(MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
			requestModel = constructAttributeFromJson(requestModelConstructor,  request);
		}
		// ------------------------------------------------------------------------------------------
		// GETリクエストの変換
		// ------------------------------------------------------------------------------------------
		else {
			requestModel = constructAttributeFromQueryString(requestModelConstructor,  request);
		}

		return requestModel;
	}

	/**
	 * リクエストパラメータをバインドする.
	 * @param binder データバインダー
	 * @param request リクエスト
	 * @see {@linkplain super#bindRequestParameters(WebDataBinder, NativeWebRequest)}
	 */
	@Override
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
		binder.bind(new MutablePropertyValues(request.getParameterMap()));
	}

	/**
	 * GETリクエストをパースし、{@linkplain Request}モデルを生成する.
	 * @param requestModelConstructor {@linkplain Request}モデルのコンストラクタ
	 * @param request リクエスト
	 * @return {@linkplain Request}モデル
	 */
	private Request constructAttributeFromQueryString(Constructor<?> requestModelConstructor, HttpServletRequest request) {

		// --------------------------------------------
		// GETリクエストパラメータの読み込み
		// --------------------------------------------
		Map<String, String[]> paramMap = request.getParameterMap();

		// --------------------------------------------
		// GETリクエストパラメータのパース
		// --------------------------------------------
		Request requestModel = null;
		try {
			requestModel = (Request) requestModelConstructor.newInstance((Object[]) null);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn("リクエストの解析に失敗しました。", e);
			return null;
		}

		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(requestModel);
		for(Map.Entry<String, String[]> entry : paramMap.entrySet()) {
			if(!beanWrapper.isWritableProperty(entry.getKey())) {
				continue;
			}
			String[] values = entry.getValue();
			if(Objects.isNull(values) || 1 != values.length) {
				log.warn("サポートしていないリクエストパラメータの読み込みをスキップしました。[{}={}]", entry.getKey(), entry.getValue());
			}
			beanWrapper.setPropertyValue(entry.getKey(), entry.getValue()[0]);
		}

		// --------------------------------------------
		// リクエストモデルのセットアップ
		// --------------------------------------------
		return setupRequestModel(request, requestModel);
	}

	/**
	 * POSTリクエスト（JSON）をパースし、{@linkplain Request}モデルを生成する.
	 * @param requestModelConstructor {@linkplain Request}モデルのコンストラクタ
	 * @param request リクエスト
	 * @return {@linkplain Request}モデル
	 * @throws IOException
	 */
	private Request constructAttributeFromJson(Constructor<?> requestModelConstructor, HttpServletRequest request) {

		// --------------------------------------------
		// POSTリクエストパラメータの読み込み
		// --------------------------------------------
		String postAttribute = null;
		try {
			ServletInputStream inputStream = request.getInputStream();
			postAttribute = new String(inputStream.readAllBytes());
		} catch (IOException e) {
			log.warn("POSTリクエストの解析に失敗しました。", e);
			return null;
		}

		// --------------------------------------------
		// POSTリクエストパラメータ(JSON)のパース
		// --------------------------------------------
		Request requestModel = null;
		try {
			requestModel = (Request) new ObjectMapper().readValue(postAttribute, requestModelConstructor.getDeclaringClass());
		} catch (IOException e) {
			log.warn("リクエストの解析に失敗しました。", e);
			return null;
		}

		// --------------------------------------------
		// リクエストモデルのセットアップ
		// --------------------------------------------
		return setupRequestModel(request, requestModel);
	}

	/**
	 * {@linkplain Request}モデルをセットアップする.
	 * @param request HTTPリクエスト
	 * @param requestModel {@linkplain Request}モデル
	 * @return {@linkplain Request}モデル
	 */
	private Request setupRequestModel(HttpServletRequest request, Request requestModel) {

		// リクエストトークンを受け付ける場合
		if(TokenRequest.class.isInstance(requestModel)) {
			String userName = null;
			String token = request.getParameter("token");
			if(StringUtils.hasText(token)) {
				MApiKey apiKey = null;
				try {
					apiKey = apiKeyRepository.getOne(token);
				} catch(EntityNotFoundException e) {
					throw new SecurityException("不正なリクエストを受け付けました。", e);
				}

				// 有効期限のチェック
				Date expirationDate = apiKey.getExpirationDate();
				if(Objects.nonNull(expirationDate) && expirationDate.after(Calendar.getInstance().getTime())) {
					throw new SecurityException("指定されたトークンは有効期限が切れています。");	
				}

				List<MUser> users = userRepository.findByApiKey(apiKey.getApiKey());
				if(CollectionUtils.isEmpty(users)) {
					throw new SecurityException("不正なリクエストを受け付けました。");				
				}
				for(MUser item : users) {
					userName = item.getUserName();
					if(StringUtils.hasText(userName)) {
						break;
					}
				}
			}
			((TokenRequest) requestModel).setUserName(userName);
		}
		requestModel.setReceptionTime(Calendar.getInstance().getTime());
		requestModel.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		return requestModel;
	}

	/**
	 * リクエストヘッダーを文字列として取得する.<br>
	 * ※ログ出力用
	 * @param request リクエスト
	 * @return　リクエストヘッダーを文字列連携した値
	 */
	private String getRequestHeaders(NativeWebRequest request) {
		StringBuilder result = new StringBuilder("header:{");
		Iterator<String> headerNames = request.getHeaderNames();
		while(headerNames.hasNext()) {
			String name = headerNames.next();
			result.append("name:").append(request.getHeader(name));
			if(headerNames.hasNext()) {
				result.append(", ");
			}
		}
		return result.append("}").toString();
	}
}
