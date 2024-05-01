package info.barbarwalk.xpost.db.entity;

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
 * 投稿Entityクラス。
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "posts")
public class Posts extends EntityBase {

	/** ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/** 一意の識別子 */
	@Column(name = "x_accounts_id", nullable = false)
	private Integer xAccountsId;

	/** 投稿ID */
	@Column(name = "tweet_id", nullable = false)
	private String tweetId;

	/** 本文 */
	@Column(name = "tweet_text", nullable = false)
	private String tweetText;
}
