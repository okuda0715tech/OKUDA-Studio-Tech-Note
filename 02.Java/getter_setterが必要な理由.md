# getter_setterが必要な理由

## Android開発でgetter_setterが必要な理由

以下のようなコードがあった場合に、コンストラクタ内の「id」がAppLineDataSetクラスのフィールド「id」なのか、それともSimulationConditionクラスの「id」なのかが一見わかりずらい。（Android Sdudio上でのコードハイライトが同じになっている為。）

```Java
public class AppLineDataSet extends LineDataSet {

    /**
     * LineDataSet管理用ID
     */
    public long id;

    /**
     * コンストラクタ
     * @param yVals
     * @param label
     */
    public AppLineDataSet(List<Entry> yVals, String label, SimulationCondition condition) {
        super(yVals, label);
        id = condition.id;
    }
}
```

よく見れば、`condition.id`か`id`なので区別ができるのだが、普段、ハイライト表示で判別している開発者（自分以外の誰か）が見た場合に、一瞬戸惑ってしまう。

対策としては、getter,setterを定義して、自分のクラス外からフィールドにアクセスする場合には、getterで参照する。また、自分のクラス内からアクセスする場合には、getterを使わずに直接参照する。

なお、フィールド名を「mXXXX」という命名規則にしただけでは、上記の例では、どちらのフィールドも「mId」となってしまう為、やはりgetterを定義するべきである。
