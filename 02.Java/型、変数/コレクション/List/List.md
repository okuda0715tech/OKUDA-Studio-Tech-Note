<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [List](#list)
	- [List 型の変数を ArrayList 型にダウンキャストする方法](#list-型の変数を-arraylist-型にダウンキャストする方法)
	- [Object 型などの上位クラスを List 型のデータにダウンキャストする方法](#object-型などの上位クラスを-list-型のデータにダウンキャストする方法)
	- [リストのコピーを生成する](#リストのコピーを生成する)
	- [ある List の中身を別の List の末尾に追加する (シャローコピー)](#ある-list-の中身を別の-list-の末尾に追加する-シャローコピー)
	- [List 内の指定した要素の値を入れ替える](#list-内の指定した要素の値を入れ替える)
	- [List 内の全ての要素に対して、一括で同じ値をセットする。](#list-内の全ての要素に対して一括で同じ値をセットする)
	- [List の指定した範囲の要素を参照するビューを定義する](#list-の指定した範囲の要素を参照するビューを定義する)
	- [List の指定した範囲の要素を削除する](#list-の指定した範囲の要素を削除する)
	- [生成と同時に初期化する](#生成と同時に初期化する)
	- [二つのリストを比較し、削除された要素を取得する](#二つのリストを比較し削除された要素を取得する)
<!-- TOC END -->


# List

## List 型の変数を ArrayList 型にダウンキャストする方法

`new ArrayList<>({List型の変数})`とするだけでOK

**Sample.java**

```Java
List<String> linkedList = new LinkedList<>();
linkedList.add("A");
linkedList.add("B");
linkedList.add("C");
ArrayList<String> arrayList = new ArrayList<>(linkedList);
```


## Object 型などの上位クラスを List 型のデータにダウンキャストする方法

```java
public class ListUtil {

    /**
     * Object 型のオブジェクトを List<\E> 型にキャストする。
     *
     * @param object       List にキャストしたいオブジェクト
     * @param elementClass リストの要素の Class クラス
     * @param <E>          リストの要素の型
     * @return List に変換されたオブジェクト
     */
    public static <E> List<E> castObjToList(Object object, Class<E> elementClass) {

        List<E> castedList = new ArrayList<>();

        // List であることを保証
        AppAssertion.assertList(object);
        List<?> list = (List<?>) object;

        // 要素の型を保証
        AppAssertion.assertElementType(elementClass, list);
        for (Object element : list) {
            castedList.add(elementClass.cast(element));
        }

        return castedList;
    }
}
```

```java
public class AppAssertion {

    public static void assertList(Object object) {
        if (!(object instanceof List<?>)) {
            throw new AssertionError();
        }
    }

    public static <E> void assertElementType(Class<E> elementClass, List<?> list) {
        for (Object elementObject : list) {
            boolean canCast = elementClass.isAssignableFrom(elementObject.getClass());
            if (!canCast) {
                throw new AssertionError();
            }
        }
    }
}
```


## リストのコピーを生成する

**【注意】**
この方法では、リスト自体は別のインスタンスになりますが、要素は元のリストと同じインスタンスを  
参照しています。そのため、どちらかのリストを更新した場合は、もう一方のリストも更新されます。  
さらに、どちらかのリストをソートした場合も、もう一方のリストがソートされるため、注意してください。

```Java
List<Data> list1 = new ArrayList<>();
List<Data> list2 = new ArrayList<>(list1);
```


## ある List の中身を別の List の末尾に追加する (シャローコピー)

**【注意】**
この方法では、リスト自体は別のインスタンスになりますが、要素は元のリストと同じインスタンスを  
参照しています。そのため、どちらかのリストを更新した場合は、もう一方のリストも更新されます。  
さらに、どちらかのリストをソートした場合も、もう一方のリストがソートされるため、注意してください。

`list1.addAll(list2)` メソッドを使用します。  
上記を実行すると list1 の末尾に list2 の値が追加されます。

list1 の指定した位置に list2 を追加したい場合は、  
`list1.addAll(index, list2)` メソッドを使用します。

**Sample.java**

```Java
List<Integer> list = new ArrayList<Integer>();

list.add(1);
list.add(2);
list.add(3);
list.add(4);

List<Integer> list2 = new ArrayList<Integer>();

list2.add(5);
list2.add(6);
list2.add(7);
list2.add(8);

list.addAll(list2);
```


## List 内の指定した要素の値を入れ替える

`{List型のインスタンス}.set({index},{書き換え後のオブジェクト});`を使用します。

**SetSample.java**

```Java
List<Integer> list = new ArrayList<Integer>();
list.add(1);
list.add(2);
list.add(3);
list.set(0, 10);
```

上記の例を実行すると、listの3つの要素の値が `10` , `2` , `3` になります。


## List 内の全ての要素に対して、一括で同じ値をセットする。

**SetSample.java**

```Java
List<Integer> list = new ArrayList<Integer>();
list.add(1);
list.add(2);
list.add(3);
Collections.fill(list, 10);
```

上記の例を実行すると、 list の 3 つの要素の値が全て `10` になります。


## List の指定した範囲の要素を参照するビューを定義する

この方法では、元のリストのビューを返します。  
実際の要素のデータは複製されず、同じオブジェクトを参照します。  
そのため、どちらかのリストから要素を更新すると、もう一方も更新されます。  
また、どちらかのリストをソートすると、もう一方にもソートが反映されます。

```java
List<integer> mainList = new ArrayList<>();
mainList.add(0);
mainList.add(1);
mainList.add(2);
mainList.add(3);
mainList.add(4);

// 第一引数：FromIndex
// 第二引数：ToIndex
List<integer> sbList = mainList.subList(1, 3);

System.out.println("元のList = " + mainList); // 元のList = [0, 1, 2, 3, 4]
System.out.println("subList = " + sbList); // subList = [1, 2]
```


## List の指定した範囲の要素を削除する

```java
public static void main(String[] args) {
		List<String> names = new ArrayList<>();
		names.add("White");
		names.add("Yellow");
		names.add("Red");
		names.add("Blue");
		names.add("Black");


		// 要素番号が1と2の要素を削除する
		// endIndexはその一つ前の要素までが削除される
		names.subList(1, 3).clear();

		// 削除前
		// [White, Yellow, Red, Blue, Black]
		// 削除後
		// [White, Blue, Black]
}
```


## 生成と同時に初期化する

```Java
List<String> list = new ArrayList<String>(Arrays.asList("Apple", "Orange", "Melon"));
```


## 二つのリストを比較し、削除された要素を取得する

```Java
/**
 * 引数で与えられた二つのリストを比較し、削除された要素のオブジェクトを返す。
 * <p>
 * 【前提】
 * oldList と newList は、 equals() メソッドをオーバーライドしていること。
 * oldList と newList の要素の格納順は異なっていても問題なく動作する。
 *
 * @param oldList 要素を削除する前のリスト
 * @param newList 要素を削除した後のリスト
 * @param <E>     要素の型
 * @return 【正常系】
 * 削除された要素を返す。
 * 【異常系】
 * oldList と newList の要素数が、 oldList.size() - newList.size() == 1 でない場合は、アサーションエラーとする。
 * oldList の要素のうち、 newList に含まれていない要素が二つ以上存在する場合は、アサーションエラーとする。
 */
public static <E> E getRemovedElement(List<E> oldList, List<E> newList) {
	// 要素数が一つだけ減少していること
	assert oldList.size() - newList.size() == 1;

	List<E> notFoundItemList = new ArrayList<>();

	for (E oldListItem : oldList) {
		if (newList.contains(oldListItem)) {
			continue;
		}
		notFoundItemList.add(oldListItem);
	}

	// 削除されてた要素が一つだけであること
	assert notFoundItemList.size() == 1;

	return notFoundItemList.get(0);
}
```
