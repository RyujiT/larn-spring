# データベース
CREATE DATABASE IF NOT EXISTS LARN_SPRING CHARACTER SET utf8mb4;

/*
 * マスタテーブルのテーブル生成クエリ.
 */
# APIキーマスタ
CREATE TABLE IF NOT EXISTS LARN_SPRING.M_API_KEY(API_KEY char(8) PRIMARY KEY, EXPIRATION_DATE DATE);
insert into LARN_SPRING.M_API_KEY(  API_KEY , EXPIRATION_DATE ) value ( 't|6M!D.X' , NULL );
insert into LARN_SPRING.M_API_KEY(  API_KEY , EXPIRATION_DATE ) value ( 'M2h)CAve' , NOW() );
insert into LARN_SPRING.M_API_KEY(  API_KEY , EXPIRATION_DATE ) value ( 'eM8~pQtH' , NOW() + INTERVAL 12 MONTH );

/*
 * トランザクションテーブルの生成クエリ.
 */
 