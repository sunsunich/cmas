<%-- Базовое обрамление для любых страниц сайта --%>
<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="myfun" uri="/WEB-INF/tld/myfun" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="lightHeader" required="false" %>
<%@ attribute name="indexpage" required="false" %>
<%@ attribute name="customScripts" required="false" %>
<%@ attribute name="customCSSFiles" required="false" %>
<%@ attribute name="doNotDoAuth" required="false" %>

<!DOCTYPE html>
<html lang="en" class="english">
<head>
    <c:set var="url">${pageContext.request.requestURL}</c:set>
    <base href="${fn:substring(url, 0, fn:length(url) - fn:length(pageContext.request.requestURI))}${pageContext.request.contextPath}/" />

    <meta charset="utf-8"/>
    <title>${title}</title>
    <meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/>
    <meta name="description"
          content="In addition to organizing international underwater sport events it is at the forefront of technical and scientific research and development. It can be associated with elaborating one of the oldest and most extensive dive training system."/>
    <meta name="distribution" content="local"/>
    <meta name="keywords" content="cmas, diving, finswimming, apnea, sport diving, sport, underwater"/>
    <meta name="copyright" content="All rights reserved"/>
    <meta name="generator" content="Eplos CMS 3.0"/>
    <meta name="robots" content="index, follow"/>
    <base href="/"/>
    <meta itemprop="name" content="World Underwater Federation - Quality in diving"/>
    <meta itemprop="description"
          content="In addition to organizing international underwater sport events it is at the forefront of technical and scientific research and development. It can be associated with elaborating one of the oldest and most extensive dive training system."/>
    <meta property="og:title" content="World Underwater Federation - Quality in diving"/>
    <meta property="og:image" content="php_images/jutland_2016_banner_v3-450x150.jpg"/>
    <link rel="shortcut icon" type="image/x-icon" href="i/favicon.ico"/>
    <link rel="apple-touch-icon" href="i/apple-touch-icon.png">
    <link rel="stylesheet" type="text/css" href="c/styles.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/styles-redesign.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/print.css" media="print"/>
    <meta name="google-site-verification" content="Xaw1kGm7_ZeykPyMuWwVpY8t9_tOTAkWEaKu6aOv6i0"/>
    <meta name="creator" content="dotindot">
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <![endif]-->
    <!--[if lt IE 7]>
    <link rel="stylesheet" type="text/css" href="c/ie6.css" media="screen"/>
    <![endif]-->
    <!--[if lte IE 7]>
    <link rel="stylesheet" type="text/css" href="c/ie6-7.css" media="screen"/>
    <![endif]-->
    <!--[if lt IE 9]>
    <link rel="stylesheet" type="text/css" href="c/ie8.css" media="screen"/>
    <![endif]-->
    <script type="text/javascript">
        function PrintMail(argName) {
            document.write('<A href="mailto:' + argName + '@cmas.org">' + argName + '@cmas.org</A>');
        }
    </script>

    <script type="text/javascript" src="js/lib/json.js"></script>
    <script type="text/javascript" src="js/lib/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="js/lib/jquery.ba-hashchange.min.js"></script>
    <script type="text/javascript" src="js/lib/ejs_production.js"></script>

    <script type="text/javascript" src="js/controller/cookie_controller.js"></script>
    <script type="text/javascript" src="js/controller/loader_controller.js"></script>
    <script type="text/javascript" src="js/model/error_codes.js"></script>

    <c:forEach items="${customCSSFiles}" var="customCSSFile">
        <link type="text/css" rel="stylesheet" href="${customCSSFile}"/>
    </c:forEach>

    <c:forEach items="${customScripts}" var="customScript">
        <script type="text/javascript" src="${customScript}"></script>
    </c:forEach>

</head>
<body id="cmas-site">

<c:if test="${doNotDoAuth == null || !doNotDoAuth}">
    <my:authorize/>
</c:if>

<!--begin sitewrapper-->
<div id="main-wrapper">
    <!--end top-->
    <!--begin glass box-->
    <div id="glass-box">
        <div id="glass-box-top"></div>
        <div id="glass-box-content">
            <div class="glass-box-content-inner clearfix" id="glass-box-content-inner">
                <!--top white box for logo and menu-->
                <div class="white-box full mb10" id="head">
                    <div class="white-box-top"></div>
                    <div class="white-box-middle clearfix">
                        <h1>
                            <a href="http://www.cmas.org/en" class="logo" title="CMAS.org back to homepage">CMAS - World
                                Underwater Federation</a>
                        </h1>

                        <div id="top">
                            <div class="search-box">
                                <form id="search-form" action="http://www.cmas.org/search" method="get">
                                    <input type="text" class="search-query" value="Search on website..." id="txbQuery"
                                           name="query" onfocus="handleTextField(this,'hide','Search on website...')"
                                           onblur="handleTextField(this,'show','Search on website...')"/>
                                    <input type="button" value="Go" class="search-btn" id="searchSubmit"/>
                                </form>
                            </div>
                            <ul class="social-media-sites">
                                <li>
                                    <a href="http://www.youtube.com/cmasdiverline" class="yt" target="_blank"
                                       title="CMAS YouTube Channel">CMAS YouTube Channel</a>
                                </li>
                                <li>
                                    <a href="http://www.facebook.com/cmasdiverline" class="fb" target="_blank"
                                       title="CMAS Offical Facebook page">CMAS Offical Facebook page</a>
                                </li>
                            </ul>
                        </div>

                        <!--main menu-->
                        <ul id="main-menu">
                            <li class="toplevel first jshover" id="about-cmas">
                                <a href="javascript:void(0)" class="toplevel" title="About CMAS">About CMAS</a>
                                <ul class="submenu" id="about-cmas-submenu">
                                    <li class="submenu-top">&nbsp;</li>
                                    <li class="submenu-middle clearfix">

                                        <a href="http://www.cmas.org/cmas/about">
                                            The World Underwater Federation
                                        </a>
                                        <a href="http://www.cmas.org/cmas/the-board-of-directors">
                                            The Board of Directors
                                        </a>
                                        <a href="http://www.cmas.org/cmas/federations" class="last">
                                            Federations
                                        </a>

                                    </li>
                                    <li class="submenu-bottom">&nbsp;</li>
                                </ul>
                            </li>
                            <li class="toplevel jshover" id="commissions">
                                <a href="javascript:void(0);" class="toplevel" title="Commissions">Commissions</a>
                                <!--commissions submenu-->
                                <ul class="submenu" id="commissions-submenu">
                                    <li class="submenu-top">&nbsp;</li>
                                    <li class="submenu-middle clearfix">
                                        <ul class="submenu-column w105">
                                            <li class="header-cont">
                                                <h2>

                                                    <a href="http://www.cmas.org/sport"
                                                       title="Sport Committee">Sport<br/> Committee</a>
                                                </h2>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/apnoea">Apnoea</a>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/aquathlon">Aquathlon</a>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/finswimming">Finswimming</a>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/hockey">Underwater Hockey</a>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/orienteering">Orienteering</a>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/underwater-rugby">Underwater Rugby</a>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/spearfishing">Spearfishing</a>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/sport-diving">Sport Diving</a>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/visual">Visual</a>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/target-shooting">Target Shooting</a>
                                            </li>

                                        </ul>
                                        <ul class="submenu-column w100">
                                            <li class="header-cont">
                                                <h2>
                                                    <a href="http://www.cmas.org/technique" title="Technical Committee">Technical
                                                        Committee</a>
                                                </h2>
                                            </li>
                                            <!--
                                              <li>
                                                  <a href="http://www.cmas.org/technique/about-tec">About</a>
                                              </li>
                                              <li>
                                                  <a href="http://www.cmas.org/learn-to-dive">Learn to Dive</a>
                                              </li>
                                              <li>
                                                  <a href="http://www.cmas.org/technique/cmas-international-diver-training-standards-alphabetical-order">Training standards</a>
                                              </li>
                                              <li>
                                                  <a href="http://www.cmas.org/technique/training-map">Training Map</a>
                                              </li>
                                              <li>
                                                  <a href="http://www.cmas.org/technique/events-120326214804">Event Calendar</a>
                                              </li>
                                              <li>
                                                  <a href="http://www.cmas.org/technique/tcnewsletter">Newsletter</a>
                                              </li>
                                              <li>
                                                  <a href="http://www.cmas.org/technique/tc-photos-videos">Photos & Videos</a>
                                              </li>

                                            -->
                                        </ul>
                                        <ul class="submenu-column w170">
                                            <li class="header-cont">
                                                <h2>
                                                    <a href="http://www.cmas.org/science" title="Scientific Committee">Scientific
                                                        Committee</a>
                                                </h2>
                                            </li>

                                        </ul>
                                        <ul class="submenu-column w142">
                                            <li class="header-cont">
                                                <h2>
                                                    Commissions under the BoD
                                                </h2>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/appeal">Appeal</a>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/disciplinary">Disciplinary</a>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/legal">Legal</a>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/medical">Medical</a>
                                            </li>
                                            <li>
                                                <a href="http://www.cmas.org/youth">Youth</a>
                                            </li>

                                        </ul>
                                    </li>
                                    <li class="submenu-bottom">&nbsp;</li>
                                </ul>
                                <!--end commissions submenu-->
                            </li>
                            <li class="toplevel">
                                <a href="http://www.cmas.org/events" class="toplevel" title="Event Calendar">Event
                                    Calendar</a>
                            </li>
                            <li class="toplevel">
                                <a href="http://www.cmas.org/news" class="toplevel" title="News">News</a>
                            </li>
                            <li class="toplevel last">
                                <a href="http://www.cmas.org/photos-and-videos" class="toplevel"
                                   title="Photos &amp; Videos">Photos &amp; Videos</a>
                            </li>

                        </ul>
                        <!--end main menu-->
                        <div class="utilities">

                            <a href="registration.html" class="login-link"
                               title="Members' Area">Members' Area</a>

                            <div class="langchange-flags-cont">
                                <a href="http://www.cmas.org/fr?language=fr" class="fr" title="français"
                                   style="margin-right: 10px;">FR</a>
                                <a href="http://www.cmas.org/es?language=es" class="es" title="espanol">ES</a>
                            </div>
                        </div>
                    </div>
                    <div class="white-box-bottom"></div>
                </div>
                <!--end top white box-->


                <jsp:doBody/>

                <!--end content section-->
                <div class="full" style="height: 14px;"></div>
                <div id="footer-menus" class="full">
                    <div class="footer-menu-box">
                        <h3 class="footer-menu-header">main menus</h3>
                        <ul>
                            <li>
                                <a href="http://www.cmas.org/cmas/about">» About CMAS</a>
                            </li>
                            <li>
                                <a href="http://www.cmas.org/events">» Event Calendar</a>
                            </li>
                            <li>
                                <a href="http://www.cmas.org/news">» News</a>
                            </li>
                            <li>
                                <a href="http://www.cmas.org/photos-and-videos">» Photos &amp; Videos</a>
                            </li>
                        </ul>
                    </div>
                    <div class="footer-menu-box">
                        <h3 class="footer-menu-header">Committees</h3>
                        <ul>
                            <li>
                                <a href="http://www.cmas.org/sport">» Sport Committee</a>
                            </li>
                            <li>
                                <a href="http://www.cmas.org/technique">» Technical Committee</a>
                            </li>
                            <li>
                                <a href="http://www.cmas.org/science">» Scientific Committee</a>
                            </li>
                        </ul>
                    </div>
                    <div class="footer-menu-box">
                        <h3 class="footer-menu-header">cmas diverline</h3>
                        <ul>
                            <li>
                                <a href="http://www.youtube.com/CMASDiverline" target="_blank">» Videos on Youtube</a>
                            </li>
                            <li>
                                <a href="http://www.facebook.com/CMASDiverline" target="_blank">» Community on
                                    Facebook</a>
                            </li>
                        </ul>
                    </div>
                    <div class="footer-menu-box">
                        <h3 class="footer-menu-header"><a href="http://www.cmas.org/en">CMAS.org</a></h3>
                        <ul>
                            <li>
                                <a href="http://www.cmas.org/contact.php">» Contacts</a>
                            </li>
                            <li>
                                <a href="http://www.cmas.org/sitemap">» Sitemap</a>
                            </li>
                            <li>
                                <a href="http://www.cmas.org/impressum">» Impressum</a>
                            </li>
                            <li>
                                <a href="http://www.cmas.org/privacy-policy">» Privacy Policy</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <!--end bottom link-->


            </div>
        </div>
        <div id="glass-box-bottom"></div>
    </div>
</div>
<!--end sitewrapper-->
<script>
    var footerDiv = document.getElementById('footer-menus');
    var glassDiv = document.getElementById('glass-box-content');

    var topDiv = document.getElementById('top');
    var headDiv = document.getElementbyId('head');
    var glassInnerDiv = document.getElementById('glass-box-content-inner');

    glassDiv.appendChild(footerDiv);
    eElement.insertBefore(newFirstElement, eElement.firstChild);
    glassInnerDiv.appendChild(topDiv);
</script>
<!--container for bottom animation-->
<div class="footer">
    <div id="bubbles"></div>
    <div class="visitcounter">Visits today: 15 | Visits last month: 4516 | Visits to date: 1671336</div>
</div>
<script type="text/javascript" charset="utf-8" src="js/thirdparty.js"> </script>
<script type="text/javascript" charset="utf-8">
    window.onload=function(){
        var bubbles = new FlashObject("http://www.cmas.org//swf/bubbles.swf", "flash_bubbles", "960", "297", "9");
        bubbles.addParam("quality", "best");
        bubbles.addParam("wmode", "transparent");
        bubbles.addParam("menu", "false");
        bubbles.addParam("bgcolor", "#ffffff");
        bubbles.addParam("allowScriptAccess", "sameDomain");
        if(document.getElementById("bubbles"))
            bubbles.write("bubbles");
    }
</script>
<script type="text/javascript" charset="utf-8" src="js/script.js"> </script>

</body>
</html>