<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [SavedStateHandler](#savedstatehandler)
  - [準備](#準備)
<!-- TOC END -->


# SavedStateHandler

## 準備

`build.gradle` へ以下を追加

```java
implementation 'androidx.lifecycle:lifecycle-viewmodel-savedstate:2.2.0'
// ViewModel生成時の getDefaultViewModelProviderFactory() 呼び出しのために必要
implementation 'androidx.preference:preference:1.1.1'
```
