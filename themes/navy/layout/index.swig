<div id="banner-wrapper">
<div id="banner_tabs" class="flexslider">
	<ul class="slides">
		<li>
			<a title="" target="_blank" href="/SuperMap-iDesktop-Cross/news/">
				<img width="1920" height="482" alt="" style="background: url(/SuperMap-iDesktop-Cross/img/banner1.png) no-repeat center;" src="/SuperMap-iDesktop-Cross/img/alpha.png">
			</a>
		</li>
		<li>
			<a title="" href="/SuperMap-iDesktop-Cross/docs/index.html">
				<img width="1920" height="482" alt="" style="background: url(/SuperMap-iDesktop-Cross/img/banner2.png) no-repeat center;" src="/SuperMap-iDesktop-Cross/img/alpha.png">
			</a>
		</li>
		<li>
			<a title="" target="_blank" href="http://www.supermap.com/cn/xhtml/SuperMap-iObjects-8C.html">
				<img width="1920" height="482" alt="" style="background: url(/SuperMap-iDesktop-Cross/img/banner1.jpg) no-repeat center;" src="/SuperMap-iDesktop-Cross/img/alpha.png">
			</a>
		</li>
	</ul>
	<ul class="flex-direction-nav">
		<li><a class="flex-prev" href="javascript:;">Previous</a></li>
		<li><a class="flex-next" href="javascript:;">Next</a></li>
	</ul>
	<ol id="bannerCtrl" class="flex-control-nav flex-control-paging">
		<li><a>1</a></li>
		<li><a>2</a></li>
		<li><a>3</a></li>
    </ol>


<div id="banner" class="wrapper">
  <div class="inner">
    <h2 id="banner-title"><b>{{ page.subtitle }}</b></h2>
    <!--div id="banner-start">
      <span id="banner-start-command"><b>跨平台、全开源、可定制的 GIS 平台</b></span>
    </div>
    <div id="intro-get-started-wrap">
      <a href="docs/" id="intro-get-started-link">{{ __('index.get_started') }}</a>
    </div-->
    <div id="banner-share">{{ partial('partial/share') }}</div>
  </div>
</div>
</div>
<div id="content-wrap">
  <div class="wrapper">
    <div class="inner">
      {{ page.content }}
      <div id="intro-get-started-wrap">
        <a href="docs/" id="intro-get-started-link">{{ __('index.get_started') }}</a>
      </div>
    </div>
  </div>
</div>
</div>