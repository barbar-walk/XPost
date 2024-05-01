package info.barbarwalk.xpost.webapi.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import info.barbarwalk.xpost.AppDtoBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * X API ユーザー情報取得APIレスポンスクラス。
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class Users extends AppDtoBase {

	/**
	 * デフォルトコンストラクタ.
	 */
	public Users() {}

	/**
	 * コンストラクタ.
	 * @param name フレンドリーネーム（表示名）
	 * @param username スクリーンネーム
	 */
	public Users(String name, String username) {
		this.data = new Data();

		// TODO とりあえず適当にUUIDセット。
		UUID uuid = UUID.randomUUID();
		this.data.setId(uuid.toString());
		this.data.setName(name);
		this.data.setUsername(username);
	}

	/** レスポンス情報 */
	private Data data;

	/**
	 * レスポンス情報。
	 */
	@lombok.Data
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	@EqualsAndHashCode(callSuper = false)
	public static class Data extends AppDtoBase {
		/** デフォルト：一意の識別子 */
		private String id;
		/** デフォルト：フレンドリーネーム（表示名） */
		private String name;
		/** デフォルト：スクリーンネーム */
		private String username;
	}
}
