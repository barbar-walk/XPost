package info.barbarwalk.xpost.dto;

import info.barbarwalk.xpost.AppDtoBase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 投稿DTOクラス。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RequestTweet extends AppDtoBase {

	/** 本文 */
	@NotBlank(message = "本文を入力してください。")
	@Size(max = 160, message = "本文は最大160文字です。")
	private String text;
}
