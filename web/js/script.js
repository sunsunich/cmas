
/*REDESIGN MIATT*/
var footerDiv = document.getElementById('footer-menus');
var glassDiv = document.getElementById('glass-box-content');
glassDiv.appendChild(footerDiv);


/**
    Szövegmezők alapértelmezett értékének (smartlabel) ki-be kapcsolása
    @tfield - HTMLFormObject - a lekezelt szövegmező
    @op - String - kikapcsolásnál "hide", bekapcsolásnál "show"
    @deftext - String - a smartlabel szövege
    @return: true
*/
function handleTextField(tfield, op, deftext){
    switch(op){
            case 'show' : if(tfield.value == '')
                        tfield.value=deftext;
            break;
            
            case 'hide' : if(tfield.value == deftext)
                        tfield.value='';
            break;
    }
    return true;
}

/**
	Navigációs "ál-selectek" ki-be nyitogatása
	@menuId - String - legordülő UL ID-je (JQuery konstruktornak adjuk át)
	függőség: JQuery 1.4
*/
function toggleSelectMenu(menuId){
	select = $('#'+menuId);
	if(select.css('display')!=='block'){
				select.slideDown(250);
				$('body#cmas-site').bind(
					"click",function(){select.slideUp(250)}
				)
	}
	else{
		select.slideUp(250);
		$('body#cmas-site').unbind("click");
	}
	return false;
}

function initSliderMain(){
	slidesNum = $('div#sports-slideshow .slide').length;
	if(slidesNum > 0 && $('div#sports-slideshow').length > 0){
		$('div#sports-slideshow').after('<div id="sports-slideshow-pager">')
        $('div#sports-slideshow').cycle({
			fx: 'fade',
            timeout: 7000,
            speed: 500,
            pager: 'div#sports-slideshow-pager',
            pause: 1
        });
	}
}

/*Tabchanger*/
function showTab(argMaxIndex, argActualIndex){
	var linksObj = document.getElementById('tabChangers');
	var linkObj = false;
	var display =  'none';
	var blockObj;

	for (i=1;i<=argMaxIndex;i++){
		linkObj = document.getElementById('tabChange'+i);
		blockObj = document.getElementById('tab'+i);
		blockObj.style.display = 'none';
		linkObj.className = 'inactive';
	}

	linkObj = document.getElementById('tabChange'+argActualIndex);
	linkObj.className = 'active';

	blockObj = document.getElementById('tab'+argActualIndex);
	blockObj.style.display = 'block';

	linksObj.style.display = 'block';
}

/**
	Megmutatja a versenyeredményeket
	@argRow			Object		Az adott sor objektuma
	@argDivId		string		Div ID, ahová be kell tölteni
	@argURL			string		URL, ahonnan be lehet őket tölteni
*/
function showChampionshipResults(argRow, argDivId, argURL)
{
	var div = document.getElementById('ch'+argDivId);
	argRow.className = 'galleriesrow highlight';
	
	if (div!=null){
		if (div.style.display!='block'){
			$(div).slideDown(300);
			div.innerHTML = '<img src="i/ajax-loader.gif" />';
			setTimeout(function(){loadChampionshipResults(argDivId,argURL)},
					   100);		
		}
	}
}
/**
	Betölti a versenyeredményeket
	@argDivId		string		Div ID, ahová be kell tölteni
	@argURL			string		URL, ahonnan be lehet őket tölteni
*/
function loadChampionshipResults(argDivId, argURL)
{
	var div = document.getElementById('ch'+argDivId);
	if (div!=null){
		var html = ajax_load(argURL+'&getResults=1');
		div.innerHTML = html;
	}
}

function ajax_load(url)
{
	var http_request=null;
	
	var d = new Date();
	//url+='&t='+d.getTime();
	//létrehozzuk az objektumot
	if (window.XMLHttpRequest) {
	    //Mozilla és minden más értelmes böngésző
	    http_request = new XMLHttpRequest();
	    if (http_request.overrideMimeType) http_request.overrideMimeType('text/html');
	} else if (window.ActiveXObject) {
	    //hülye IE
            try {
                http_request = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                try {
                    http_request = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (e) {}
            }

	}
	if(!http_request) {	document.cursor='default';return('ERROR');}
	//lekérjük a megadott urlt, és visszaadjuk a tartalmát
	http_request.open('GET', url, false);
	http_request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); 
	http_request.send(null);
	document.cursor='default';
	return(http_request.status==200?http_request.responseText:'');
}
/**
 * A standard-ek és standard csoportok kezeléséhez
 */
function standards_selectGroup(argSelect)
{
    var value = argSelect.options[argSelect.selectedIndex].value;
    document.getElementById('standard-1').style.display = 'none';
    document.getElementById('standard-2').style.display = 'none';
    document.getElementById('standard-3').style.display = 'none';
    document.getElementById('standard-4').style.display = 'none';
    if (value){
	document.getElementById(value).style.display = 'block';
    }
}
function standards_selectProgramme(argSelect)
{
    var value = argSelect.options[argSelect.selectedIndex].value;
    var objButton = document.getElementById('standard-button');
    if (value) objButton.setAttribute('class', 'btn');
    else objButton.setAttribute('class', 'btn opacity5');
    objButton.setAttribute('data-url', value);
}
function standard_go()
{
    var url = document.getElementById('standard-button').getAttribute('data-url');
    if (url) window.location.href = url;
}

/**
 * Betölt további híreket
 * @argPageId			int		Oldal (rovat) azonosítója
 * @argLink			Object	Link objektum
*/
function moreNews(argPageId, argLink)
{
	var container = document.getElementById('pages');
	var linkText = argLink.innerHTML;
	var start = 4;
	
	if (argLink.getAttribute("data-loading") == "1") return;

	argLink.innerHTML = '<img src="images/ajax-loader.gif" />';
	argLink.setAttribute("data-loading","1");

	client = new HttpClient();
    client.requestType = 'POST';
    client.callback = function(){};
    result = client.makeRequest('more-news', 'loadpages=1&start='+start+'&limit=100&parentPage='+argPageId, null);
//alert(result);
	eval('result = '+result);
	
	for (i=0;i<result.pages.length;i++){
		page = result.pages[i];
		if (argWithNumbers) page.name = '<b>'+(start+1+i)+'</b>. - '+page.name;
		if (i==0) html = '<div class="article-lead newpage">';
		else  html = '<div class="article-lead">';
        html+= '<h2 class="header"><a href="'+page.fileName+'">'+page.name+'</a></h2>';
        html+= '<div class="article-info">';
        html+= '</div>';
		html+= '<p>'+page.lead+'</p>';
		html+= '</div>';
		container.innerHTML+= html;
	}
	if (result.pages.length==10){
		argLink.innerHTML = linkText;
		argLink.style.backgroundColor = '#eaeaea';
	}else{
		argLink.style.display = 'none';
	}
	argLink.setAttribute("data-loading","0");
}
/**
	Láthatóvá teszi és betölti egy tag részleteit
	@argId		int			Tag azonosító
	@argObjRow	object		A sor, amire kattintottunk
*/
var loading = false;
function ShowCDCDetails(argId,argObjRow, argLanguage)
{
    if (loading==true) return false;
    loading = true;
    
    if (!argLanguage) argLanguage = 1;
    
    var obj = document.getElementById('fed'+argId)
	    
    if (!obj) return false;
    if (obj.style.display == 'block'){
	obj.style.display = 'none';
	loading = false;
	if (argObjRow) argObjRow.className = argObjRow.className.replace('active','');
    }else{
	obj.style.display = 'block';
	if (argObjRow) argObjRow.className+=' active';
	obj.style.display = 'block';
	obj.innerHTML = '<img src="i/document_tree_loader.gif" class=\"preloader\">';

	client = new HttpClient();
        client.requestType = 'POST';
        client.callback = function(){};
        obj.innerHTML = client.makeRequest('diving-center-details', 'id='+argId+'&language='+argLanguage, null);
	loading = false;
    }
}

/**
	HTTP Client
*/
function HttpClient(){}
HttpClient.prototype = {
 requestType: 'GET',
 isAsync: false,
 xmlhttp: false,
 callback: false,
 loadMess: '',
 sendMess: '',
/**
 * Az ajax hivás elküldésekor végrehajtandó tevékenység
 * @argSendMessage                string                                   a küldéskori üzenet
 * @argObj                        object                                   az az objektum, ahova majd a végeredmény kerül
 */
 onSend: function(argSendMessage, argObj){if( argObj ) argObj.innerHTML = argSendMessage;},

/**
 * Az ajax hivás betöltődésekor végrehajtandó tevékenység
 * @argLoadMessage                string                                   a betöltéskori üzenet
 * @argObj                        object                                   az az objektum, ahova majd a végeredmény kerül
 */
 onLoad: function(argLoadMessage, argObj){if( argObj ) argObj.innerHTML = argLoadMessage;},

/**
 * Hiba esetén vegrehajtandó tevékenység
 * @argError                      string                                   Hibaüzenet
 */
 onError: function(argError){
   alert(argError);
 },
 /**
   Az ajax hívás inicializálása
 */
 init: function(){
  try{
   this.xmlhttp=new XMLHttpRequest();
  }
  catch(e){
   var xmlhttp_ids = new Array('MSXML2.XMLHTTP.5.0',
                               'MSXML2.XMLHTTP.4.0',
                               'MSXML2.XMLHTTP.3.0',
                               'MSXML2.XMLHTTP',
                               'Microsoft.XMLHTTP');
   var success = false;
   for(var i = 0;i < xmlhttp_ids.length && !success; i++){
    try{
     this.xmlhttp = new ActiveXObject(xmlhttp_ids[i]);
     success = true;
    }
    catch (e){}
   }
   if( !success ){
    this.onError('Nem lehet XMLHttpRequest hívást kezdeményezni.');
   }
  }
 },

/**
 * Az ajax hivás kezdeményezése
 * @argUrl                        string                                   a fogadó script url-je, ahol a feldolgozás történik
 * @argPayload                    string                                   ha a request típusa post, akkor ez tartalmazza a post paramétereket
 * @argObj                        object                                   az az objektum, ahova a végeredmény kerül
 * @argIsXML                      bool                                     a végeredmény XML, vagy text típusú
 * @return                        string                                   ha nem aszinkron volt a hívás, akkor az eredmény itt tárolódik
 */
 makeRequest: function(argUrl, argPayload, argObj, argIsXML){
  if( !this.xmlhttp ){this.init();}
  this.xmlhttp.open(this.requestType, argUrl, this.isAsync);
  if( this.requestType == 'POST' ){this.xmlhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded;charset=UTF-8');}
  if( this.isAsync ) this.xmlhttp.onreadystatechange = function(){self._readyStateChangeCallback(argObj, argIsXML);}
  var self = this;
  this.xmlhttp.send(argPayload);
  if( !this.isAsync ){return (argIsXML?this.xmlhttp.responseXML:this.xmlhttp.responseText);}
  return true;
 },

/**
 * Az aszinkron ajax hívás visszatérésének lekezelése
 * @argObj                        string                                   az az objektum, ahova a végeredmény kerül
 * @argIsXML                      bool                                     a végeredmény XML, vagy text típusú
 */
 _readyStateChangeCallback: function(argObj, argIsXML){
  switch(this.xmlhttp.readyState){
   case 0:
   case 1:
   case 2:
    this.onSend(this.sendMess, argObj);
   break;
   case 4:
    this.onLoad(this.loadMess, argObj);
    if( this.xmlhttp.status == 200 ) this.callback( (argIsXML?this.xmlhttp.responseXML:this.xmlhttp.responseText));
    else if( this.xmlhttp.status != 0 ) this.onError('HTTP hiba történt a lekérdezéskor: ['+this.xmlhttp.status+'] '+this.xmlhttp.statusText);
   break;
  }
 }
}
/*--- DIVING CENTERS MAP ------*/
/**
	Betölti a diving centers oldal
	@argContinent	string	Kontinens kódja
*/
function DivingCenters(argContinent)
{
	document.location.href = divingCentersURL+'?continent='+argContinent;  
}
/**
	Bekapcsolja a megadott kontinenst
	@argContinent	string	Kontinens neve
*/ 
function ShowContinentOnMap(argContinent)
{
	var obj=document.getElementById('continent');
	if (argContinent!='') obj.className='continent_'+argContinent;
	else obj.className='continent_off';
}

$(document).ready(function(e){
	
	//call the initializer of the main page slideshow
	initSliderMain();
	
	/*focus effect for main search box*/
	$('input.search-query').focus(function(e){
			$(this).parents('div.search-box').addClass('focused');
		})
	$('input.search-query').blur(function(e){
			$(this).parents('div.search-box').removeClass('focused');
		})
	
	/*gallery filter Event handler*/
	$('a#gallery-filter-opener').click(function(e){
			toggleSelectMenu('gallery-select');
			e.stopPropagation();
		})
	
	/*members are loged in Event handler*/
	$('a#login-opener').click(function(e){
			toggleSelectMenu('login-select');
			e.stopPropagation();
		})
	
	/*:hover and :focus JS patch for older IE versions*/
	if($.browser.msie && $.browser.version.substring(0,1)<8){
		$('.jsfocus').focus(
			function(){$(this).addClass('focused')}
		)
		$('.jsfocus').blur(
			function(){$(this).removeClass('focused')}
		)
	}
	if($.browser.msie && $.browser.version.substring(0,1)<8){
		$('.jshover').mouseenter(
			function(){$(this).addClass('hovered')}
		)
		$('.jshover').mouseleave(
			function(){$(this).removeClass('hovered')}
		)
	}
	
	/*setting fixed position to the filters and icon row on event calendar pages*/
	if($('#filter-navigation').length){
		var filterBar = $('#filter-navigation');
		var filterBarTop = filterBar.offset().top;
		$(window).bind(
			'scroll',
			function(){
				if($(window).scrollTop() > filterBarTop){				
					filterBar.addClass('fixed-nav');
				}else{
					filterBar.removeClass('fixed-nav');
				}
			}
		)
	}
	
	/*tooltip for the calendar file download icon, DEVNOTE: if further JS tooltips are requred than this should be rewritten to a reusable function with parameters, now it only refers to one elemnt*/
	if($('#event-call-dn').length){
	    var tooltip;
	    var linkItem = $('#event-call-dn');
	    var tooltipText = linkItem.attr('title');
	    linkItem.attr('title','');
	    var tooltipTopPos = linkItem.offset().top + 17+'px';
	    var tooltipLeftPos = linkItem.offset().left + 'px';
	    
	    linkItem.bind(
		'mouseenter',
		function(){
		    $('<div id="tooltip"><div class="tooltip-pointer"></div><div class="tooltip-content">'+tooltipText+'</div></div>').appendTo($('#cmas-site'));
		    tooltip = $('#tooltip');
		    tooltip.css({'left':tooltipLeftPos,'top':tooltipTopPos});
		    tooltip.fadeIn(150);
		}
	    );
	    
	    linkItem.bind(
		'mouseleave',
		function(){
		    tooltip.fadeOut(150,function(){tooltip.remove();});
		}
	    );
	}
	
	/*records page*/
	initRecordsAccordion();
})

/**
 * Élesíti a rekordok accrodiont. Csak a rekordok oldalakon használjuk
*/

function initRecordsAccordion()
{
	//tab chang functionality for tabsets in the Records page accordion
	//right now the stylesheet only support 2 tabs next to each other, both this script can handle n number of tabs
	//the tablinks must be in the same order in the HTML as the tabs!
	var recordPageAccordionHeaders = $('.records-row-head');

	//accordion functionality on the records page
	if (recordPageAccordionHeaders.length) {
		recordPageAccordionHeaders.bind('click', function(e){
			var actAccHead, actAccordion, parentRow, openAccordions, openAccordionHead, openAccordionCont, openClassName;
			actAccHead = $(this);//the clicked row
			actAccordion = actAccHead.siblings('.record-tables-container'); //the content to show below the clicked row
			parentRow = actAccHead.parent('.records-row'); //the parent LI of the clicked row
			openAccordRow = parentRow.siblings('.opened'); //any other LI in the accordion that is currently open (there can alway be only 1)
			openAccordionHead = openAccordRow.find('.records-row-head'); //the header of the other accordion that is opened
			openAccordionCont = openAccordRow.find('.record-tables-container'); //the content of the other accordion that is opened
			openClassName = 'opened';
			//helper for closing an accordion
			//used on both the click event of an opened accordion
			//and on the 'Back' button click
			function closeAccordion() {
				actAccordion.slideUp(300, function(){
					actAccHead.removeClass(openClassName);
					parentRow.removeClass(openClassName);
					//reset the hash in the URL
					document.location.hash = '';
					//remove the hashchange event, of course only if the browsers supports it
					if ('onhashchange' in window) {
					$(window).unbind('onhashchange');
					};
				});
			};
			
			//if it's not opened yet
			if (!actAccHead.hasClass(openClassName)) {
				//close all others - if there ara any (the accordion starts in a state where all items are closed)
				if (openAccordRow.length) {
					openAccordionCont.slideUp(300, function(){
						openAccordRow.removeClass(openClassName);
						openAccordionHead.removeClass(openClassName);				    
					});
				};
				
				//open it and add the classes
				actAccordion.slideDown(300, function(){
					jQuery('html,body').animate({scrollTop: parentRow.offset().top}, 250);
					parentRow.addClass(openClassName);
					actAccHead.addClass(openClassName);
					//add the perent LIs ID as hash to the URL for hotlinking
					document.location.hash = parentRow.attr('id');
					//Close the panel if the user pushes 'Back' only works in modern browsers
					if ('onhashchange' in window) {
						$(window).bind('hashchange', function(event){
							if (document.location.hash.indexOf('records-') < 0) {
							closeAccordion();
							}
						});
					};
					});
				}else{
				//when the accordion item is in opened state it should be able to close itself
				closeAccordion();
			};
			
			//adatok betöltése
			var container = $(this.parentNode).find('.record-tables-container');
			container.empty();
			container.load(this.getAttribute('data-source'), function(){
				var recordPageTabLinks = container.find('.records-tab-changer');
				recordPageTabLinks.bind('click', function(e){
					var actTab, otherTabs, allTabs, tabDivs, actTabDiv, activeClassName;
					actTab = $(this); //the clicked tab-chnager
					otherTabs = actTab.siblings('.records-tab-changer'); //all other tab changers
					tabDivs = actTab.parents('.records-tabset').find('.records-tab'); //all the tabs
					allTabs = actTab.add(otherTabs); //all the tabs - wee need this for looping through them all to find the newly activated tab
					activeClassName = 'active';
					
					//remove all 'active' classes from the other elements
					otherTabs.removeClass(activeClassName);
					//add the 'active' class to the clicked tab
					actTab.addClass(activeClassName);
					//hide all the tab DIVs
					tabDivs.removeClass(activeClassName);
					//iterate thourgh the tab links find the one just activated and activate the tab DIV with the same index
					allTabs.each(function(i){
						if ($(this).hasClass(activeClassName)) {
							$(tabDivs[i]).addClass(activeClassName);
						};
					});
					e.preventDefault();
				});
			});
			e.stopPropagation();
		});
		
		//hot linking functionality
		//make absulatelly sure that the hash link is an ID of an accordion element
		//and not some other hash link on the page
		if (document.location.hash && document.location.hash.indexOf('records-') > 0) {
		var accRow, accRowHead, accRowCont;
		accRow = $(document.location.hash);
		if (accRow.length) {
			accRowHead = accRow.find('.records-row-head');
			accRowHead.trigger('click');
		};
		};
	};
	
}
