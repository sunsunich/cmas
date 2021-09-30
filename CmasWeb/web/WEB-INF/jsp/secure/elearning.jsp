<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<my:securepage title="cmas.face.elearning.title"
               activeMenuItem="elearning"
               customScripts="js/model/elearning_model.js,js/controller/elearning_controller.js"
>

    <div class="content-center-wide" id="content">

        <div class="panel panel-header">
            <span class="header2-text">Please find below instructions for accessing CMAS E-learning portal</span>
            <div class="form-description clearfix">
                CMAS AquaLink provides you with a one time use coupon for CMAS E-learning portal.<br/>
                With the coupon you can register at CMAS E-learning portal for free and access the E-learning materials
                from CMAS.<br/>
                After successful registration at CMAS E-learning portal you can return to it at any time.<br/>
            </div>
        </div>
        <div id="registerToElearningBlock" class="panel panel-header" style="display: none">
            <div class="header3-text">New registeration at CMAS E-learning portal</div>
            <div class="form-description clearfix">You will only be able to use the same coupon once</div>
            <br/>
            <button id="eLearningRegister" class="positive-button" style="display: none">
                REGISTER AT E-LEARNING PORTAL
            </button>
            <span id="tokeErrorText" class="basic-text error-color" style="display: none"></span>

        </div>
        <div id="returnToElearningBlock" class="panel panel-header">
            <div class="header3-text">For returning to CMAS E-learning portal press the button below:</div>
            <br/>
            <button class="positive-button"
                    onclick="window.open('https://elearning.cloud-cmas.org/course/index.php', '_blank').focus();">
                GO TO E-LEARNING PORTAL
            </button>
        </div>
    </div>

    <!-- end of Content -->

</my:securepage>
