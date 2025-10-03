- [companion object](#companion-object)
  - [概要](#概要)
  - [使用方法](#使用方法)


# companion object

## 概要

companion object は、 Java でいう `static` の変わりになるものです。


## 使用方法

```kotlin
class Quiz {
    // companion object の後ろに名前がないバージョン.
    companion object {
        var total: Int = 15
        var answered: Int = 5
    }
}

fun main() {
    println("Total = ${Quiz.total}, Answerd = ${Quiz.answered}")
}
```



```kotlin
class Quiz {
    // companion object の後ろに名前があってもOK.
    companion object StudentProgress {
        var total: Int = 15
        var answered: Int = 5
    }
}

fun main() {
    println("Total = ${StudentProgress.total}, Answerd = ${StudentProgress.answered}")
    println("Total = ${Quiz.total}, Answerd = ${Quiz.answered}")
}
```


