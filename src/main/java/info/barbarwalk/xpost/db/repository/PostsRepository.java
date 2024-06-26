package info.barbarwalk.xpost.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import info.barbarwalk.xpost.db.entity.Posts;

/**
 * 投稿関連リポジトリインターフェース。
 */
public interface PostsRepository extends PagingAndSortingRepository<Posts, Long>, CrudRepository<Posts, Long> {

	/**
	 * x_accounts_id が一致する投稿情報を取得する。
	 * @param xAccountsId XアカウントテーブルのID
	 * @return 投稿情報の一覧を返す。
	 */
	List<Posts> findByxAccountsIdOrderByIdDesc(@Param("xAccountsId") Integer xAccountsId);

	/**
	 * 投稿IDで投稿データを検索する。
	 * @param tweetId 投稿ID
	 * @return 投稿情報を返す。
	 */
	Optional<Posts> findByTweetId(@Param("tweetId") String tweetId);
}
