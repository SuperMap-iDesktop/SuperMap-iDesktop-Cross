<div id="banner-wrapper">
<div id="banner" class="wrapper">
  <div class="inner">
    <h2 id="banner-title">{{ page.subtitle }}</h2>
    <div id="banner-start">
      <span id="banner-start-command"><b>跨平台、全开源、可定制的 GIS 平台</b></span>
    </div>
    <div id="intro-get-started-wrap">
      <a href="docs/" id="intro-get-started-link">{{ __('index.get_started') }}</a>
    </div>
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