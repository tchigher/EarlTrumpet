# EarlTrumpet
一个轻量级的弹幕库

## 效果图

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200306193452813.gif)
## 支持功能如下：
- 可自定义弹幕的布局
- 弹幕不会重叠
- 弹幕的点击事件，可自行实现点击区域的策略
- 可设置弹幕的速度
- 支持缓存机制

## Todo List
- 支持多种类型的弹幕共存
- 完成每条弹幕的高度自动计算
- 支持自定义跑道的选择


 ## 过程
最开始的思路是使用一个ViewGroup，然后开个线程来进行add子view，然后子View每次都移动固定的单位值，这样就可以实现弹幕的效果，但等实现好了之后，发现存在很多弊端，比如容易出现卡顿，变量在不同线程中的管理难等。
因为这种的是频繁刷新操作，于是使用了SurfaceView，但是代码的改动就特别的大，不过核心的思路还是没变，例如弹幕跑道的选择，也就是新加入的弹幕药放在第几行合适，然后清理已经不再会出现的弹幕。
点击事件的改动比较大，因为之前是直接addView，所以可以直接对新增弹幕采用设置点击监听即可，但改成SurfaceView就需要做比较多的工作，需要通过GestureDetector来对事件的处理，比如获取在屏幕里的弹幕，然后检查点击的位置落在哪个弹幕上，加大了计算量。在弹幕点击事件这里大家可以自行实现点击范围策略接口来指定点击弹幕的哪个位置有效。
当然，该弹幕库还是存在很多的不足，例如弹幕满屏的情况下可能还会卡顿，需尽量避免在bindViewHolder中做相对耗时的操作，缓存的机制还不够完善，后面会继续增强缓存机制，还有提供更多可以灵活去自定义实现的方。
欢迎大家给出宝贵的建议和start。

## 最后
交流联系QQ：2504529451