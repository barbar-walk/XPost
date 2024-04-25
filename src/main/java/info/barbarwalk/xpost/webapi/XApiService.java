package info.barbarwalk.xpost.webapi;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import info.barbarwalk.xpost.webapi.dto.OauthToken;
import info.barbarwalk.xpost.webapi.dto.Users;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * X API 操作関連サービスクラス。
 */
@Log4j2
@Service
public class XApiService {

	/**
	 * プロパティファイル（application.properties）の読み込み。
	 * ※「x_properties.xxx」の値とマッピング。
	 */
	@Data
	@Component
	@ConfigurationProperties(prefix = "x-properties")
	private static class XApiProperties {
		/** Client ID */
		private String clientId;
		/** Client Secret */
		private String clientSecret;

		/** Redirect URI */
		private String redirectUri;

		/** ベースURL */
		private String baseUrl;
		/** アクセストークン取得APIベースURL（oauth2/token） */
		private String oauth2TokenUrl;
		/** ユーザー情報取得APIベースURL（users/me） */
		private String usersMeUrl;
	}

	/** APIクライアント */
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	/** プロパティファイルの設定値 */
	@Autowired
	private XApiProperties xApiProperties;

	/**
	 * RestTemplateを取得する。
	 * ※RestTemplateに対して何か共通の初期化処理が必要になったらこの辺で実装。
	 *
	 * @return RestTemplateを返す。
	 */
	private RestTemplate getRestTemplate() {
		return restTemplateBuilder.build();
	}

	/**
	 * 実行時間をトレースする為、APIリクエスト処理のラップ。
	 */
	private <T> ResponseEntity<T> exchange(HttpHeaders headers, String url, Class<T> responseType,
			HttpMethod method, MultiValueMap<String, String> queryParams, Map<String, String> postParams, String body) throws RestClientException {
		log.info(">>>>> X API 処理開始 >>>>>：endpoint={}, responseType={}, queryParams={}, postParams={}", url, responseType.getName(), queryParams, postParams);

		Date startTime = new Date();

		// header設定
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		if (method == HttpMethod.POST) {
			entity = new HttpEntity<>(body, headers);
		}

		// クエリパラメータをセット
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		String uri = builder.queryParams(queryParams).toUriString();

		// APIリクエスト。
		try {
			ResponseEntity<T> t =
					this.getRestTemplate().exchange(uri, method, entity, responseType, postParams);
			Date elapsedTime = new Date();
			long elapsed = elapsedTime.getTime() - startTime.getTime();

			log.info("<<<<< X API 処理終了 <<<<<：経過時間({}ms)：endpoint={}, responseType={}, queryParams={}, postParams={}", elapsed, url, responseType.getName(), queryParams, postParams);
			return t;

		} catch (Exception e) {
			log.warn("<<<<< X API 異常終了" + " Exception=" + e);
			throw e;
		}
	}

	/**
	 * アクセストークン取得API。
	 *
	 * @param code 認可コード
	 * @return アクセストークンを取得する。
	 */
	public ResponseEntity<OauthToken> getOauthToken(String code) {
		String apiUrl = this.xApiProperties.baseUrl + this.xApiProperties.oauth2TokenUrl;

		// ヘッダー
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", this.generateBasicAuthHeader());

		// queryパラメータ
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();

		// POSTパラメータ
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("code", code);
		postParams.put("grant_type", "authorization_code");
		postParams.put("redirect_uri", this.xApiProperties.redirectUri);
		postParams.put("code_verifier", "challenge");
		postParams.put("client_id", this.xApiProperties.clientId);

		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		postParams.forEach(builder::queryParam);
		// 「?」を除去。
		String body = builder.build().encode().toUriString().substring(1);

		ResponseEntity<OauthToken> oauthToken = null;
		try {
			oauthToken = this.exchange(headers, apiUrl, OauthToken.class,
					HttpMethod.POST, queryParams, postParams, body);

		} catch (Exception e) {
			log.warn("アクセストークン取得に失敗しました。：code=" + code, e);
		}

		return oauthToken;
	}

	/**
	 * ユーザー情報取得API。
	 *
	 * @param oauthToken トークン情報
	 * @return ユーザー情報を取得する。
	 */
	public ResponseEntity<Users> getMe(OauthToken oauthToken) {
		String apiUrl = this.xApiProperties.baseUrl + this.xApiProperties.usersMeUrl;

		// ヘッダー
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + oauthToken.getAccessToken());

		// queryパラメータ
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();

		// POSTパラメータ
		Map<String, String> postParams = new HashMap<String, String>();

		ResponseEntity<Users> users = null;
		try {
			users = this.exchange(headers, apiUrl, Users.class,
					HttpMethod.GET, queryParams, postParams, null);

		} catch (Exception e) {
			log.warn("ユーザー情報取得に失敗しました。：oauthToken=" + oauthToken, e);
		}

		return users;
	}

	/**
	 * Basic認証の認証情報を生成。
	 * @return 認証情報
	 */
	private String generateBasicAuthHeader() {
		String credentials = this.xApiProperties.clientId + ":" + this.xApiProperties.clientSecret;
		String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
		return "Basic " + encodedCredentials;
	}
}
