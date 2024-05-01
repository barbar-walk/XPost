package info.barbarwalk.xpost.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import info.barbarwalk.xpost.db.entity.Posts;
import info.barbarwalk.xpost.db.repository.PostsRepository;
import info.barbarwalk.xpost.webapi.dto.Tweets;
import info.barbarwalk.xpost.webapi.dto.Tweets.Data;
import lombok.extern.log4j.Log4j2;

/**
 * 投稿DB操作関連サービスクラス。
 */
@Log4j2
@Service
public class PostsService {

	/** リポジトリインターフェース。 */
	@Autowired
	private PostsRepository repository;


	/**
	 * 投稿の登録処理を行う。
	 *
	 * @param xAccountsId DBで保持してる x_accounts テーブルのID
	 * @param tweets X API 投稿APIレスポンス情報
	 */
	public void save(Integer xAccountsId, Tweets tweets) {
		log.info("投稿の登録処理開始。：xAccountsId={}, tweets={}", xAccountsId, tweets);

		Data data = tweets.getData();

		Posts posts = new Posts();

		posts.setXAccountsId(xAccountsId);
		posts.setTweetId(data.getId());
		posts.setTweetText(data.getText());

		repository.save(posts);
	}

	/**
	 * x_accounts_id が一致する投稿情報を取得する。
	 * @param xAccountsId XアカウントテーブルのID
	 * @return 投稿情報の一覧を返す。
	 */
	public List<Posts> findByXAccountsId(Integer xAccountsId) {
		return repository.findByxAccountsIdOrderByIdDesc(xAccountsId);
	}

	/**
	 * 投稿データの削除を行う。
	 * @param tweetId 投稿ID
	 */
	public void delete(String tweetId) {
		// tweetIdから対象の投稿データを検索する。
		Posts posts = repository.findByTweetId(tweetId).orElse(null);

		if (posts != null) {
			repository.delete(posts);
		}
	}
}
