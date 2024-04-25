package info.barbarwalk.xpost.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import info.barbarwalk.xpost.webapi.XApiService;
import info.barbarwalk.xpost.webapi.dto.OauthToken;
import info.barbarwalk.xpost.webapi.dto.Users;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

/**
 * ホーム画面コントローラー。
 */
@Log4j2
@Controller
@RequestMapping("/")
public class HomeController {

 	/** セッションキー：トークン保持。 */
 	public static final String SESSION_KEY_TOKEN = "SESSION_KEY_TOKEN";

	/** セッション情報。 */
	@Autowired
	private HttpSession session;

	/** X API 操作関連サービスクラス */
	@Autowired
	private XApiService xApiService;

	/**
	 * ログイン画面
	 */
	@GetMapping(path = { "", "/" })
	public String index() {
		return "login";
	}

	/**
	 * ログイン処理（Xからのコールバック）
	 */
	@GetMapping("login")
	public String login(
			@RequestParam(required = false) String state,
			@RequestParam(required = false) String code,
			Model model) {

		// セッション情報取得。（トークンがあったら、そのままそれを使用。なかったら取得API。）
		OauthToken oauthToken = (OauthToken) session.getAttribute(SESSION_KEY_TOKEN);

		if (oauthToken == null) {
			// アクセストークンの取得
			ResponseEntity<OauthToken> responseOauthToken = xApiService.getOauthToken(code);
			log.info("アクセストークン：responseOauthToken={}", responseOauthToken);

			// 取得失敗したらとりあえずログイン画面に戻す。
			if (responseOauthToken == null) {
				log.warn("トークン取得失敗。");
				return "redirect:/";
			}

			oauthToken = responseOauthToken.getBody();
		}

		log.info("アクセストークン：oauthToken={}", oauthToken);
		// とりあえずセッションに保持。
		session.setAttribute(SESSION_KEY_TOKEN, oauthToken);

		// ユーザー情報取得
		ResponseEntity<Users> responseUsers = xApiService.getMe(oauthToken);
		log.info("ユーザー情報：responseUsers={}", responseUsers);

		// 取得失敗したらとりあえずログイン画面に戻す。
		if (responseUsers == null) {
			log.warn("ユーザー情報取得失敗。");
			return "redirect:/";
		}

		Users users = responseUsers.getBody();
		log.info("ユーザー情報：users={}", users);

		model.addAttribute("users", users.getData());

		return "home";
	}

}
