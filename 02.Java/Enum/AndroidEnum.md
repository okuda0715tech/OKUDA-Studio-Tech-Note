<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [AndroidEnum](#androidenum)
	- [EnumにResource IDを持たせる](#enumにresource-idを持たせる)

<!-- /TOC -->


# AndroidEnum

## EnumにResource IDを持たせる

```Java
public enum SortType {
    ITEM_NAME_ASC(R.string.title_asc),
    ITEM_NAME_DESC(R.string.title_desc),
    CREATE_DATE_ASC(R.string.create_date_asc),
    CREATE_DATE_DESC(R.string.create_date_desc),
    UPDATE_DATE_ASC(R.string.update_date_asc),
    UPDATE_DATE_DESC(R.string.update_date_desc);

    private Integer resourceId;

    SortType(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getName(Resources resources) {
        return resources.getString(resourceId);
    }
}
```

```Java
// getNameメソッドを呼び出す際に、リソースのインスタンスを与える。
// 以下は、Activityから呼び出す場合のサンプル
SortType.ITEM_NAME_ASC.getName(getResources())
```

