- [様々な padding](#様々な-padding)
  - [AlignmentLine](#alignmentline)
  - [VerticalAlignmentLine と HorizontalAlignmentLine](#verticalalignmentline-と-horizontalalignmentline)
  - [paddingFrom](#paddingfrom)
  - [paddingFromBaseline](#paddingfrombaseline)


# 様々な padding

## AlignmentLine

コンポーザブルを配置する際の 「寄せ」 を設定する際に使用する基準位置を表すオブジェクトです。

例えば、 Text コンポーザブルは AlignmentLine を持っていますが、 当然 AlignmentLine を持っていないコンポーザブルもあります。

AlignmentLine を直接生成することはほとんどなく、 VerticalAlignmentLine か HorizontalAlignmentLine が実際に生成さることがほとんどです。


## VerticalAlignmentLine と HorizontalAlignmentLine

VerticalAlignmentLine と HorizontalAlignmentLine は、両者を混同しがちな名前であるため、注意してください。

VerticalAlignmentLine (垂直方向の基準線) は、 Column など要素が縦に並ぶコンポーザブル内部で使用される、垂直方向の基準線という意味です。そのため、子要素の左右に対するパディングを設定するための基準線として使用されます。

HorizontalAlignmentLine (水平方向の基準線) は、 Row など要素が横に並ぶコンポーザブル内部で使用される、水平方向の基準線という意味です。そのため、子要素の上下に対するパディングを設定するための基準線として使用されます。


## paddingFrom

基準位置 ( AlignmentLine ) からの距離でパディングを設定するための修飾子です。基準位置には、主に FirstBaseLine と LastBaseLine の二種類が存在しており、この修飾子では、そのどちらを基準位置にするかを指定することが可能です。


## paddingFromBaseline

paddingFrom 修飾子を拡張した修飾子です。通常は、 paddingFrom よりも、 paddingFromBaseline を使用する機会が多いと思います。

引数の top に指定されたパディングは、自動的に FirstBaseine からの距離として扱われます。また、 bottom に指定されたパディングは、自動的に LastBaseine からの距離として扱われます。

