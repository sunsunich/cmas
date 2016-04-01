<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>

<my:basePage title="Members' Area" indexpage="false"
             customScripts="js/model/registration_model.js,js/controller/registration_controller.js,js/model/login_model.js,js/controller/login_controller.js"
        >

    <div id="registrationPanel">
        <p>
            <label for="txbEmail">Your e-mail address:</label><br/>
            <label class="errorMessage" id="error_email"></label>
            <input type="text" id="txbEmail" name="txbEmail" value="" class='input'/><br/>
            <label for="txbRegPassword">Your password:</label><br/>
            <label class="errorMessage" id="error_password"></label>
            <input type="password" id="txbRegPassword" name="txbRegPassword" value="" class='input'/><br/>
            <label for="txbRegPasswordRepeat">Repeat your password:</label><br/>
            <label class="errorMessage" id="error_passwordRepeat"></label>
            <input type="password" id="txbRegPasswordRepeat" name="txbRegPasswordRepeat" value="" class='input'/><br/>
            <label for="oplCountries">Your country:</label><br/>
            <label class="errorMessage" id="error_country"></label>
            <select name="oplCountries" id="oplCountries" size=1 onChange="" class='optionlist'>
                <option value='' class='optionlist'></option>
                <c:forEach items="${countries}" var="country">
                    <option value='${country.code}' class='optionlist'>${country.name}</option>
                </c:forEach>
            </select>
            <label for="oplRoles">Your are:</label><br/>
            <label class="errorMessage" id="error_role"></label>
            <select name="oplRoles" id="oplRoles" size=1 onChange="" class='optionlist'>
                <option value='' class='optionlist'></option>
                <option value='ROLE_AMATEUR' class='optionlist'>Amateur</option>
                <option value='ROLE_ATHLETE' class='optionlist'>Athlete</option>
            </select>
            <label for="txbFirstName">Your first name:</label><br/>
            <input type="text" id="txbFirstName" name="txbFirstName" value="" class='input'/><br/>
            <label for="txbLastName">Your last name:</label><br/>
            <input type="text" id="txbLastName" name="txbLastName" value="" class='input'/><br/>
        </p>
        <p>By continuing, you are agreeing to <A href="data_security.php" target=_new>our Privacy
            Policy.</A></p>
        <br/>

        <div class="buttons-container">
            <img src="i/ajax-loader.gif" id="loading" class="loader-anim"/>
            <button id="regForm" type="button" value="Continue">Continue</button>
        </div>
    </div>

    <div id="registrationOkPanel"><p>Your registration was sucessful. You will automatically receive an
        e-mail with your login password wich you can sign in with.</p></div>
    <div class="login_box">
        <h3>I am a returning member</h3>

        <div class="errorMessage" id="errorMessage1"></div>
        <div id="loginPanel"><p>
            <label for="txbLogin">CMAS ID (Usually your email address):</label>
            <input type="text" id="txbLogin" name="txbLogin" value="" class='input'/> <label
                for="txbPassword">Password:</label>
            <input type="password" name="txbPassword" id="txbPassword" value=""
                   class='input'/></p>

            <div class="buttons-container">
                <img src="i/ajax-loader.gif" id="login-anim" class="loader-anim"/>
                <button id="loginForm" type="button" value="Sign In">Sign In</button>
            </div>
            <p><a href="javascript:void(0)" onclick="ShowForgetPassword()">Did you forget your
                password?</a></p>
        </div>
        <div id="forgetPasswordPanel">
            <p>Please enter the e-mail address you used at the time of registration so we can generate
                and send a new password for you.<br/><br/>
                <label for="txbEmail2">Your email address:</label>
                <input type="text" id="txbEmail2" name="txbEmail2" value=""
                       onkeypress="if (CheckEnter(event)) {SendEmail();}" class='input'/></p>

            <div class="buttons-container">
                <img src="i/ajax-loader.gif" id="send-anim" class="loader-anim"/>
                <button type="button" onclick="SendEmail()" value="Continue">Continue</button>
            </div>
            <div id="forgetPasswordOkPanel"></div>
        </div>
    </div>

    <script type="text/javascript">
        document.getElementById('txbLogin').focus();
    </script>


</my:basePage>
