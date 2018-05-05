package jp.gr.java_conf.javapokul.larn_spring.interceptor.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 動的ルーティングデータソースリゾルバー.<br>
 * 名前解決を行い、動的にデータソースをルーティングする.
 * @author rtaba
 */
public class DynamicRoutingDatasourceResolver extends AbstractRoutingDataSource {

	/** 	ロガー. */
	private final Logger logger = LoggerFactory.getLogger(DynamicRoutingDatasourceResolver.class);

	/** 名前解決キー（データソース名）. */
	private static ThreadLocal<String> lookupKey = new ThreadLocal<>();

	/**
	 * 定義された名前解決キー（データソース名）をスイッチする.
	 * @param dataSourceName データソース名
	 */
	public void switchLookupKey(String dataSourceName) {
		lookupKey.remove();
		lookupKey.set(dataSourceName);
	}

	/**
	 * 定義された名前解決キー（データソース名）を取得する.
	 * @return データソース名
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		String lookupkey = lookupKey.get();
		logger.debug("データソースキー={}", lookupkey);
		return lookupkey;
	}
}
