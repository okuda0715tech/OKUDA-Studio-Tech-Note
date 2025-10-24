<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [BETWEEN 仕様](#between-仕様)
  - [WHERE 句に BETWEEN とその他の条件を同時に指定することは可能か？](#where-句に-between-とその他の条件を同時に指定することは可能か)
  - [BETWEEN で指定した値は含まれるのか含まれないのか？](#between-で指定した値は含まれるのか含まれないのか)
<!-- TOC END -->


# BETWEEN 仕様

## WHERE 句に BETWEEN とその他の条件を同時に指定することは可能か？

例

```sql
SELECT id, name
FROM principals
WHERE type = 'R'
AND id BETWEEN 100 AND 200;
```

上記のように `type = 'R'` という条件と `BETWEEN` の条件 `id BETWEEN 100 AND 200` を
同時に指定することは可能です。


## BETWEEN で指定した値は含まれるのか含まれないのか？

**含まれます。**

境界値を含めたくない場合は、不等号を使って範囲指定する必要があります。
