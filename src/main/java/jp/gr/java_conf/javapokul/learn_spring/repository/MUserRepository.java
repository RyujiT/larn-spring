package jp.gr.java_conf.javapokul.learn_spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.gr.java_conf.javapokul.learn_spring.entity.MUser;

/**
 * ユーザーマスタのリポジトリ.
 * @author rtaba
 */
@Repository
public interface MUserRepository extends JpaRepository<MUser, String> {
	
	List<MUser> findByApiKey(String apiKey);
}