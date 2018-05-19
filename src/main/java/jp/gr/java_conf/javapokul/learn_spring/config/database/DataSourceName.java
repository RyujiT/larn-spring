package jp.gr.java_conf.javapokul.learn_spring.config.database;

/**
 * データソース名.
 * @author rtaba
 */
public final class DataSourceName {
	/** 読み込み専用データソース名. */
	public final static String READ_ONLY_DATASOURCE = "readOnlyDataSource";

	/** 書き込み可能データソース名. */
	public final static String READ_WRITE_DATASOURCE = "readWriteDataSource";
}
