package jp.gr.java_conf.javapokul.larn_spring.interceptor.database;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jp.gr.java_conf.javapokul.larn_spring.config.database.DataSourceName;

/**
 * 動的データソースルーティングインターセプター.<br>
 * 更新処理が行われる直前で書き込み可能データソースへ切り替える.<br>
 * 注意）Repositoryを利用する場合を想定している
 * @author rtaba
 */
@Component
@EnableAspectJAutoProxy
@Aspect
@Order(Integer.MAX_VALUE-1)
public class DynamicRoutingDataSourceInterceptor {

	/** 書き込み処理の検知パターン. */
	private static final String PATTERN_DETECTION_WRITING = 
			"@annotation(org.springframework.transaction.annotation.Transactional)"
			+ " || execution(public * org.springframework.data.repository.CrudRepository+.save*(..))"
			+ " || execution(public * org.springframework.data.repository.CrudRepository+.delete*(..))";

	/** 動的ルーティングデータソースリゾルバー. */
	@Autowired
	private DynamicRoutingDatasourceResolver datasourceResolver;

	/**
	 * データソースを書き込み可能へ切り替える.
	 */
	@Before(PATTERN_DETECTION_WRITING)
	public void switchDataSource() {
		datasourceResolver.switchLookupKey(DataSourceName.READ_WRITE_DATASOURCE);
	}

	/**
	 * データソースを読み込み専用へ再度切り替える.
	 */
	@After(PATTERN_DETECTION_WRITING)
	public void reSwitchDataSource() {
		datasourceResolver.switchLookupKey(DataSourceName.READ_ONLY_DATASOURCE);
	}
}
