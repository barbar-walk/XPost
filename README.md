# XPost

X（旧：Twitter）のSNS連携のサンプルコードです。<br>
2024/05/02現在、Freeの開発アカウントで利用できるAPIを試したものになります。

- OAuth認証
- 自身の情報取得（GET /2/users/me）
- 投稿（POST /2/tweets）
- 投稿削除（DELETE /2/tweets/:id）

言語はJava、アーキテクチャはSpringBoot3.0.4使ってます。<br>
詳細はbuild.gradleを御覧ください。

画面のデザインは[Nice Admin](https://bootstrapmade.com/nice-admin-bootstrap-admin-html-template/)を利用させてもらいました。

## 画面説明

簡単な画面説明です。

### ログイン画面

ログイン画面です。

<img src="docs/screenshots/01_01_login.jpg" width="75%">

「ログイン！！」ボタンタップで、XのOAuth認証画面へ遷移します。

<img src="docs/screenshots/01_02_oauth.jpg" width="75%">

XのOAuth認証画面です。<br>
※簡易的な実装なので、認証後アクセストークンはセッションに保持しています。URL直打ちなどで、ログインページを表示すればセッション情報は破棄されます。


### 投稿画面

投稿画面です。<br>
投稿内容はアプリ内のDBに保存されるとともに、Xに投稿します。<br>
また、投稿した内容は削除することができます。<br>
削除機能は、Xに投稿した内容を削除し、DBに保存した内容も削除します。

<img src="docs/screenshots/02_01_post.jpg" width="75%">

初期表示時。投稿が無い状態です。

<img src="docs/screenshots/02_02_post.jpg" width="75%">

投稿がある状態です。<br>
「削除」ボタン押下で、該当の投稿が削除されます。

<img src="docs/screenshots/02_03_post.jpg" width="75%">

Xに投稿された内容です。<br>
※テキストのみ投稿可能です。リンクや画像などの投稿は対応できていません。


## ライセンス

このプロジェクトはMITライセンスのもとでライセンスされています。詳細については、LICENSE.txtファイルを参照してください。
