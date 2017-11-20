'use strict';

var pathFn = require('path');
var _ = require('lodash');
var url = require('url');
var cheerio = require('cheerio');
var lunr = require('lunr');

var localizedPath = ['docs', 'api'];

function startsWith(str, start){
  return str.substring(0, start.length) === start;
}

hexo.extend.helper.register('page_nav', function(){
  var type = this.page.canonical_path.split('/')[0];
  var sidebar = this.site.data.sidebar[type];
  // var path = pathFn.basename(this.path);
  var path = this.url_for(this.path);
//  console.log(path); 
  var list = {};
  var prefix = 'sidebar.' + type + '.';

  for (var i in sidebar){
    for (var j in sidebar[i]){
      list[sidebar[i][j]] = j;
    }
  }

  var keys = Object.keys(list);
  var index = keys.indexOf(path);
  var result = '';
  var href = '';
  if (index > 0){
	href = this.en_url(keys[index - 1]);
    result += '<a href="' + href + '" class="article-footer-prev" title="' + this.__(prefix + list[href]) + '">' +
      '<i class="fa fa-chevron-left"></i><span>' + this.__('page.prev') + '</span></a>';
  }

  if (index < keys.length - 1){
	href = this.en_url(keys[index + 1]);
    result += '<a href="' + href + '" class="article-footer-next" title="' + this.__(prefix + list[href]) + '">' +
      '<span>' + this.__('page.next') + '</span><i class="fa fa-chevron-right"></i></a>';
  }
  return result;
});

hexo.extend.helper.register('doc_sidebar', function(className){
  var type = this.page.canonical_path.split('/')[0];
  var sidebar = this.site.data.sidebar[type];
  var path = pathFn.basename(this.path);
  var result = '<ul class="topnav">';
  var self = this;
  var prefix = 'sidebar.' + type + '.';
  var lang = this.page.lang;
  
   
   _.each(sidebar, function(menu, title){
       result += '<li id="' + title + '"><strong class="' + className + '-title">' + self.__(prefix + title) + '</strong>';
       result += '<ul>'
       _.each(menu, function(submenu, subtitle){
           if(_.isString(submenu)){
               var itemClass = className + '-link';
               if (submenu === path) itemClass += ' current';
               result += '<li id="' + subtitle + '"><a href="' + submenu + '" class="' + itemClass + '">' + self.__(prefix + subtitle) + '</a></li>';
           } else{
               result += '<li id="' + subtitle + '"><strong class="' + className + '-subtitle">' + self.__(prefix + subtitle) + '</strong>';
               result += '<ul>'
               _.each(submenu, function(link, text){
                   var itemClass = className + '-link';
                   if (link === path) itemClass += ' current';
				   var href = link;
                   result += '<li id="' + text + '"><a href="' + link + '" class="' + itemClass + '">' + self.__(prefix + text) + '</a></li>';
           })
           result += '</ul></li>';
           }
       })
       result += '</ul></li>';
   })
    
    result += '</ul>';
  return result;
});

hexo.extend.helper.register("en_url",function Newhref(hrefpara) {
	var lang = this.page.lang;
	var href = '';
	if(lang=='en'){
		var serverletApp = 'SuperMap-iDesktop-Cross';
		var hrefpara1 = hrefpara.substr(serverletApp.length+1,hrefpara.length-1);
		href= '/'+serverletApp+'/en'+hrefpara1;
	}else{
		href = hrefpara;	
	}
	return href;
 }
);
hexo.extend.helper.register('header_menu', function(className){
  var menu = this.site.data.menu;
  var result = '';
  var self = this;
  var lang = this.page.lang;
  var isChinese = lang === 'zh';

  _.each(menu, function(path, title){
  //&& ~localizedPath.indexOf(title)
   if (!isChinese) path = lang + path;

    result += '<a href="' + self.url_for(path) + '" class="' + className + '-link">' + self.__('menu.' + title) + '</a>';
  });

  return result;
});

hexo.extend.helper.register('canonical_url', function(lang){
  var path = this.page.canonical_path;
  if (lang && lang !== 'zh') path =  path + '/' + lang ;

  return this.config.url + '/' + path;
});

hexo.extend.helper.register('url_for_lang', function(path){
  var lang = this.page.lang;
  var url = this.url_for(path);

  if (lang !== 'zh' && url[0] === '/') url = url + '/' + lang; 

  return url;
});

hexo.extend.helper.register('raw_link', function(path){
  return 'http://git.oschina.net/supermap/SuperMap-iDesktop-Cross-Docs/blob/master/source/' + path;
});

hexo.extend.helper.register('page_anchor', function(str){
  var $ = cheerio.load(str, {decodeEntities: false});
  var headings = $('h1, h2, h3, h4, h5, h6');

  if (!headings.length) return str;

  headings.each(function(){
    var id = $(this).attr('id');

    $(this)
      .addClass('article-heading')
      .append('<a class="article-anchor" href="#' + id + '" aria-hidden="true"></a>');
  });

  return $.html();
});

hexo.extend.helper.register('lunr_index', function(data){
  var index = lunr(function(){
    this.field('name', {boost: 10});
    this.field('tags', {boost: 50});
    this.field('description');
    this.ref('id');
  });

  _.sortBy(data, 'name').forEach(function(item, i){
    index.add(_.assign({id: i}, item));
  });

  return JSON.stringify(index.toJSON());
});

hexo.extend.helper.register('canonical_path_for_nav', function(){
  var path = this.page.canonical_path;

  if (startsWith(path, 'docs/') || startsWith(path, 'api/')){
    return path;
  } else {
    return '';
  }
});

hexo.extend.helper.register('lang_name', function(lang){
  var data = this.site.data.languages[lang];
  return data.name || data;
});

hexo.extend.helper.register('disqus_lang', function(){
  var lang = this.page.lang;
  var data = this.site.data.languages[lang];

  return data.disqus_lang || lang;
});


