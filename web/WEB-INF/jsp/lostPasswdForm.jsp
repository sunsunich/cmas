<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>


<jsp:useBean id="captchaError" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="reCaptchaPublicKey" scope="request" type="java.lang.String"/>


<my:basePage title="ВОССТАНОВЛЕНИЕ ПАРОЛЯ " indexpage="false">

                   <!-- end of Navigation menu -->

    <div class="setting-block">

        <div class="setting_rules">Введите ваш e-mail, контрольный текст в форме и вам будет отправлено письмо с
            дальнейшими указаниями
        </div>


        <form:form htmlEscape="true"
                   action="/lostPasswd.html"
                   method="POST"
                   id="">

            <ef:input label="E-mail:*" path="email"/>


            <div id="" class="capcha-block">


                <ef:captcha reCaptchaPublicKey="${reCaptchaPublicKey}"/>

                <c:if test="${captchaError}">
                    <span class="error correct">Буквы не соответствуют</span>
                </c:if>
		<input class="setting_agree" type="submit" value="Подтвердить"/>
            </div>
            
        </form:form>        
    </div>
    <!-- end of Content -->


</my:basePage>