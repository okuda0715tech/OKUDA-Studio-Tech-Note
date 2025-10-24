<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [ItemAnimator](#itemanimator)
  - [animateAppearance](#animateappearance)
  - [animatePersistence](#animatepersistence)
  - [runPendingAnimations](#runpendinganimations)
  - [endAnimation](#endanimation)
  - [endAnimations](#endanimations)
  - [isRunning](#isrunning)
<!-- TOC END -->


# ItemAnimator

## animateAppearance

```java
/**
 * ViewHolderがレイアウトに追加されたときに、RecyclerViewによって呼び出されます。
 * <p>
 * 具体的には、レイアウトの開始時にViewHolderが子として存在しておらず、LayoutManagerによって追加され
 * たことを意味します。アダプターに新しく追加されたか、他の要因により単に表示可能になった可能性があります。
 * <p>
 * ItemAnimatorは、アニメーションが完了したときにdispatchAnimationFinished（ViewHolder）を呼び出す
 * 必要があります（または、ビューをアニメーション化しないことを決定した場合は、
 * dispatchAnimationFinished（ViewHolder）を即座に呼び出す必要があります）。
 *
 * @param viewHolder
 * @param preLayoutInfo
 * @param postLayoutInfo
 * @return
 */
```


## animatePersistence

```java
/**
 * レイアウトの変更が要求されていないViewに対するアニメーションを実装するためのメソッド
 * <p>
 * レイアウトの前後の両方にViewHolderが存在し、RecyclerViewが
 * そのRecyclerView.Adapter.notifyItemChanged（int）呼び出しを受信していない、かつ、
 * RecyclerView.Adapter.notifyDataSetChanged（）呼び出しを受信していない場合に、
 * RecyclerViewによって呼び出されます。
 * <p>
 * このViewHolderは、レイアウト開始時に表示していたものと同じデータを引き続き表しますが、
 * その位置/サイズはLayoutManagerによって変更される場合があります。
 * <p>
 * アイテムのレイアウト位置が変更されなかった場合でも、RecyclerViewはこの情報を追跡しないため
 * （またはアニメーションが不要であることを必ずしも認識しないため）、このメソッドを呼び出します。
 * ItemAnimatorはこのケースを処理する必要があり、アニメーション化するものがない場合は、
 * dispatchAnimationFinished（ViewHolder）を呼び出してfalseを返す必要があります。
 * <p>
 * ItemAnimatorは、アニメーションが完了したときにdispatchAnimationFinished（ViewHolder）を呼び出す
 * 必要があります（または、ビューをアニメーション化しないことを決定した場合は、
 * dispatchAnimationFinished（ViewHolder）を即座に呼び出す必要があります）。
 *
 * @param viewHolder
 * @param preLayoutInfo
 * @param postLayoutInfo
 * @return
 */
```


## runPendingAnimations

```java
/**
 * 開始を待機している保留中のアニメーションがある場合に呼び出されます。
 * 保留中のアニメーションがあるかどうかは、animateAppearance（）、animateChange（）、
 * animatePersistence（）、および、animateDisappearance（）からの戻り値によって制御されます。
 * これらは、関連するアニメーションを開始するためにItemAnimatorを後で呼び出す必要があることを
 * RecyclerViewに通知します。runPendingAnimations（）は、次のフレームで実行されるようにスケジュール
 * されます。
 */
```


## endAnimation

```java
/**
 * アニメーションをすぐに終了する必要があるときに呼び出されるメソッド。これは、スクロールなどの
 * 他のイベントが発生したときに発生する可能性があるため、アニメーションビューを適切な終了位置に
 * すばやく配置できます。実装では、アイテムで実行されているアニメーションがキャンセルし、
 * アニメーションされるプロパティが最終値に設定されている必要があります。また、このメソッドが
 * 呼び出されるとアニメーションが効果的に実行されるため、終了したアニメーションごとに
 * dispatchAnimationFinished（ViewHolder）を呼び出す必要があります。
 *
 * @param item
 */
```


## endAnimations

```java
/**
 * endAnimation(@NonNull RecyclerView.ViewHolder item) に同じ
 */
```


## isRunning

```java
/**
 * 現在実行中のアイテムアニメーションがあるかどうかを返すメソッド。
 * このメソッドは、アニメーションが終了するまで他のアクションを遅らせるかどうかを決定するために使用できます。
 *
 * @return
 */
```
