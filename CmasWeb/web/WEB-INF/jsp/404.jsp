<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>


<my:nonsecurepage title="cmas.face.404.title" customCSSFiles="/c/page404.css">

    <div id="content">           <!-- Content -->
        <div class="backgroundContainer page404" id="404_container">
            <div style="height: 75%;"></div>
            <div class="page404-text" id="page404-text">
                <s:message code="cmas.face.404.message"/>
            </div>
            <div style="height: 4%;"></div>
            <button class="positive-button" onclick="window.location='/'">
                <s:message code="cmas.face.link.back.home.text"/>
            </button>
        </div>
    </div>

    <picture>
        <!--[if IE 9]>
        <video style="display: none;"><![endif]-->

        <source srcset="/i/404_land_2888.png" media="(min-width: 2800px) and (min-aspect-ratio: 10/9)">
        <source srcset="/i/404_land_2560.png" media="(min-width: 2560px) and (min-aspect-ratio: 10/9)">
        <source srcset="/i/404_land_1920.png" media="(min-width: 1920px) and (min-aspect-ratio: 10/9)">
        <source srcset="/i/404_land_1680.png" media="(min-width: 1680px) and (min-aspect-ratio: 10/9)">
        <source srcset="/i/404_land_1440.png" media="(min-width: 1440px) and (min-aspect-ratio: 10/9)">
        <source srcset="/i/404_land_1280.png" media="(min-width: 1280px) and (min-aspect-ratio: 10/9)">
        <source srcset="/i/404_land_1024.png" media="(min-width: 1024px) and (min-aspect-ratio: 10/9)">
        <source srcset="/i/404_land_962.png" media="(min-width: 962px) and (min-aspect-ratio: 10/9)">
        <source srcset="/i/404_land_640.png 1x, /i/404_land_1280.png 2x, /i/404_land_1920.png 3x, /i/404_land_2560.png 4x"
                media="(min-width: 640px) and (min-aspect-ratio: 10/9)">
        <source srcset="/i/404_land_480.png 1x, /i/404_land_962.png 2x, /i/404_land_1440.png 3x, /i/404_land_1920.png 4x"
                media="(min-aspect-ratio: 10/9)">

        <source srcset="/i/404_tab_portrait_1024.png"
                media="(min-width: 1024px) and (min-aspect-ratio: 3/4)">
        <source srcset="/i/404_tab_portrait_834.png" media="(min-width: 834px) and (min-aspect-ratio: 3/4)">
        <source srcset="/i/404_tab_portrait_768.png" media="(min-width: 768px) and (min-aspect-ratio: 3/4)">

        <source srcset="/i/404_portrait_800.png" media="(min-width: 800px) and (min-aspect-ratio: 2/3)">
        <source srcset="/i/404_portrait_516.png" media="(min-width: 516px) and (min-aspect-ratio: 2/3)">
        <source srcset="/i/404_portrait_320.png" media="(min-width: 320px) and (min-aspect-ratio: 2/3)">

        <source srcset="/i/404_long_portrait_1080.png" media="(min-width: 1080px)">
        <source srcset="/i/404_long_portrait_720.png" media="(min-width: 720px)">
        <source srcset="/i/404_long_portrait_601.png" media="(min-width: 601px)">
        <source srcset="/i/404_long_portrait_540.png" media="(min-width: 540px)">
        <source srcset="/i/404_long_portrait_480.png" media="(min-width: 480px)">
        <source srcset="/i/404_long_portrait_414.png" media="(min-width: 414px)">
        <source srcset="/i/404_long_portrait_375.png" media="(min-width: 375px)">
        <source srcset="/i/404_long_portrait_320.png" media="(min-width: 320px)">

            <%--default--%>
        <source srcset="/i/404_land_480.png">

        <!--[if IE 9]></video><![endif]-->
        <img id="404_background" alt="404 background" style="display: none">
    </picture>
    <!-- end of Content -->

    <script type="application/javascript">
        $(window).load(function () {
            replaceBackgroundFullWindow("404_container", "404_background", $('#header').height());
            adjustTextFont("page404-text", 4, 12, 32);
        });
        $(window).resize(function () {
            replaceBackgroundFullWindow("404_container", "404_background", $('#header').height());
            adjustTextFont("page404-text", 4, 12, 32);
        });
    </script>

</my:nonsecurepage>