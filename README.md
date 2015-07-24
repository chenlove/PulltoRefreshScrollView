# PulltoRefreshScrollView简介
1、一个简单的下拉刷新组件，继承自ScrollView。

#使用
1、导入module，直接在XML替换ScrollView为MyScrollView。

2、默认使用ScrollView里LinearLayout第一个子View为header(下拉时候显示的内容)。

3、实现两个监听器onPullToRefreshListener(开始刷新)、onRefreshAbleStatusChangedListener(是否可刷新状态的改变)。

4、完成刷新操作时，调用refreshCompleted()让header隐藏。


