<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Tag](#tag)
	- [Tagの使い方](#tagの使い方)
	- [コード](#コード)

<!-- /TOC -->


# Tag

## Tagの使い方

- Viewにちょっとしたデータを持たせる
- 保持するデータはIDではないため、重複することが認められている


## コード

```Java
TextView textView = new TextView(requireContext());

// データの格納
int data1 = 1;
String data2 = "2";
textView.setTag(R.id.key1, data1); // 第二引数はObject型で定義されている
taxtView.setTag(R.id.key2, data2);
// View に一つだけデータを持たせたい場合には、 key を指定せず、 データだけをセットすることが可能である。

// データの取得
int a = (int)textView.getTag(R.id.key1);
String b = String.valueOf(textView.getTag(R.id.key2));
```

```xml
<!-- ファイル名の例 ： tag_ids.xml -->
<?xml version="1.0" encoding="utf-8"?>
<resources>
  <item type="id" name="key1"/>
  <item type="id" name="key2"/>
</resources>
```


- `setTag`、`getTag`に使用するIDは、自分で生成してはいけない。
- 生成するには、リソースxmlファイル内で、<item type="id" name="xxx"\>とする。
