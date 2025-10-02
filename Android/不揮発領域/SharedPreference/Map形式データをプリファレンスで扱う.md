<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Map 形式データをプリファレンスで扱う](#map-形式データをプリファレンスで扱う)
  - [JSON 形式の文字列として扱うためのユーティリティ](#json-形式の文字列として扱うためのユーティリティ)
<!-- TOC END -->


# Map 形式データをプリファレンスで扱う

## JSON 形式の文字列として扱うためのユーティリティ

```Java
public class PreferenceUtil {

    public static <V> void saveMap(String prefFileName, String prefKey, Map<String, V> inputMap,
                                   Context context) {

        SharedPreferences preferences =
                context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);

        if (preferences == null) {
            return;
        }

        JSONObject jsonObject = new JSONObject(inputMap);
        String jsonString = jsonObject.toString();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(prefKey, jsonString);
        editor.apply();
    }

    public static <V> Map<String, V> loadMap(String prefFileName, String prefKey, String prefDefVal,
                                             Class<V> mapValClass, Context context) {

        Map<String, V> outputMap = new HashMap<>();

        SharedPreferences preferences =
                context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);

        if (preferences == null) {
            return outputMap;
        }

        try {
            String jsonString = preferences.getString(prefKey, prefDefVal);
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> keysItr = jsonObject.keys();
            while (keysItr.hasNext()) {
                String jsonKey = keysItr.next();
                Object jsonValObj = jsonObject.get(jsonKey);

                // ジェネリクスでは、 instanceOf が使用できないため、
                // 特殊な方法で 「型チェック & ダウンキャスト」 を行っている。
                V jsonVal = null;
                if (mapValClass.isAssignableFrom(jsonValObj.getClass())) {
                    jsonVal = mapValClass.cast(jsonValObj);
                }

                outputMap.put(jsonKey, jsonVal);
            }
        } catch (JSONException e) {
            // TODO エラー処理。必要なら実装する。
        }
        return outputMap;
    }
}
```
