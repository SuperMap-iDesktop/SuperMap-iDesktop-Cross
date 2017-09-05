# 产品介绍 #
 SuperMap iDesktop Cross 是一款支持跨平台、全开源的桌面GIS应用与开发平台系统，可在 Windows 和 Linux 系统上运行，是国内首款可在 Linux操作系统上运行的桌面GIS系统，实现了GIS数据在跨平台环境下的展示。SuperMap iDesktop Cross 是一款跨平台、全开源、可扩展的桌面GIS产品，也是超图新一代的开源GIS桌面产品。

SuperMap iDesktop Cross 是基于SuperMap iObjects Java 8C、Eclipse和OSGI等平台，通过Java语言开发的插件式、跨平台GIS 应用软件，提供了地图制图、数据管理、数据处理、数据分析等功能，同时提供了灵活的开发框架和辅助控件，便于用户二次开发。

更多详情请访问 [SuperMap 官方网站](http://www.supermap.com)

# 源代码地址 #

* [oschina](http://git.oschina.net/supermap/SuperMap-iDesktop-Cross)
* [CSDN](https://code.csdn.net/SuperMapDesktop/supermap-idesktop-cross)
* [GitHub](https://github.com/SuperMap-iDesktop/SuperMap-iDesktop-Cross)

# 联机帮助 #
SuperMap iDesktop Cross 的配套联机帮助采用基于 node.js 的静态网站工具 hexo 生成部署，并在 oschina 上托管联机帮助源代码，您可以基于该项目轻松扩展自己的联机帮助内容。同时同步推送至 Github pages 生成联机帮助项目主页，您可以在这里浏览查看最新的联机帮助内容。相关地址如下：

- [oschina 联机帮助源代码地址](https://git.oschina.net/supermap/SuperMap-iDesktop-Cross-Docs "oschina 联机帮助源代码地址")
- [Github pages 联机帮助主页](http://supermap-idesktop.github.io/SuperMap-iDesktop-Cross "Github 联机帮助主页")

# 使用指南 #
        配置好环境变量后在eclipse中导入工程即可直接启动。配置环境变量细节请参见《SuperMap iDesktop Cross 开发指南.md》第二章

# 更新历史 #

* SuperMap iDesktop Cross 8C SP1 
    * 地图制图
        * 新增了栅格专题图功能，可对栅格数据制作单值、分段专题图，并支持设置和修改专题图属性。
        * 优化了专题图属性面板，提高了专题图子设置项与图层管理器中图层节点显示的联动性。
    * 数据处理
        * 新增了SQL查询功能，可通过构建的 SQL 语句对矢量、属性表等数据集进行查询。
    * 数据分析
        * 新增了缓冲区分析功能，可根据指定的缓冲半径对点、线、面数据集进行缓冲区分析。

* SuperMap iDesktop Cross 8C
    * 产品发布

# Release版地址 #

*  [Release版下载地址](http://support.supermap.com.cn/DownloadCenter/ProductPlatform.aspx)

# 目录结构 #
* Assistant
    
    帮助相关插件，提供帮助支持。

* Configuration
    
    此文件夹用于存放桌面的一些启动参数。

* Controls
    
    界面相关公共插件，新添加插件如果需要使用桌面封装的界面则需要引入此包。

* Core
    
    代码逻辑相关公共插件，新添加插件如果需要使用桌面提供的公共方法则需要引入此包。

* DataConversion
    
    数据转换相关插件，如数据的导入导出。

* DataEditor
    
    数据编辑插件，如数据集新建，复制。

* DataProcess
    
    数据处理插件，如多边形融合（\*）等。

* DataTopology
    
    数据拓扑处理插件，如对数据集拓扑预处理。

* DataView
    
    数据浏览插件，如对数据集进行SQL查询等。

* Frame
    
    主窗体插件，用于管理桌面的主窗体，如退出桌面、登录(\*)等。

* iDesktop
    
    桌面启动插件，用于在桌面其他插件加载之后显示窗体。

* LayoutEditor
    
    布局编辑插件，如在布局上增加指北针(\*)，图例(\*)等。

* LayoutView
    
    布局浏览插件，如布局的放大(\*)、缩小(\*)、全幅显示(\*)。

* MapEditor
    
    地图编辑插件，如在地图上绘制对象、复制、粘贴等。

* MapView
    
    地图浏览插件，如在地图上选择对象，漫游和刷新等。

* NetServices

    网络服务插件，如发布iServer服务。

* Process

	可视化建模核心类库，提供可视化建模使用和扩展的全部功能接口。

* RealspaceEditor
    
    场景编辑插件，如在场景上绘制对象等。

* RealspaceEffect
    
    场景粒子对象绘制插件，如绘制火焰(\*)，降雨(\*)等特效。

* RealspaceView
    
    场景浏览插件，如在场景中漫游(\*),缩放(\*),刷新(\*)。
    
* Resources
    
    此文件夹用于存放桌面中用到的图标等资源。
    
* SpatialAnalyst
    
    空间分析插件，如查询栅格值，缓冲区分析等。
    
* TabularView
    
    属性表浏览插件，用于有关属性表的操作,如属性表的浏览，排序，统计。
    
* Templates
    
    此文件夹中存放了一些桌面用到的模板，如颜色方案，投影信息等。
    
* WorkEnvironment
    
    用于存放桌面界面的布局，其中Default文件夹下存放的是windows平台用的布局环境，Linux文件夹下存放的是Linux平台使用的布局环境。

* WorkflowView


# 二次开发说明 #
详情请参见 《SuperMap iDesktop Cross 开发指南.md》。

# 许可声明 #
详情请参见 《LICENSE》。

# FAQ #

Q：代码运行时抛异常

A：git上代码分为2个分支：master和develop。其中master分支为主版本分支，每次发布稳定版本时会推送到Master分支上。develop分支是开发分支，每天修改的代码都会推送，但不保证代码的稳定性。

Q：linux系统界面中的字体显示为方块

A:这是linux中文字体缺失导致的。在文件夹 /jre/jre/lib/fonts/fallback 下放置一个中文字体即可正常显示。

Q：地图中字体显示异常

A：在地图可视化过程中，会使用到一些特殊的字体，比如 “微软雅黑”等，这部分字体在 Linux操作系统中可能没有，从而导致地图注记等文本要素显示异常。请查看附件 fonts_1.zip 以及 fonts_2.zip，其中包含了主流常见字体，在 Linux操作系统使用本程序或源码时，请下载并解压到"根目录/support/fonts/"目录（如果没有，请自行创建）中，并添加环境变量 SUPERMAP_ROOT,该环境变量值设置为"根目录/support/"。然后参照《SuperMap iDesktop Cross 8C 扩展开发指南.doc》进行环境配置，即可正常显示文本要素。

Q：没有中文环境的Linux系统中发布带中文名称的地图服务，浏览器中地图预览图片显示异常。

A：若Linux没有中文环境，则发布的地图名称建议改成英文名称；或在有中文环境的Linux系统中发布含中文名称的地图服务。

# 更多帮助信息 #
请访问[SuperMap 技术资源中心](http://support.supermap.com.cn/product/iDesktop.aspx)获取技术支持。