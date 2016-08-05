/*
 * jQuery UI Multilevel Accordion v.1
 * 
 * Copyright (c) 2011 Pieter Pareit
 *
 * http://www.scriptbreaker.com
 *
 */

//plugin definition
(function($){
    $.fn.extend({

    //pass the options variable to the function
    accordion: function(options) {
        
		var defaults = {
			accordion: 'true',
			speed: 300,
			closedSign: '&nbsp;&nbsp;<i class="fa fa-chevron-circle-right"></i>',
			openedSign: '&nbsp;&nbsp;<i class="fa fa-chevron-circle-down"></i>'
		};
		// Extend our default options with those provided.
		var opts = $.extend(defaults, options);
		//Assign current element to variable, in this case is UL element
 		$this = $(this);
 		
 		//add a mark [+] to a multilevel menu
 		$this.find("li").each(function() {
 			if($(this).find("ul").size() != 0){
 				//add the multilevel sign next to the link
 				$(this).find("strong:first").append("<span>"+ opts.closedSign +"</span>");
 				
 				//avoid jumping to the top of the page when the class is an sidebar-title
 				if($(this).find("strong:first").attr('class') == "sidebar-title"){
 		  			$(this).find("strong:first").click(function(){return false;});
 		  		}
 			}
 		});

 		//open active level
 		$this.find("li.active").each(function() {
 			$(this).parents("ul").slideDown(opts.speed);
 			$(this).parents("ul").parent("li").find("span:first").html(opts.openedSign);
 		});
        
  		$this.find("li strong").click(function() {
  			if($(this).parent().find("ul").size() != 0){
  				if(opts.accordion){
  					//Do nothing when the list is open
  					if(!$(this).parent().find("ul").is(':visible')){
  						parents = $(this).parent().parents("ul");
  						visible = $this.find("ul:visible");
  						visible.each(function(visibleIndex){
  							var close = true;
  							parents.each(function(parentIndex){
  								if(parents[parentIndex] == visible[visibleIndex]){
  									close = false;
  									return false;
  								}
  							});
  							if(close){
  								if($(this).parent().find("ul") != visible[visibleIndex]){
  									$(visible[visibleIndex]).slideUp(opts.speed, function(){
  										$(this).parent("li").find("span:first").html(opts.closedSign);
  									});
  									
  								}
  							}
  						});
  					}
  				}
  				if($(this).parent().find("ul:first").is(":visible")){
  					$(this).parent().find("ul:first").slideUp(opts.speed, function(){
  						$(this).parent("li").find("span:first").delay(opts.speed).html(opts.closedSign);
  					});
  					
  					
  				}else{
  					$(this).parent().find("ul:first").slideDown(opts.speed, function(){
  						$(this).parent("li").find("span:first").delay(opts.speed).html(opts.openedSign);
  					});
  				}
  			}
  		});

		function sidebarUnfold() {
			$this.find("a").click(function () {

				//获取点击<a>标签时，展开目录节点的id
				function OpenNode() {
					OpenNodeId = '';
					$this.find("li strong").each(function () {
						if ($(this).parent().find("ul:first").is(":visible") != 0) {
							if (OpenNodeId == '') {
								OpenNodeId = $(this).parent().attr('id');
							} else {
								OpenNodeId += ',' + $(this).parent().attr('id');
							}
						}
					});
					return OpenNodeId;
				}

				function Dochref(hrefpara) {
					var isDochref = false;
					var hrefpara1 = hrefpara.split("/");
					var hrefpara2 = hrefpara1[hrefpara1.length - 1];
					var hrefpara3 = hrefpara2.split(".");
					var dochref = hrefpara3[hrefpara3.length - 2];
					$this.find("li").each(function () {
						isDochref = $(this).attr('id') == dochref;
						return !isDochref;
					});

					return isDochref;
				}

				var redhref = $(this).attr('href');
				if (Dochref(redhref) == true) {
					redhref += '?' + OpenNode();
					$(this).attr('href', redhref);
				}
			});
		}
		//刷新目录树，展开地址栏查询参数出记录的目录。
		var UrlParam = window.location.search.substr(1);
		var UrlParamArray = UrlParam.split(',');
		for (var i = 0; i < UrlParamArray.length; i++) {
			var nodeid = document.getElementById(UrlParamArray[i]);
			var nodeidjQuery = $(nodeid);
			nodeidjQuery.find("ul:first").show();
			nodeidjQuery.find("span:first").html(opts.openedSign);
		}
		sidebarUnfold();

		//当前页面对应目录高亮显示。
		function sidebarHighlight() {
			var currenthref = window.location.href;
			var hrefparams = currenthref.split("/");
			var hrefparam1 = hrefparams[hrefparams.length-1];
			var hrefparam2 = hrefparam1.split(".");
			var filename = hrefparam2[hrefparam2.length-2];
			$this.find("li").each(function () {
				if ($(this).attr('id') == filename) {
					if ($(this).parent().parent().find("ul:first").is(":visible") == 0){
						$(this).parent().parent().find("ul:first").show()
					}
					$(this).find("a").css('color','#33a3dc');
				}
			});
		}
		sidebarHighlight();
	},
		expand: function () {
			function articleclick() {
				$this.find("a").click(function () {
					/*function OpenNode() {
					 $this.find("li strong").each(function () {
					 if ($(this).parent().find("ul:first").is(":visible") != 0) {
					 if (OpenNodeId == '') {
					 OpenNodeId = $(this).parent().attr('id');
					 } else {
					 OpenNodeId += ',' + $(this).parent().attr('id');
					 }
					 }
					 });
					 return OpenNodeId;
					 }*/

					function Dochref(hrefpara) {
						var isDochref = false;
						var hrefpara1 = hrefpara.split("/");
						var hrefpara2 = hrefpara1[hrefpara1.length - 1];
						var hrefpara3 = hrefpara2.split(".");
						var dochref = hrefpara3[hrefpara3.length - 2];
						$this.find("li").each(function () {
							isDochref = $(this).attr('id') == dochref;
							return !isDochref;
						});

						return isDochref;
					}

					var redhref = $(this).attr('href');
					if (Dochref(redhref) == true) {
						redhref += '?' + OpenNode();
						$(this).attr('href', redhref);
					}
				});
			}

			articleclick();
		}

	});

})(jQuery);

$(document).ready(function() {
	$(".topnav").accordion({
		accordion:false,
		speed: 500,
		closedSign: '&nbsp;&nbsp;<i class="fa fa-chevron-circle-right"></i>',
		openedSign: '&nbsp;&nbsp;<i class="fa fa-chevron-circle-down"></i>'
	});
	/*$(".article").accordion({
		accordion:false,
		speed: 500,
		closedSign: '&nbsp;&nbsp;<i class="fa fa-chevron-circle-right"></i>',
		openedSign: '&nbsp;&nbsp;<i class="fa fa-chevron-circle-down"></i>'
	});*/
});

