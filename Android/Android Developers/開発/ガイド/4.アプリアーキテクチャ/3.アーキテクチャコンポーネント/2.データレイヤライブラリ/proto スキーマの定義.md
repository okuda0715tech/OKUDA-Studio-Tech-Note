
# Protocol Buffers スキーマの記述方法

Protocol Buffers（プロトコルバッファ）は、Googleによって開発された効率的なシリアライズフォーマットです。データをコンパクトにバイナリ形式でシリアライズ・デシリアライズできるため、ネットワーク通信やストレージに最適です。以下は、Protocol Buffersのスキーマの記述方法についての要点をまとめたものです。

## 1. 基本構造
Protocol Buffersのスキーマは、`.proto`という拡張子を持つファイルに定義されます。このファイルでは、メッセージ型やサービスの定義を行います。

```proto
syntax = "proto3";

message Person {
  string name = 1;
  int32 id = 2;
  string email = 3;
}
```

## 2. syntax 宣言
スキーマファイルの先頭には、使用するProtocol Buffersのバージョンを指定する`syntax`宣言が必要です。現在主流なのは`proto3`です。

```proto
syntax = "proto3";
```

## 3. メッセージの定義
`message`キーワードを使って、データ構造を定義します。メッセージはフィールドの集合で、各フィールドには型、名前、タグ番号を指定します。

```proto
message Person {
  string name = 1;
  int32 id = 2;
  string email = 3;
}
```

- **フィールドの型**: `int32`、`string`、`bool`、`float`などの基本型、他のメッセージ型、または`repeated`を使ってリストにできます。
- **名前**: フィールド名は一意で、わかりやすい名前を付けます。
- **タグ番号**: 各フィールドには一意のタグ番号（1以上の整数）が割り当てられます。これによりバイナリフォーマットでのフィールド識別が可能です。

## 4. フィールドの型
Protocol Buffersにはさまざまな基本型があり、用途に応じて使い分けます。

- **整数型**: `int32`, `int64`, `uint32`, `uint64`など
- **浮動小数点型**: `float`, `double`
- **文字列型**: `string`
- **バイト型**: `bytes`
- **ブール型**: `bool`

## 5. repeated フィールド
同じ型の複数の値を持つフィールドは、`repeated`キーワードで定義します。これは配列やリストのように扱えます。

```proto
message Book {
  repeated string authors = 1;
}
```

## 6. ネストされたメッセージ
メッセージは他のメッセージの中にネストして定義することができます。

```proto
message AddressBook {
  message Person {
    string name = 1;
    int32 id = 2;
  }
  repeated Person people = 1;
}
```

## 7. Enums
列挙型は、特定のセットの定義済みの値を持つフィールドを定義する際に使用されます。

```proto
enum PhoneType {
  MOBILE = 0;
  HOME = 1;
  WORK = 2;
}
message PhoneNumber {
  string number = 1;
  PhoneType type = 2;
}
```

## 8. サービスの定義
RPC（Remote Procedure Call）サービスは、`.proto`ファイル内で定義できます。`service`キーワードを使ってサービス名とメソッドを指定します。

```proto
service PersonService {
  rpc GetPerson (PersonRequest) returns (PersonResponse);
}
```

## 9. デフォルト値
フィールドに対するデフォルト値を定義することができます。`default`キーワードを使って設定します。

```proto
message Config {
  string hostname = 1 [default = "localhost"];
}
```

ただし、 proto3 では default キーワードはサポートされていません。その代わりに、フィールドが指定されていない場合、以下のようなデフォルト値が自動的に適用されます：

- 数値型 (int32, int64, uint32, uint64, float, double など): - デフォルト値は `0` です。
- ブール型 (bool): デフォルト値は `false` です。
- 文字列型 (string): デフォルト値は空文字列 `""` です。
- バイト型 (bytes): デフォルト値は空のバイト列です。
- 列挙型 (enum): デフォルト値は列挙型で定義された最初の値です。
- メッセージ型: デフォルト値は「未設定」、つまり `null` 相当です。

proto3 で、カスタムデフォルト値が必要な場合は、コードレベルでデフォルト値を設定する必要があります。具体的には、デシリアライズ後にフィールドがデフォルトのままの場合に、自分でカスタムデフォルト値を設定する方法があります。

```python
# Pythonの例
person = Person()
if not person.name:
    person.name = "Default Name"
```

## 10. インポートと名前空間
他の`.proto`ファイルをインポートしたり、`package`キーワードで名前空間を定義することもできます。

```proto
import "other.proto";

package example;
```

## 11. 互換性の保持
スキーマは将来的に拡張や変更が可能ですが、後方互換性を維持するためにいくつかのルールに従う必要があります。例えば、既存のフィールドのタグ番号や型を変更せず、新しいフィールドを追加することは安全です。

## 12. スキーマファイル全体に影響する設定

option キーワードを使って、スキーマファイル全体に影響する設定を行うことができます。これらは通常、ファイルの先頭に記述します。

```proto
syntax = "proto3";

// パッケージ名を指定する
option java_package = "com.example.project";

// Javaクラス名を指定する
option java_outer_classname = "MyProtoClass";
```

## 13. そのメッセージに関連する設定

メッセージのスコープで option を使用して、そのメッセージに関連する設定を指定できます。この例では、Personメッセージにカスタムオプション (my_custom_option) が適用されています。

```proto
message Person {
  option (my_custom_option) = true;

  string name = 1;
  int32 id = 2;
}
```

## まとめ
Protocol Buffersのスキーマは、効率的かつ柔軟にデータを表現するための強力なツールです。基本的な構文と慣習に従うことで、スケーラブルで互換性のあるデータフォーマットを簡単に設計することができます。スキーマを慎重に設計することで、将来的な拡張や変更にも対応しやすくなります。
