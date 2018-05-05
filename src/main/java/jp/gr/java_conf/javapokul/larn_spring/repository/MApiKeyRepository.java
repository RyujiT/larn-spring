package jp.gr.java_conf.javapokul.larn_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.gr.java_conf.javapokul.larn_spring.entity.MApiKey;

/**
 * APIキーのリポジトリ.
 * @author rtaba
 */
@Repository
public interface MApiKeyRepository extends JpaRepository<MApiKey, String> {
}
