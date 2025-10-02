


# 動的に生成したViewにマージンが適用されない

Viewをinflateする際に`parentViewGroup`を`null`にしてはいけません。  
以下のようにinflateするとマージンが適用されます。
`inflater.inflate(R.layout.test, linearLayout, false);`

動的に生成したViewを親ViewGroupと関連付けなければ、LayoutParameterが取得できないことが原因です。

LayoutInflaterを使用せずに、newでViewを生成し、addする場合は、「addしてからLayoutMarameterを取得する」必要があります。  
そうしなければ、LayoutParameterが取得できず、nullが返ってくるためです。  
以下にサンプルコードを載せます。

```java
dirNamesContaier.addView(dirNameButton);

// getLayoutParamsは、viewをlayoutにaddしてから取得しないとnullが返ってくる
ViewGroup.LayoutParams layoutParams = dirNameButton.getLayoutParams();
ViewGroup.MarginLayoutParams marginLP = (ViewGroup.MarginLayoutParams) layoutParams;
int margin = (int) getResources().getDimension(R.dimen.interval_3dp);
marginLP.setMargins(margin, margin, margin, margin);
dirNameButton.setLayoutParams(marginLP);
```
