<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Utility](#utility)
  - [Integer で ID を管理する](#integer-で-id-を管理する)
  - [String で ID を管理する](#string-で-id-を管理する)
<!-- TOC END -->


# Utility

## Integer で ID を管理する

```java
/**
 * ID が無限に増え続ける可能性のない、軽量な ID は、この IntegerIdManager を使用する。
 * ID が Integer / int 型で定義されている場合に使用可能なクラスである。
 */
public class IntegerIdManager {

    public static int getNextIdFromIdList(List<Integer> ids) {
        int nextId = 1;

        // IDを昇順にソートする
        Collections.sort(ids);

        int currentMaxId = 0;
        if (ids.size() != 0) {
            // 現在、既に採番されているIDの最大値を取得
            currentMaxId = ids.get(ids.size() - 1);
        }

        if (currentMaxId == Integer.MAX_VALUE) {
            // 現在のリスト内の最大のIDがint型の最大値である場合は、1に戻り、1,2,3,...と順番に再利用可能なIDを探す。
            while (ids.contains(nextId)) {
                if (nextId == Integer.MAX_VALUE) {
                    // todo : 採番できるIDがなく、IDが桁あふれした場合の処理
                }
                nextId++;
            }
        } else {
            // 現在のIDの+1したIDを次のIDとする。
            nextId = currentMaxId + 1;
        }

        return nextId;
    }

    public static int getNextIdFromIdOwnerList(List<? extends IdOwner> idOwners) {

        List<Integer> ids = getIdsFromIdOwners(idOwners);

        return getNextIdFromIdList(ids);
    }

    public interface IdOwner {

        Integer getIntegerId();

    }

    public static List<Integer> getIdsFromIdOwners(List<? extends IdOwner> idOwners) {
        List<Integer> ids = new ArrayList<>();

        for (IdOwner idOwner : idOwners) {
            ids.add(idOwner.getIntegerId());
        }

        return ids;
    }

}
```


## String で ID を管理する

```java
/**
 * ID が無限に増え続ける可能性のある ID は、この StringIdManager を使用する。
 * ID が String 型で定義されている場合に使用可能なクラスである。
 */
public class StringIdManager {

    public static String getNextId(@NonNull List<? extends IdOwner> idOwners) {

        int currentMaxId = 1;

        if (idOwners.size() == 0) {
            return String.valueOf(currentMaxId);
        }

        for (IdOwner idOwner : idOwners) {
            int id = Integer.parseInt(idOwner.getStringId());
            if (currentMaxId < id) currentMaxId = id;
        }

        return String.valueOf(currentMaxId + 1);
    }

    public interface IdOwner {

        String getStringId();

    }

    public static String incrementId(String id) {
        return String.valueOf(Integer.parseInt(id) + 1);
    }
}
```
