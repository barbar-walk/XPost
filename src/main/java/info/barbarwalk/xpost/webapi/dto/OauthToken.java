package info.barbarwalk.xpost.webapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * X API アクセストークン取得APIレスポンスクラス。
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class OauthToken extends DtoBase {

	/**
	 * デフォルトコンストラクタ.
	 */
	public OauthToken() {}

	/** トークンタイプ */
	private String tokenType;
	/** 有効期限 */
	private String expiresIn;
	/** アクセストークン */
	private String accessToken;
	/** スコープ */
	private String scope;
}
