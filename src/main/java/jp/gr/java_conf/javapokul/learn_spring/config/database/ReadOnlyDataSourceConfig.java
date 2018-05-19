package jp.gr.java_conf.javapokul.learn_spring.config.database;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * 読み込み専用データソースの設定クラス.
 * @author rtaba
 */
@Configuration
public class ReadOnlyDataSourceConfig {

	/** データソースプロパティ名. */
	private final static String PROPERTIES_BEAN_NAME = "readOnlyDataSourceProperties";

	/** プロパティーの接頭語. */
	private final static String PROPERTIES_PRIFIX = "read-only.datasource";

	/**
	 * データソースのプロパティをDIコンテナに登録する.
	 * @return データソースのプロパティ
	 */
	@Bean(PROPERTIES_BEAN_NAME)
	@ConfigurationProperties(prefix=PROPERTIES_PRIFIX)
	public DataSourceProperties datasourceProperties() {
		return new DataSourceProperties();
	}

	/**
	 * データソースをDIコンテナに登録する.
	 * @param properties データソースのプロパティ
	 * @return データソース
	 */
	@Bean(DataSourceName.READ_ONLY_DATASOURCE)
	public DataSource dataSource(@Qualifier(PROPERTIES_BEAN_NAME) DataSourceProperties properties) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(properties.getUrl());
		dataSource.setUsername(properties.getUsername());
		dataSource.setPassword(properties.getPassword());
		dataSource.setDriverClassName(properties.getDriverClassName());
		dataSource.setConnectionProperties(null);
		return dataSource;
	}
}
