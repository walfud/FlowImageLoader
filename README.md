# Introduction
FlowImageLoader is very friendly to developer. Because every thing is intuitive and under your control.

# Usage

```
compile 'com.walfud:flowimageloader:1.0.0'
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

## TODO
3. Request queue for same url in same time