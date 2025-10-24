<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [AbsentLiveData](#absentlivedata)
	- [概要](#概要)
	- [コード](#コード)

<!-- /TOC -->


# AbsentLiveData

## 概要

AbsentLiveDataとはnullが入ったLiveDataのことであり、MutableLiveDataではないため、更新もできません。


## コード

```Java
/**
 * A LiveData class that has {@code null} value.
 */
public class AbsentLiveData extends LiveData {
    private AbsentLiveData() {
        postValue(null);
    }
    public static <T> LiveData<T> create() {
        return new AbsentLiveData();
    }
}
```

