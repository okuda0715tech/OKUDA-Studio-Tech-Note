<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [onTouchとonTouchEventの違い](#ontouchontouchevent違)

<!-- /TOC -->


# onTouchとonTouchEventの違い

- onTouchEvent
  - ユーザーがUIを操作した場合にOSが呼び出すコールバックメソッドである。
  - ActivityやViewやTextViewなどが実装しているメンバメソッドである。
  - 使用方法としては、ViewやTextViewを継承したクラスを作成し、そこでonTouchEventをオーバーライドして、独自の処理を実装する。
    - 他のView等を継承してオーバーライドするのが特徴である。
  - 特にActivityは`setOnTouchListener`メソッドが存在しないため、`onTouch`が使用できない。そのため、`onTouchEvent`を使用する。
  - `onTouch`の方が優先的に呼ばれます。`onTouch`メソッドで`false`を返した場合のみ、`onTouchEvent`が呼び出されます。
  - イベントリスナと呼ばれることもある。
  - 【重要】`ACTION_DOWN`で`false`を返しても、後続の`ACTION_MOVE`や`ACTION_UP`は呼ばれます。
- onTouch
  - `View.OnTouchListener`インターフェースの抽象メソッドである。
  - `View.setOnTouchListener(OnTouchListener)`メソッドで上記のリスナーをセットして使用する。
  - `onTouchEvent`とは異なり、わざわざViewなどを継承して独自のクラスを定義する必要がないため、手軽に使用することができる。
  - `onTouchEvent`よりも優先して呼ばれます。`onTouch`メソッドで`false`を返した場合のみ、`onTouchEvent`が呼び出されます。
  - `onTouch(View v, MotionEvent event)`引数に`View`をとる点が`onTouchEvent`と異なる。
  - イベントハンドラと呼ばれることもある。
  - 【重要】`ACTION_DOWN`で`false`を返した場合、後続の`ACTION_MOVE`や`ACTION_UP`は呼ばれません。
