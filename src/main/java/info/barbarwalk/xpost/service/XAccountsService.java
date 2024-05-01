package info.barbarwalk.xpost.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import info.barbarwalk.xpost.db.entity.XAccounts;
import info.barbarwalk.xpost.db.repository.XAccountsRepository;
import info.barbarwalk.xpost.webapi.dto.Users;
import info.barbarwalk.xpost.webapi.dto.Users.Data;
import lombok.extern.log4j.Log4j2;

/**
 * XアカウントDB操作関連サービスクラス。
 */
@Log4j2
@Service
public class XAccountsService {

	/** リポジトリインターフェース。 */
	@Autowired
	private XAccountsRepository repository;


	/**
	 * 登録処理を行う。
	 *   ※アカウントの存在チェックをし、あれば更新、無ければ登録を行う。
	 *
	 * @param users X API ユーザー情報
	 */
	public XAccounts save(Users users) {
		Data data = users.getData();
		String accountId = data.getId();

		XAccounts xAccounts = repository.findByAccountId(accountId).orElse(new XAccounts());
		log.info("DB登録状況：xAccounts={}", xAccounts);

		xAccounts.setAccountId(accountId);
		xAccounts.setName(data.getName());
		xAccounts.setUsername(data.getUsername());
		xAccounts.setLastLogin(new Date());

		return repository.save(xAccounts);
	}

	/**
	 * XアカウントをaccountIdで検索する。
	 * @param accountId アカウントID
	 * @return Xアカウント情報を返す。無ければnull。
	 */
	public XAccounts findByAccountId(String accountId) {
		return repository.findByAccountId(accountId).orElse(null);
	}
}
