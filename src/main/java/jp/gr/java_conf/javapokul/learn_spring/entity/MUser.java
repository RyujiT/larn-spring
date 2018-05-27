package jp.gr.java_conf.javapokul.learn_spring.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザーマスタのエンティティ.
 * @author rtaba
 */
@Getter
@Setter
@ToString
@Entity
@Proxy(lazy=false)
@Table(name="M_USER")
public class MUser implements Serializable {

	private static final long serialVersionUID = 1L;

    /** ユーザー名. */
	@Id
	@Column(name="USER_NAME")
	private String userName;

	/** APIキー. */
	@Column(name="API_KEY")
	private String apiKey;
}
