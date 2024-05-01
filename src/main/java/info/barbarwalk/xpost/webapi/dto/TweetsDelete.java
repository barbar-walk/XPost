package info.barbarwalk.xpost.webapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import info.barbarwalk.xpost.AppDtoBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * X API 投稿APIレスポンスクラス。
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class TweetsDelete extends AppDtoBase {

	/**
	 * デフォルトコンストラクタ.
	 */
	public TweetsDelete() {}

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
		/** 削除結果 */
		private Boolean deleted;
	}
}
