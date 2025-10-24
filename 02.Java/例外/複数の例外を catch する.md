- [複数の例外を catch する](#複数の例外を-catch-する)
  - [個別に catch する](#個別に-catch-する)
  - [一括で catch する](#一括で-catch-する)


# 複数の例外を catch する

## 個別に catch する

```java
catch (IOException ex) {
     logger.log(ex);
     throw ex;
} catch (SQLException ex) {
     logger.log(ex);
     throw ex;
}
```


## 一括で catch する

```java
catch (IOException | SQLException ex) {
     logger.log(ex);
     throw ex;
}
```






