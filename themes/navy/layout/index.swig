<header id="banner" class="wrapper">
  <div class="inner">
    <h2 id="banner-title">{{ page.subtitle }}</h2>
    <div id="banner-start">
      <span id="banner-start-command">跨平台、全开源、可定制 GIS 平台</span><a id="banner-start-link" href="docs/"><i class="fa fa-arrow-right"></i></a>
    </div>
    <div id="banner-share">{{ partial('partial/share') }}</div>
  </div>
</header>
<div id="content-wrap">
  <div class="wrapper">
    <div class="inner">
      {{ page.content }}
      <!--ul id="intro-cmd-wrap">
        <li class="intro-item">产品介绍</li>
        <li class="intro-item">数据管理</li>
        <li class="intro-item">地图制图</li>
        <li class="intro-item">数据处理</li>
        <li class="intro-item">场景浏览</li>
      </ul-->
      <div id="intro-get-started-wrap">
        <a href="docs/" id="intro-get-started-link">{{ __('index.get_started') }}</a>
      </div>
    </div>
  </div>
</div>