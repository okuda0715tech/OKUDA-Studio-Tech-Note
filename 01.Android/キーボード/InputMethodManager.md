<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [InputMethodManager](#inputmethodmanager)
  - [キーボードを非表示にする際に使用するパラメータ](#キーボードを非表示にする際に使用するパラメータ)
<!-- TOC END -->


# InputMethodManager

## キーボードを非表示にする際に使用するパラメータ

以下のそれぞれのパラメータを指定して、キーボードを閉じるメソッド `hideSoftInputFromWindow()`  
を実行した場合、キーボードが非表示になるかどうかを表に示します。

| パラメータ         | 暗黙的に表示された場合 | 明示的に表示された場合 | 強制的に表示された場合 |
|--------------------|------------------------|------------------------|------------------------|
| HIDE_IMPLICIT_ONLY | ○                      | ×                      | ×                      |
| HIDE_NOT_ALWAYS    | ○                      | ○                      | ×                      |

○ : 非表示になる。  
× : 非表示にならない。
