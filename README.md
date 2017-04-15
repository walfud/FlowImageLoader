# Introduction
FlowImageLoader is very friendly to developer. Because every thing is intuitive and under your control.


# Feature
1. **Fluent & Controllable** syntax
2. **Asynchronize** loading & handle in **UI-thread** no matter what thread you are in
3. **Lifecycle** awared (better adapted to `RecyclerView / RxActivity / RxFragment`)
4. **Reusable** for same url request of short time
5. **Faster** decode & **Space-Saving** for cache
6. Easy **extensible**
7. Better **Transition Animations**


# Usage

```
compile 'com.walfud:flowimageloader:2.0.0'
```

## Basic

#### Load Image With Cache
```java
FlowImageLoader.with(getApplicationContext())
      .load(Uri.parse("https://raw.githubusercontent.com/walfud/flowimageloader/master/doc/rose.png"))
      .cache()
      .into(imageView)
      .pls();
```

#### Load Image & Do Some Transformations before Cache
```java
FlowImageLoader.with(getApplicationContext())
      .load(Uri.parse("https://raw.githubusercontent.com/walfud/flowimageloader/master/doc/rose.png"))
      .resize(200, 100)
      .round(3, 3)
      .cache()
      .into(imageView)
      .pls();
```
All transformations are made in sequence you call them.

## Syntax Sugar
There are some 'flowers' makes your work easy. The 'flower' is an wrapper of `FlowImageLoader`, which aimed to meet the most common use. Usually, the 'flowers' does the following:
* Load image from url.
* Transform image to fit the `ImageView`. (If widget can't get bound, 'flowers' will try it later. Timeout is 2 seconds)
* Set the transformed image to widget.
* Cache the transformed image in order to future use.

So... It's very convenient to load network image to an `ImageView` with just one line code!

See the magic below.

#### Rose
`Rose` is rectangle with rounded.
```java
new Rose(imageView).open(url);
```
![effect_rose](https://raw.githubusercontent.com/walfud/flowimageloader/master/doc/effect_rose.png)

#### Sunflower
`Sunflower` is circled one.
```java
new Sunflower(imageView).open(url);
```
![effect_sunflower](https://raw.githubusercontent.com/walfud/flowimageloader/master/doc/effect_sunflower.png)