- [master ブランチの変更手順](#master-ブランチの変更手順)


# master ブランチの変更手順

1. 新しくmasterブランチにしたいコミットで新しいブランチを作成する
（この時、旧masterブランチがmasterという名前で存在しているため、適当なブランチ名で作成する）

2.  Github上でデフォルトブランチをmasterから新masterブランチへと変更する
（SettingタブのBranchesメニューから変更可能）

3. Github上で旧masterブランチのリモートリポジトリを削除する
（codeタブの「X branches」メニューから削除可能）

4. Android studio上でローカルの旧masterブランチの名前を変更する
（一番右下の「Git: ブランチ名」のボタンからリネーム可能）

5. 旧masterブランチをGithubへpushする

6. Github上でデフォルトブランチを新masterから旧masterブランチへと変更する

7. リモートリポジトリの新masterブランチを削除する

8. ローカルブランチの新masterブランチをリネームする

9. 新masterブランチをpushする

10. Github上で新masterブランチをデフォルトブランチに変更する


