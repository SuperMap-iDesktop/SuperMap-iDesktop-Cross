{% if page.comments && config.duoshuo_shortname %}
  <section id="comments">
<!-- 多说评论框 start -->
<div id="ds-thread" class="ds-thread" data-thread-key="<%= post.path %>" data-title="<%= post.title %>" data-url="<%= post.permalink %>"></div>
<!-- 多说评论框 end -->
<!-- 多说公共JS代码 start (一个网页只需插入一次) -->
<script type="text/javascript">
var duoshuoQuery = {short_name:"qinlj"};
	(function() {
		var ds = document.createElement('script');
		ds.type = 'text/javascript';ds.async = true;
		ds.src = (document.location.protocol == 'https:' ? 'https:' : 'http:') + '//static.duoshuo.com/embed.js';
		ds.charset = 'UTF-8';
		(document.getElementsByTagName('head')[0] 
		 || document.getElementsByTagName('body')[0]).appendChild(ds);
	})();
</script>
<!-- 多说公共JS代码 end -->

  <!-- 分享框 -->
<div class="ds-share" data-thread-key="<%= post.path %>" data-title="<%= post.title %>" data-url="<%= post.permalink %>">
    <div class="ds-share-inline">
      <ul  class="ds-share-icons-16">
      	
      	<li data-toggle="ds-share-icons-more"><a class="ds-more" href="javascript:void(0);">分享到：</a></li>
        <li><a class="ds-weibo" href="javascript:void(0);" data-service="weibo">微博</a></li>
        <li><a class="ds-qzone" href="javascript:void(0);" data-service="qzone">QQ空间</a></li>
        <li><a class="ds-qqt" href="javascript:void(0);" data-service="qqt">腾讯微博</a></li>
        <li><a class="ds-wechat" href="javascript:void(0);" data-service="wechat">微信</a></li>
      	
      </ul>
      <div class="ds-share-icons-more">
      </div>
    </div>
 </div>
  </section>
{% endif %}



