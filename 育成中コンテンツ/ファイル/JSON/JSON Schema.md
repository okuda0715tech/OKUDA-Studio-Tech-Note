- [JSON Schema](#json-schema)
  - [JSON Shema とは](#json-shema-とは)
  - [JSON Schema の定義方法](#json-schema-の定義方法)
    - [プロパティ（ペア）の定義](#プロパティペアの定義)
  - [参考資料](#参考資料)


# JSON Schema

## JSON Shema とは

JSON Schema は、スキーマファイルです。データベースのテーブルを定義するスキーマファイルがあるように、 JSON データのフォーマットを定義するスキーマファイルが JSON Schema です。

JSON Schema は、 JSON 形式で記述されます。

以下に JSON Schema の簡単な例を示します。

```json
{
    // オブジェクト型
    "type": "object",
    "properties": {
        "name": {
            // 文字列型
            "type": "string"
        },
        "age": {
            // 数値型
            "type": "number"
        }
    }
}
```

このスキーマでは、以下のような JSON データを扱うことができます。

```json
{"name": "taro", "age": 25}
{"name": "taro"}
```


## JSON Schema の定義方法

### プロパティ（ペア）の定義

```json
{
    // プロパティの宣言
    "properties": {
        // プロパティ名
        "name": {
            // プロパティの型
            // 文字列型
            "type": "string"
        },
        "age": {
            // 数値型
            "type": "number"
        }
    }
}
```




## 参考資料

- [JSON Schema TOP ページ - 公式ドキュメント](https://json-schema.org/)
- [初めての JSON Schema を作成する - 公式ドキュメント](./公式ドキュメント/Docs/2.Getting%20Started/2.Creating%20your%20first%20schema.md)




