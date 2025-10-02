<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [複数のViewModelで一つのLiveDataを共有する](#複数のviewmodelて一つのlivedataを共有する)
	- [概要](#概要)
	- [コード](#コード)

<!-- /TOC -->


# 複数のViewModelで一つのLiveDataを共有する

## 概要

複数のViewModelで一つのLiveDataを共有するためには、LiveDataをシングルトンにします。

それ以外に特別なことはありません。


## コード

**FirstViewModel.java**

```java
public class FirstViewModel extends ViewModel implements GetDataTask.Listener{

    private SingletonLiveData randomNumLiveData;

    public SingletonLiveData getRandomNumLiveData() {
        if (randomNumLiveData == null) {
            randomNumLiveData = SingletonLiveData.getInstance();
            loadData();
        }
        return randomNumLiveData;
    }

    void updateRandomNumLiveData(){
        loadData();
    }

    private void loadData() {
        GetDataTask task = new GetDataTask();
        task.setListener(new WeakReference<GetDataTask.Listener>(this));
        task.execute();
    }

    @Override
    public void onFinish(String num) {
        randomNumLiveData.setValue(num);
    }

}
```


**SecondViewModel.java**

```java
public class SecondViewModel extends ViewModel implements GetDataTask.Listener{

    private SingletonLiveData randomNumLiveData;

    public SingletonLiveData getRandomNumLiveData() {
        if (randomNumLiveData == null) {
            randomNumLiveData = SingletonLiveData.getInstance();
            loadData();
        }
        return randomNumLiveData;
    }

    void updateRandomNumLiveData(){
        loadData();
    }

    private void loadData() {
        GetDataTask task = new GetDataTask();
        task.setListener(new WeakReference<GetDataTask.Listener>(this));
        task.execute();
    }

    @Override
    public void onFinish(String num) {
        randomNumLiveData.setValue(num);
    }

}
```


**SingletonLiveData.java**

```java
public final class SingletonLiveData extends MutableLiveData<String> {

    private static final SingletonLiveData sInstance = new SingletonLiveData();

    public static SingletonLiveData getInstance(){
        // ここでnullチェックしてしまうとスレッドセーフでなくなる。
        return sInstance;
    }

    // privateなコンストラクタ
    private SingletonLiveData(){

    }
}
```


