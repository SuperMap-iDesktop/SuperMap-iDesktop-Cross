(function(){
  'use strict';

  function changeLang(){ 
    var lang = this.value;
	var newHref = '';
    var canonical = this.dataset.canonical;
    if (lang === 'zh'){ 
		newHref = '/SuperMap-iDesktop-Cross/'+canonical;
		if(null!=document.getElementById("banner1")){
			document.getElementById("banner1").style="background: url(/SuperMap-iDesktop-Cross/img/banner1.png) no-repeat center";
		}
		if(null!=document.getElementById("banner2")){
			document.getElementById("banner2").style="background: url(/SuperMap-iDesktop-Cross/img/banner2.png) no-repeat center";
		}
		if(null!=document.getElementById("banner3")){
			document.getElementById("banner3").style="background: url(/SuperMap-iDesktop-Cross/img/banner3.png) no-repeat center";
		}
	}else{
		newHref = '/SuperMap-iDesktop-Cross/'+lang+'/'+ canonical;
		if(null!=document.getElementById("banner1")){
			document.getElementById("banner1").style="background: url(/SuperMap-iDesktop-Cross/img/banner1_en.png) no-repeat center";
		}
		if(null!=document.getElementById("banner2")){
			document.getElementById("banner2").style="background: url(/SuperMap-iDesktop-Cross/img/banner2_en.png) no-repeat center";
		}
		if(null!=document.getElementById("banner3")){
			document.getElementById("banner3").style="background: url(/SuperMap-iDesktop-Cross/img/banner3_en.png) no-repeat center";
		}
	}
    location.href = newHref;
  }

  document.getElementById('lang-select').addEventListener('change', changeLang);
  document.getElementById('mobile-lang-select').addEventListener('change', changeLang);
})();