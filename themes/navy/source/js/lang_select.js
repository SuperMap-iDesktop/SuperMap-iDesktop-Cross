(function(){
  'use strict';

  function changeLang(){ 
    var lang = this.value;
	var newHref = '';
    var canonical = this.dataset.canonical;
    if (lang === 'zh'){ 
		newHref = '/SuperMap-iDesktop-Cross/'+canonical;
	}else{
		newHref = '/SuperMap-iDesktop-Cross/'+lang+'/'+ canonical;
	}
    location.href = newHref;
  }

  document.getElementById('lang-select').addEventListener('change', changeLang);
  document.getElementById('mobile-lang-select').addEventListener('change', changeLang);
})();