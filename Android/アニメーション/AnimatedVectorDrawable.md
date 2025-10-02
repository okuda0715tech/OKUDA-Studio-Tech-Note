<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [AnimatedVectorDrawable](#animatedvectordrawable)
	- [概要](#概要)
	- [作成方法](#作成方法)
	- [複数ファイルのサンプル](#複数)
		- [VectorDrawableリソースの定義（アニメーション開始前の静止状態のリソース）](#vectordrawable定義開始前静止状態)
		- [アニメーションの定義](#定義)
		- [VectorDrawableとアニメーションを結びつけるファイル](#vectordrawable結)
	- [ファイルを一つにまとめた場合のサンプル](#一場合)

<!-- /TOC -->


# AnimatedVectorDrawable

## 概要

AnimatedVectorDrawableとは、`<animated-vector>`タグで囲まれたリソースのことを言います。  
VectorDrawableにアニメーションを設定することができます。


## 作成方法

AnimatedVectorDrawableリソースを作成するには、`Shape Shifter`というWebツールを使用します。  
GoogleのAndroid担当者もこのツールの使用を推奨しています。

用意するものは、アニメーションに使用する静止画像リソース（SVGファイルまたは、そのSVGファイルから生成したVectorDrawableファイル）です。  
Shape Shifterを使用して、それらにアニメーションを付与します。


## 複数ファイルのサンプル

### VectorDrawableリソースの定義（アニメーション開始前の静止状態のリソース）

**sample_vector_drawable_resource.xml**

```xml
<!-- アニメーションを行いたいパーツ（<group>/<path>）については、「android:name」属性が必須です。 -->
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:height="64dp"
    android:width="64dp"
    android:viewportHeight="600"
    android:viewportWidth="600" >
    <group
        android:name="rotationGroup"
        android:pivotX="300.0"
        android:pivotY="300.0"
        android:rotation="45.0" >
        <path
            android:name="v"
            android:fillColor="#000000"
            android:pathData="M300,70 l 0,-70 70,70 0,0 -70,70z" />
    </group>
</vector>
```


### アニメーションの定義

**rotation.xml**

```xml
<objectAnimator
    android:duration="6000"
    android:propertyName="rotation"
    android:valueFrom="0"
    android:valueTo="360" />
```

**path_morph.xml**

```xml
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <objectAnimator
        android:duration="3000"
        android:propertyName="pathData"
        android:valueFrom="M300,70 l 0,-70 70,70 0,0 -70,70z"
        android:valueTo="M300,70 l 0,-70 70,0  0,140 -70,0 z"
        android:valueType="pathType"/>
</set>
```


### VectorDrawableとアニメーションを結びつけるファイル

**sample_animated_vector_drawable_resource.xml**

```xml
<!-- 「android:drawable」属性は、アニメーションを行うVectorDrawableリソースを指定します。 -->
<animated-vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:drawable="@drawable/vectordrawable" >

    <!-- 「android:name」属性は、アニメーションを行うVectorDrawableリソース内の一つのパーツを指定します。
    具体的には、VectorDrawableリソース内の<group>または<path>の「android:name」属性の値を指定します。 -->
    <!-- 「android:animation」属性は、アニメーションオブジェクトを指定します。
    具体的には、アニメーションオブジェクトのファイル名を指定します。 -->
    <target
        android:name="rotationGroup"
        android:animation="@animator/rotation" />
    <target
        android:name="v"
        android:animation="@animator/path_morph" />
</animated-vector>
```


## ファイルを一つにまとめた場合のサンプル

```xml
<animated-vector xmlns:android="http://schemas.android.com/apk/res/android"
                  xmlns:aapt="http://schemas.android.com/aapt" >
     <aapt:attr name="android:drawable">
         <vector
             android:height="64dp"
             android:width="64dp"
             android:viewportHeight="600"
             android:viewportWidth="600" >
             <group
                 android:name="rotationGroup"
                 android:pivotX="300.0"
                 android:pivotY="300.0"
                 android:rotation="45.0" >
                 <path
                     android:name="v"
                     android:fillColor="#000000"
                     android:pathData="M300,70 l 0,-70 70,70 0,0 -70,70z" />
             </group>
         </vector>
     </aapt:attr>

     <target android:name="rotationGroup"> *
         <aapt:attr name="android:animation">
             <objectAnimator
             android:duration="6000"
             android:propertyName="rotation"
             android:valueFrom="0"
             android:valueTo="360" />
         </aapt:attr>
     </target>

     <target android:name="v" >
         <aapt:attr name="android:animation">
             <set>
                 <objectAnimator
                     android:duration="3000"
                     android:propertyName="pathData"
                     android:valueFrom="M300,70 l 0,-70 70,70 0,0 -70,70z"
                     android:valueTo="M300,70 l 0,-70 70,0  0,140 -70,0 z"
                     android:valueType="pathType"/>
             </set>
         </aapt:attr>
      </target>
 </animated-vector>
```


## 使用方法

GitHubの自分のリポジトリにある`#AnimatedVectorDrawable`（プロジェクト名`MyAnimatedVectorDrawable`）を参照
