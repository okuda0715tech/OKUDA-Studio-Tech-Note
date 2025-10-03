<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [WorkInfo.State](#workinfostate)
  - [State 一覧](#state-一覧)
  - [isFinished()](#isfinished)
  - [State の状態の更新を受け取るサンプル](#state-の状態の更新を受け取るサンプル)
<!-- TOC END -->


# WorkInfo.State

## State 一覧

| 状態      | 意味                                                                                                      |
|-----------|-----------------------------------------------------------------------------------------------------------|
| BLOCKED   | 前提条件が正常に終了していないため、 WorkRequest が現在ブロックされていることを示す。                     |
| CANCELLED | WorkRequest がキャンセルされ、実行されないことを示す。                                                    |
| ENQUEUED  | WorkRequest が既にエンキューされており、 ERROR ( /Constraints) が満たされれば、実行可能であることを示す。 |
| FAILED    | WorkRequest が失敗した状態で完了したことを示す。                                                          |
| RUNNING   | WorkRequest が現在実行中であることを示す。                                                                |
| SUCCEEDED | WorkRequest が成功した状態で完了したことを示す。                                                          |


## isFinished()

`SUCCEEDED` , `FAILED` , `CANCELLED` 状態の場合は `true` を返します。


## State の状態の更新を受け取るサンプル

```Java
private void createListenerAndEnqueue(WorkRequest workRequest) {
  LiveData<WorkInfo> workInfoLiveData =
      workManager.getWorkInfoByIdLiveData(workRequest.getId());
  workInfoLiveData.observeForever(getObserver(workInfoLiveData));

  workManager.enqueue(workRequest);
}

@NonNull
private Observer<WorkInfo> getObserver(LiveData<WorkInfo> workInfoLiveData) {
  return new Observer<WorkInfo>() {
    @Override
    public void onChanged(WorkInfo workInfo) {
      if (workInfo == null) {
        return;
      }
      if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) {
        // do something.
      }
      if (workInfo.getState().equals(WorkInfo.State.FAILED)) {
        // do something.
      }
      if (workInfo.getState().isFinished()) {
        // この removeObserver を忘れずに実装すること！
        workInfoLiveData.removeObserver(this); // この this は Observer オブジェクト
      }
    }
  };
}
```
