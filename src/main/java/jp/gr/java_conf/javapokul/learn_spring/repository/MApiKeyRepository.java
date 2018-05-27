package jp.gr.java_conf.javapokul.learn_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.gr.java_conf.javapokul.learn_spring.entity.MApiKey;

/**
 * APIキーマスタのリポジトリ.
 * @author rtaba
 */
@Repository
public interface MApiKeyRepository extends JpaRepository<MApiKey, String> {
}
