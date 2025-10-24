<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [attrs](#attrs)
  - [概要](#概要)
  - [使い道は二通り](#使い道は二通り)
    - [1. カスタム View のカスタム属性を定義するのに使用する](#1-カスタム-view-のカスタム属性を定義するのに使用する)
    - [2. テーマ属性を定義するのに使用する](#2-テーマ属性を定義するのに使用する)
  - [enum や flag を使用した例](#enum-や-flag-を使用した例)
  - [format](#format)

<!-- /TOC -->


# attrs

## 概要

`<declare-styleable>` タグは、 `<attr>` タグと一緒に使用され、  
`<declare-styleable>` タグの中に、 `<attr>` タグを定義する形で使用します。

一般的な使用例は以下のようになります。

**使用例**

```xml
<resources>
  <declare-styleable name="LabelView">  
    <attr name="text" format="string" />  
    <attr name="textColor" format="color" />  
    <attr name="textSize" format="dimension" />  
  </declare-styleable>  
</resources>
```

View 属性、もしくは、 Theme 属性を定義するのに使用します。


## 使い道は二通り

### 1. カスタム View のカスタム属性を定義するのに使用する

カスタム View での使用方法については、カスタム View の作成方法の資料を参照してください。


### 2. テーマ属性を定義するのに使用する

```xml
<resources>

  <!-- declare-styleable タグは、複数の attr タグを定義する場合の入れ物的なものか？ -->
  <declare-styleable name="CustomTheme">
    <!-- customData という名前のテーマ属性を定義する -->
    <attr name="customData" format="string" />
  </declare-styleable>
    
  <style name="AppBaseTheme" parent="android:Theme.Light">
    <!-- 中略 -->
  </style>

  <style name="CustomTheme" parent="AppBaseTheme">
    <!-- 中略 -->      
  </style>
    
  <style name="AppTheme" parent="CustomTheme">
    <item name="customData">"ABCDE"</item>
  </style>

</resources>
```

上記で、テーマ属性が定義され、値も設定されているため、 `?attr/customData` で、  
テーマ属性から値を取得することが可能になっています。


## enum や flag を使用した例

**enumの例**

```xml
<declare-styleable name="DraggableDot">  
  <attr name="radius" format="dimension" />  
  <attr name="legend" format="string" />  
  <attr name="anr">  
    <enum name="none" value="0" />  
    <enum name="thumbnail" value="1" />  
    <enum name="drop" value="2" />  
  </attr>  
</declare-styleable>  
```

**flagの例**

```xml
<declare-styleable name="Theme">  
  <attr name="windowSoftInputMode">  
    <flag name="stateUnspecified" value="0" />  
    <flag name="stateUnchanged" value="1" />  
    <flag name="stateHidden" value="2" />  
    <flag name="stateAlwaysHidden" value="3" />  
    <flag name="stateVisible" value="4" />  
    <flag name="stateAlwaysVisible" value="5" />  
  
    <flag name="adjustUnspecified" value="0x00" />  
    <flag name="adjustResize" value="0x10" />  
    <flag name="adjustPan" value="0x20" />  
  </attr>  
</declare-styleable>  
```


## format

- 型そのものを示す型
  - fraction
  - float
  - boolean
  - color
  - string
  - dimension
  - integer
- 特別な型
  - flag
  - enum
  - reference






