##整理了一些这段时间做爬虫的思路：

##需求：
从首页http://www.autohome.com.cn/car/?pvareaid=101452开始，一共1962款车系需要全部爬取，
然后需要爬取三级目录，例如 阿尔法罗密欧 - 阿尔法罗密欧 - MiTo这种格式，爬取到[MiTo](http://www.autohome.com.cn/715/#levelsource=000000000_0&pvareaid=101594)
的详情页，需要获取的数据包括新车指导价，二手车价格等等，然后进入[参数配置](http://car.autohome.com.cn/config/series/2097.html)
的页面，将所有车款的信息爬取完毕。在车系首页会有两种情况，参数配置可点击或者不可点击,同时需要对停售的链接进行单独处理，或者停售车型的数据。关于详细车款组的信息
一定要获取全部的数据，避免遗漏。所以一条字段大概有300条左右，全部的数据大概有2w多条，全部代码由我一人完成。

##涉及的知识点：
1.关于Ajax请求：

①首页我们一直下拉才能看到获取的数据，很明显是Ajax请求，只要看XHR里的请求对其获取即可。
②我们在审查元素里看到的，不一定是真实的。可能是通过其他方式加载的，所以用正常的方式获取不到很正常，
这时候我们就要去network里刷新，找出是哪个请求获取的数据，然后请求这个链接，将其获取的数据进行解析即可。

2.关于JSON解析的技巧：

在获取车款组信息的时候，可以看到数据都是以JSON的方式加载的，keyLink 中观察到所有链接这个参数都相同，
判断可知这代表全部的数据，我们以这个为标准，进行解析。
解析JSON可以使用fastJSON，一个技巧就是将JSON平铺成guava中的table，非常方便使用。
