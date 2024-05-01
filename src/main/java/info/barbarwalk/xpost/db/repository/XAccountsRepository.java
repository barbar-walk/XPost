package info.barbarwalk.xpost.db.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import info.barbarwalk.xpost.db.entity.XAccounts;

/**
 * Xアカウント関連リポジトリインターフェース。
 */
public interface XAccountsRepository extends PagingAndSortingRepository<XAccounts, Long>, CrudRepository<XAccounts, Long> {

	/**
	 * XアカウントをaccountIdで検索する。
	 * @param accountId アカウントID
	 * @return Xアカウント情報を返す。
	 */
	Optional<XAccounts> findByAccountId(String accountId);
}
