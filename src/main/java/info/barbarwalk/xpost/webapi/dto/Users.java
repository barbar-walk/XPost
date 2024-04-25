package info.barbarwalk.xpost.webapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * X API ユーザー情報取得APIレスポンスクラス。
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class Users extends DtoBase {

	/**
	 * デフォルトコンストラクタ.
	 */
	public Users() {}

	/** レスポンス情報 */
	private Data data;

	/**
	 * レスポンス情報。
	 */
	@lombok.Data
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	@EqualsAndHashCode(callSuper = false)
	public static class Data extends DtoBase {
		/** デフォルト：一意の識別子 */
		private String id;
		/** デフォルト：フレンドリーネーム（表示名） */
		private String name;
		/** デフォルト：スクリーンネーム */
		private String username;
	}
}
