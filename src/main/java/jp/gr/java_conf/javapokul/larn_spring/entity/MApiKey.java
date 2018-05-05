package jp.gr.java_conf.javapokul.larn_spring.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * APIキーのエンティティ.
 * @author rtaba
 */
@Getter
@Setter
@ToString
@Entity
@Proxy(lazy=false)
@Table(name="M_API_KEY")
public class MApiKey implements Serializable {

    private static final long serialVersionUID = 1L;

    /** APIキー. */
	@Id
	@Column(name="API_KEY")
	private String apiKey;

    /** 有効期限. */
	@Column(name="EXPIRATION_DATE")
	private Date expirationDate;
}
