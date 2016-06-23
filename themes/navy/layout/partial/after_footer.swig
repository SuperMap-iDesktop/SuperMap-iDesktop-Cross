<!-- Scripts -->
<!-- build:js build/js/main.js -->
{{ js('js/lang_select') }}
{{ js('js/toc') }}
{{ js('js/mobile_nav') }}
{{ js('js/jquery-1.10.2.min') }}
{{ js('js/slider') }}
<!-- endbuild -->
<script src="https://cdn.jsdelivr.net/retinajs/1.3.0/retina.min.js" async></script>
{% if page.layout === 'plugins' %}
<!-- Plugin search -->
<script src="https://cdn.jsdelivr.net/lunr/0.6.0/lunr.min.js"></script>
<!-- build:js build/js/plugins.js -->
{{ js('js/plugins') }}
<!-- endbuild -->
{% endif %}
<!-- Algolia -->
{% if config.algolia %}
<script type="text/javascript" src="https://cdn.jsdelivr.net/docsearch.js/1/docsearch.min.js"></script>
<script type="text/javascript">
document.getElementById('search-input-wrap').classList.add('on');
var host=window.location.host;
if(host=='{{ config.host.github }}')
{
  docsearch({
    apiKey: '{{ config.algolia['github'].api_key }}',
    indexName: '{{ config.algolia['github'].index_name }}',
    inputSelector: '#search-input'
  });
}
else if(host=='{{ config.host.support }}')
{
  docsearch({
    apiKey: '{{ config.algolia['support'].api_key }}',
    indexName: '{{ config.algolia['support'].index_name }}',
    inputSelector: '#search-input'
  });
}
</script>
{% endif %}
