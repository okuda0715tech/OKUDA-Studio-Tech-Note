<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [FAQ](#faq)
  - [署名のベース文字列について](#署名のベース文字列について)
    - [リクエストボディーが JSON の場合](#リクエストボディーが-json-の場合)
<!-- TOC END -->


# FAQ

## 署名のベース文字列について

### リクエストボディーが JSON の場合

リクエストボディーが JSON の場合は、そのパラメータを署名のベース文字列に加える必要はない。  
署名のベース文字列に加えるのは、 `key=value&key=value` 形式のボディーの場合のみである。
