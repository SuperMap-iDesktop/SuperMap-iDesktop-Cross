#SuperMap iDesktop Cross8C 扩展开发指南
##一.配置组件许可
    SuperMap iDesktop Cross 8C是一款遵循 GPL 协议的开源产品，该产品基于SuperMap iObjects Java 8C组件产品开发，只需配置组件产品的许可即可，请参照《SuperMap iObjects Java 8C tar 包使用指南》配置组件许可。
##二.配置环境变量
###2.1.配置JAVA环境变量
将JAVA的路径加入系统的环境变量中，如果已经配置好JDK运行环境变量可跳过此步。 

*    Windows
    
  a)	右键点击计算机，选择“属性”，然后依次点击，高级系统设置-高级-环境变量。

  b)	在“系统变量”中新建JAVA_HOME变量，变量值设置为JAVA所在目录，例如：”C:\Program Files\Java\jdk1.7.0_67”。

  c)	在系统变量中的Path添加 :%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;
*	Linux

  a)	在终端中输入sudo gedit /etc/profile，打开 profile 文件；

  b)	在文件末尾加入：
 
         export JAVA_HOME=“`此处替换为JAVA目录`”

         export PATH=$JAVA_HOME/bin:$PATH

  c)	重启系统后设置生效。

###2.2 配置Eclipse的OSGi开发环境
Equinox 框架是 Eclipse 组织基于 OSGi Release 4 的一个实现框架，它实现了 OSGi 规范的核心框架和许多标准框架服务的实现。

访问[Eclipse官网](http://www.eclipse.org/downloads/)下载指定的开发版本：Eclipse for RCP and RAP Developers。该版本已经内置了OSGi开发环境，您只需要在新建插件工程（Plug-in Project）的时候选择OSGi Framework即可。

###2.3 配置SuperMap iObjects Java 8C组件环境变量
组件的环境变量可以直接在系统环境变量中配置，也可以在工程中配置，您可根据自己的需求进行选择。

####2.3.1 下载SuperMap iObjects Java 8C组件

访问[SuperMap 官方网站](http://www.supermap.com/cn/)，进入[技术资源中心产品下载页面](http://support.supermap.com.cn/DownloadCenter/ProductPlatform.aspx )，下载目标平台的 SuperMap iObjects Java 8C 组件产品。

* 在系统环境中配置组件环境变量
 
    将组件的路径添加到环境变量中，如果已配置好组件环境变量可跳过此步。

 *	Windows

    a)	在Path中加入组件的根目录，例如：D:\Program Files\SuperMap\SuperMap iObiects Java 7C\Bin
    
 *	Linux

    a)	在终端中输入sudo gedit /etc/profile，打开 profile 文件；

    b)	在文件末尾加入
       export LD_LIBRARY_PATH=“`组件路径`”:$LD_LIBRARY_PATH

    c)	重启系统使设置生效
    
*  在工程中配置组件环境变量

导入工程之后，在iDesktop目录下的iDesktop Frame Configuration.launch文件上右击，选择Run As中的Run Configurations。在弹出窗口左侧选择OSGi Framework中的iDesktop Frame Configuration,随后在右侧窗口中的Enviroment标签页中选择“New…” 按钮进行添加环境变量操作。
在工程中配置的方法：启动iDesktop插件的调试配置项（Debug Configurations）或者运行配置项（Debug Configurations），切换到Environment标签页，新建一个Environment variable，在弹出窗口中设置环境变量。
若使用的是Windows操作系统，则“Name”处应填写path；若为Linux操作系统则填写LD_LIBRARY_PATH。“Value”需填写为组件路径，点击OK 保存。

###2.4配置工程支持库
在更换了组件包以后，需要将组件bin目录中的所有.jar文件替换到/core/lib目录下，以添加jar包依赖。
##3. 编写代码

###3.1 导入工程

启动eclipse之后依次选择： File – Import – General – Exitsting Project into Workspace – Browse，弹出文件选择窗口。找到工程所在文件夹后点击Finish完成导入。
###3.2  新建项目
依次点击File – New - Project，打开New Project窗口。选择Plug-in Development目录下的Plug-in Project，点击Next输入工程名称, 其中OSGI framework 选择Equinox ,点击Next。Execution Environment设置为JDK1.7，最后点击Finish按钮即可完成新建。
###3.3 添加依赖关系
添加对Core和Controls两个插件的依赖。

* 在新建的工程上右键，在右键菜单上依次选择 Bulid  Path - Configure Bulid Path，在弹出窗口右方选择Projects，然后点击“Add…”按钮，添加Core和Controls项目。然后选择Libraries，移除Plug-in Dependencies，最后点击OK完成添加。
*	打开工程META-INF目录下的MANIFEST.MF文件，在Dependencies页面中的Required Plug-ins页面中点击“Add…”按钮添加Controls和Core包。

###3.4    编写代码
*	在生成的Activator类的Start()方法中添加初始化插件的代码：
```java
Application.getActiveApplication().getPluginManager().addPlugin(“SuperMap.Desktop.SampleCode”, bundleContext.getBundle());
```
将插件注册到项目中。
其中SuperMap.Desktop.SampleCode”为插件名称；bundleContext.getBundle()为Bundle对象。
*	然后创建功能类，继承CtrlAction，其中的run() 方法为执行时调用的函数，enable() 方法返回功能是否可用。
*	新增配置文件，在项目目录“\WorkEnvironment\Default”下新建配置文件，可参考工具包中的SampleCodeWorkEnvironment.xml文件进行添加。

###3.5 配置启动等级（可略过）
在工程中配置的方法：启动iDesktop插件的调试配置项（Debug Configurations）或者运行配置项（Debug Configurations），切换到Bundles标签页，找到新加的插件，设置StartLever，数值越大，插件启动越晚，然后点击Run运行程序。