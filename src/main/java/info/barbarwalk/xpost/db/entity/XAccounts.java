package info.barbarwalk.xpost.db.entity;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * XアカウントEntityクラス。
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "x_accounts")
public class XAccounts extends EntityBase {

	/** ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/** 一意の識別子 */
	@Column(name = "account_id", nullable = false)
	private String accountId;

	/** フレンドリーネーム（表示名） */
	@Column(name = "name", nullable = false)
	private String name;

	/** スクリーンネーム */
	@Column(name = "username", nullable = false)
	private String username;

	/** 最終ログイン日時 */
	@Column(name = "last_login", nullable = false)
	private Date lastLogin;
}
