{% if index && post.comments %}
  <section id="comments">
<!--高速版-->
<div id="SOHUCS"></div>
<script charset="utf-8" type="text/javascript" src="http://changyan.sohu.com/upload/changyan.js" ></script>
<script type="text/javascript">
    window.changyan.api.config({
        appid: 'cysmcwSf3',
        conf: 'prod_50e88b32072d8cb949c543dc84922c45'
    });
<div id="SOHUCS" sid="<%= page.title %>"></div>
<script>
  (function(){
    var appid = 'cysmcwSf3',
    conf = 'prod_50e88b32072d8cb949c543dc84922c45';
    var doc = document,
    s = doc.createElement('script'),
    h = doc.getElementsByTagName('head')[0] || doc.head || doc.documentElement;
    s.type = 'text/javascript';
    s.charset = 'utf-8';
    s.src =  'http://assets.changyan.sohu.com/upload/changyan.js?conf='+ conf +'&appid=' + appid;
    h.insertBefore(s,h.firstChild);
    window.SCS_NO_IFRAME = true;
  })()
</script>        
  </section>
{% endif %}
