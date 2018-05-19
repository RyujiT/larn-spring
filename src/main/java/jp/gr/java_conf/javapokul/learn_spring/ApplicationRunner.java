package jp.gr.java_conf.javapokul.learn_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * アプリケーションの起動クラス.
 * @author rtaba
 */
@SpringBootApplication(exclude=DataSourceAutoConfiguration.class)
public class ApplicationRunner {

	/**
	 * アプリケーションの起動メソッド.
	 * @param args コマンド引数
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApplicationRunner.class, args);
	}
}
