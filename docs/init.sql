/* スキーマ（DB）作成 */
CREATE SCHEMA IF NOT EXISTS `xpost_db` DEFAULT CHARACTER SET utf8 ;

/* ユーザー作成 */
CREATE USER 'x_user'@'localhost' IDENTIFIED BY 'x_pass';

/* ユーザー権限設定 */
GRANT ALL PRIVILEGES ON xpost_db.* TO 'x_user'@'localhost';
FLUSH PRIVILEGES;

/* ユーザー削除（※為念、消す方法。コメントを外して使用する。） */
-- DROP USER 'x_user'@'localhost';
