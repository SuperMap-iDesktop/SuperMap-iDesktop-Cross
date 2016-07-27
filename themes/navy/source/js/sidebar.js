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
 		var $this = $(this);
 		
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
			$this.find("li a").click(function () {
				function OpenNode() {
					var OpenNodeId = '';
					$this.find("li strong").each(function() {
						if ($(this).parent().find("ul:first").is(":visible") != 0) {
							if(OpenNodeId==''){
								OpenNodeId= $(this).parent().attr('id');
							}else {
								OpenNodeId +=',' + $(this).parent().attr('id');
							}
						}
					});
					return OpenNodeId;
				}

				var redhref = $(this).attr('href');
				redhref += '?'+OpenNode();
				$(this).attr('href',redhref);
			});

			var UrlParam = window.location.search.substr(1);
			var UrlParamArray = UrlParam.split(',');
			for (var i= 0;i < UrlParamArray.length;i++) {
				var nodeid = document.getElementById(UrlParamArray[i]);
				var nodeidjQuery = $(nodeid);
				nodeidjQuery.find("ul:first").show();
				nodeidjQuery.find("span:first").html(opts.openedSign);
			}
		}
		sidebarUnfold();

		function sidebarHighlight() {
			var currenthref = window.location.href;
			var hrefparams = currenthref.split("/");
			var hrefparam1 = hrefparams[hrefparams.length-1];
			var hrefparam2 = hrefparam1.split(".");
			var filename = hrefparam2[hrefparam2.length-2];
			$this.find("li").each(function () {
				if ($(this).attr('id') == filename) {
					$(this).find("a").css('color','#33a3dc');
				}
			});
		}
		sidebarHighlight();

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
});
