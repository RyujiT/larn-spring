package jp.gr.java_conf.javapokul.larn_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * アプリケーションの起動クラス.
 * @author rtaba
 */
@SpringBootApplication
public class ApplicationRunner {

	/**
	 * サンプルアプリケーションの起動メソッド.
	 * @param args コマンド引数
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApplicationRunner.class, args);
	}
}
