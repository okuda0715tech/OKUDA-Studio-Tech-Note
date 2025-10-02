- [layout_marginBaseline の使い方](#layout_marginbaseline-の使い方)

# layout_marginBaseline の使い方

自分の View の Baseline を他の View の Bottom や Baseline に対して制約を付けている時に  
少し上下に移動させたい。という場合に `layout_marginBaseline` が使用される。

つまり、
`layout_constraintBaseline_toBottomOf` や `layout_constraintBaseline_toBaselineOf`  
を使用しているときに、上下の位置に調整が必要な場合に `layout_marginBaseline` が使用される。
