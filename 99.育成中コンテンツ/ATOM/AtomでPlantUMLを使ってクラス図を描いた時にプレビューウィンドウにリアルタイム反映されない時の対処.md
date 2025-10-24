<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [PlantUMLでリアルタイム反映されない場合の対処](#plantumlでリアルタイム反映されない場合の対処)
	- [Javaのバージョンは8をインストールする](#javaのバージョンは8をインストールする)
		- [バージョン確認方法](#バージョン確認方法)
	- [JDKは複数インストールされている場合は一つだけにする（v1.8.0が良さそう）](#jdkは複数インストールされている場合は一つだけにするv180が良さそう)

<!-- /TOC -->

# PlantUMLでリアルタイム反映されない場合の対処

## Javaのバージョンは8をインストールする

### バージョン確認方法

ターミナルで以下のコマンドを実行する
```
java -version
```

今インストールされているjavaのアンインストールとjava8のインストール手順は以下の通り

```
brew cask remove java
brew tap caskroom/versions
brew cask install java8
```

## JDKは複数インストールされている場合は一つだけにする（v1.8.0が良さそう）

手順は以下のサイトの通り

[macOSで古いJDKをアンインストール
](https://qiita.com/okoshi/items/8ef75fb0104f55fd1a3c)
