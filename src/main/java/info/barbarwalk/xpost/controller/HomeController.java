package info.barbarwalk.xpost.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import info.barbarwalk.xpost.db.entity.Posts;
import info.barbarwalk.xpost.db.entity.XAccounts;
import info.barbarwalk.xpost.dto.RequestTweet;
import info.barbarwalk.xpost.service.PostsService;
import info.barbarwalk.xpost.service.XAccountsService;
import info.barbarwalk.xpost.webapi.XApiService;
import info.barbarwalk.xpost.webapi.dto.OauthToken;
import info.barbarwalk.xpost.webapi.dto.Tweets;
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
 	/** セッションキー：ユーザー情報保持。 */
 	public static final String SESSION_KEY_USER = "SESSION_KEY_USER";

	/** セッション情報。 */
	@Autowired
	private HttpSession session;

	/** X API 操作関連サービスクラス */
	@Autowired
	private XApiService xApiService;

	/** XアカウントDB操作関連サービス */
	@Autowired
	private XAccountsService xAccountsService;

	/** 投稿DB操作関連サービス */
	@Autowired
	private PostsService postsService;

	/**
	 * ログイン画面
	 */
	@GetMapping(path = { "", "/" })
	public String index() {
		// セッションの初期化。
		session.invalidate();
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

		XAccounts xAccounts = (XAccounts) session.getAttribute(SESSION_KEY_USER);

		// アクセストークンの取得
		ResponseEntity<OauthToken> responseOauthToken = xApiService.getOauthToken(code);
		log.info("アクセストークン：responseOauthToken={}", responseOauthToken);

		// 取得失敗したらとりあえずログイン画面に戻す。
		if (responseOauthToken == null) {
			log.warn("トークン取得失敗。");
			return "redirect:/";
		}

		OauthToken oauthToken = responseOauthToken.getBody();

		log.info("アクセストークン：oauthToken={}", oauthToken);
		// とりあえずセッションに保持。
		session.setAttribute(SESSION_KEY_TOKEN, oauthToken);

		// ユーザー情報取得（※Freeだと、1ユーザーに対して1日25回しか叩け無い。。。）
		ResponseEntity<Users> responseUsers = xApiService.getMe(oauthToken);
		log.info("ユーザー情報：responseUsers={}", responseUsers);

		Users users = null;

		// 取得失敗したらとりあえずログイン画面に戻す。
		if (responseUsers == null) {
			log.warn("ユーザー情報取得失敗。");

			// TODO Rate limit が厳しすぎるので、他の動作確認ができない為、取れなかったら適当に「匿名」でセットw。
			users = new Users("匿名さん", "hogehoge");

//				return "redirect:/";
		} else {
			users = responseUsers.getBody();
		}

		log.info("ユーザー情報：users={}", users);

		// DBに保存。
		xAccounts = xAccountsService.save(users);

		// とりあえずセッションに保持。
		session.setAttribute(SESSION_KEY_USER, xAccounts);

		return "redirect:/home";
	}

	/**
	 * ホーム画面の表示。
	 */
	@GetMapping("home")
	public String home(Model model) {

		OauthToken oauthToken = (OauthToken) session.getAttribute(SESSION_KEY_TOKEN);
		XAccounts xAccounts = (XAccounts) session.getAttribute(SESSION_KEY_USER);

		if (oauthToken == null || xAccounts == null) {
			log.warn("セッションからトークンが取得できませんでした。：oauthToken={}, xAccounts={}", oauthToken, xAccounts);
			return "redirect:/";
		}

		if (!model.containsAttribute("requestTweet")) {
			model.addAttribute("requestTweet", new RequestTweet());
		}

		// DBからユーザー情報取得。
		XAccounts users = xAccountsService.findByAccountId(xAccounts.getAccountId());
		log.info("ユーザー情報：users={}", users);

		// DBから投稿情報取得。
		List<Posts> postsList = postsService.findByXAccountsId(xAccounts.getId());

		model.addAttribute("users", users);
		model.addAttribute("postsList", postsList);

		return "home";
	}

	/**
	 * 投稿処理。
	 */
	@PostMapping("tweet")
	public String tweet(@Validated @ModelAttribute RequestTweet requestTweet,
			BindingResult result,
			RedirectAttributes redirectAttributes) {

		log.info("tweetアクションが呼ばれました。：requestTweet={}", requestTweet);

		OauthToken oauthToken = (OauthToken) session.getAttribute(SESSION_KEY_TOKEN);
		XAccounts xAccounts = (XAccounts) session.getAttribute(SESSION_KEY_USER);

		if (oauthToken == null || xAccounts == null) {
			log.warn("セッションからトークンが取得できませんでした。：oauthToken={}, xAccounts={}", oauthToken, xAccounts);
			return "redirect:/";
		}

		// バリデーション。
		if (result.hasErrors()) {
			log.warn("バリデーションエラーが発生しました。：requestTweet={}, result={}", requestTweet, result);

			redirectAttributes.addFlashAttribute("validationErrors", result);
			redirectAttributes.addFlashAttribute("requestTweet", requestTweet);

			return "redirect:/home";
		}

		String text = requestTweet.getText();

		ResponseEntity<Tweets> responseTweets = xApiService.postTweets(oauthToken, text);
		Tweets tweets = responseTweets.getBody();
		log.info("投稿完了：tweets={}", tweets);

		// DBに保存。
		postsService.save(xAccounts.getId(), tweets);

		return "redirect:/home";
	}
}
