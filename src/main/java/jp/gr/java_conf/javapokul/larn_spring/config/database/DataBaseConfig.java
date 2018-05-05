package jp.gr.java_conf.javapokul.larn_spring.config.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jp.gr.java_conf.javapokul.larn_spring.interceptor.database.DynamicRoutingDatasourceResolver;

/**
 * データベース関連定義クラス.<br>
 * <ul>
 * <li>複数のデータソースを動的にルーティング機能</li>
 * <li>エンティティマネージャー</li>
 * <li>トランザクションマネージャー</li>
 * </ul>
 * @author rtaba
 */
@Configuration
@EnableTransactionManagement(order=Integer.MAX_VALUE)
@EnableJpaRepositories("jp.gr.java_conf.javapokul.larn_spring.repository")
public class DataBaseConfig {

	/** 読み込み専用データソース. */
	@Autowired
	@Qualifier(DataSourceName.READ_ONLY_DATASOURCE)
	private DataSource readOnlyDatasource;

	/** 書き込み可能データソース. */
	@Autowired
	@Qualifier(DataSourceName.READ_WRITE_DATASOURCE)
	private DataSource readWriteDatasource;

	/** エンティティを内包したパッケージ一覧. */
	@Value("#{'${spring.jpa.entity.packages}'.split(',')}")
	private List<String> entityPackages;

	/**
	 * JPAのプロパティをDIコンテナに登録する.
	 * @return JPAのプロパティ
	 */
	@Bean
	@ConfigurationProperties(prefix="spring.jpa.hibernate")
	public Properties jpaHibernateProperties() {
		return new Properties();
	}

	/**
	 * データソース（動的ルーティングデータソースリゾルバー）をDIコンテナに登録する.
	 * @return データソース（動的ルーティングデータソースリゾルバー）
	 */
	@Bean
	public DynamicRoutingDatasourceResolver dataSource() {   
		Map<Object, Object> datasources = new HashMap<>();
		datasources.put(DataSourceName.READ_ONLY_DATASOURCE, readOnlyDatasource);
		datasources.put(DataSourceName.READ_WRITE_DATASOURCE, readWriteDatasource);

		DynamicRoutingDatasourceResolver resolver = new DynamicRoutingDatasourceResolver();
		resolver.setTargetDataSources(datasources);
		resolver.setDefaultTargetDataSource(readOnlyDatasource);
		return resolver;
	}

	/**
	 * エンティティマネージャーファクトリーをDIコンテナに登録する.
	 * @param dataSource データソース（動的ルーティングデータソースリゾルバー）
	 * @return エンティティマネージャーファクトリー
	 */
	@Bean
	public EntityManagerFactory entityManagerFactory(DynamicRoutingDatasourceResolver dataSource) {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setPackagesToScan(entityPackages.toArray(new String[entityPackages.size()]));
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		factory.setJpaProperties(jpaHibernateProperties());
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	/**
	 * トランザクションマネージャーをDIコンテナに登録する.
	 * @param entityManagerFactory エンティティマネージャーファクトリー
	 * @return トランザクションマネージャー
	 */
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		transactionManager.setDefaultTimeout(30);
		transactionManager.setRollbackOnCommitFailure(true);
		return transactionManager;
	}
}
