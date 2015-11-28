<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>

<my:basePage title="Members' Area" indexpage="false"
             customScripts="js/model/registration_model.js,js/controller/registration_controller.js,js/model/login_model.js,js/controller/login_controller.js"
        >
    <!--middle section: committee boxes-->
    <div class="full mb10">
        <ul class="committees-menu">
            <li class="committe-menu sport jshover">
                <a href="http://www.cmas.org/sport" class="committe-menu-link"><span class="pointer">▼</span> Sport</a>
                <ul class="committee-submenu">
                    <li>
                        <a href="http://www.cmas.org/apnoea">
                            Apnoea
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/aquathlon">
                            Aquathlon
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/finswimming">
                            Finswimming
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/hockey">
                            Underwater Hockey
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/orienteering">
                            Orienteering
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/underwater-rugby">
                            Underwater Rugby
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/spearfishing">
                            Spearfishing
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/sport-diving">
                            Sport Diving
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/visual">
                            Visual
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/target-shooting">
                            Target Shooting
                        </a>
                    </li>

                </ul>

            </li>
            <li class="committe-menu tech jshover">
                <a href="http://www.cmas.org/technique" class="committe-menu-link"><span class="pointer">▼</span>
                    Technical issues</a>
                <ul class="committee-submenu">
                    <li>
                        <a href="http://www.cmas.org/technique">News</a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/technique/about-tec">
                            About
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/learn-to-dive">
                            Learn to Dive
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/technique/cmas-international-diver-training-standards-alphabetical-order">
                            Training standards
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/technique/training-map">
                            Training Map
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/technique/events-120326214804">
                            Event Calendar
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/technique/tcnewsletter">
                            Newsletter
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/technique/tc-photos-videos">
                            Photos & Videos
                        </a>
                    </li>

                </ul>

            </li>
            <li class="committe-menu sci jshover">
                <a href="http://www.cmas.org/science" class="committe-menu-link"><span class="pointer">▼</span> Science</a>
                <ul class="committee-submenu">
                    <li>
                        <a href="http://www.cmas.org/science">News</a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/science/about-sci">
                            About
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/science/activity">
                            Activity
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/science/standards">
                            Training Standards
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/science/underwater-cultural-heritage">
                            Underwater Cultural Heritage
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/science/scientific-diving">
                            Scientific Diving
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cmas.org/science/education-materials">
                            Educational Materials
                        </a>
                    </li>

                </ul>

            </li>
        </ul>
    </div>
    <!--end middle section-->
    <!--content section-->
    <div class="white-box full mb10" id="content">
        <div class="white-box-top"></div>
        <div class="white-box-middle article clearfix">
            <script type="text/javascript" src="js/login.js"></script>

            <div class="column w539">
                <h1 class="header">

                    Members' Area
                </h1>

                <div class="login_container">
                    <script type="text/javascript">
                        var invalidEmailMessage = 'Invalid or missing e-mail';
                    </script>
                    <div class="registration_box">
                        <h3>I am new to CMAS</h3>

                        <div id="regInfoPanel">
                            <input type="button" value="Create a CMAS account" class="createacc"
                                   onclick="ShowRegistration()"/>

                            <P>CMAS&nbsp;Membership is free and registration only takes a minute. As a member, you will
                                have access to the Document Area of the site.</P>
                        </div>
                        <div id="registrationPanel">
                            <p>
                                <label for="txbEmail">Your e-mail address:</label><br/>
                                <label class="errorMessage" id="error_email"></label>
                                <input type="text" id="txbEmail" name="txbEmail" value="" class='input'/><br/>
                                <label for="txbRegPassword">Your password:</label><br/>
                                <label class="errorMessage" id="error_password"></label>
                                <input type="text" id="txbRegPassword" name="txbRegPassword" value="" class='input'/><br/>
                                <label for="txbRegPasswordRepeat">Repeat your password:</label><br/>
                                <label class="errorMessage" id="error_passwordRepeat"></label>
                                <input type="text" id="txbRegPasswordRepeat" name="txbRegPasswordRepeat" value="" class='input'/><br/>
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
                                    <option value='ROLE_SPORTSMAN' class='optionlist'>Sportsman</option>
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
                </div>
                <script type="text/javascript">
                    document.getElementById('txbLogin').focus();
                </script>
            </div>

            <div class="column w333 last">
                <h1 class="header">
                    Need help?
                </h1>

                <div class="article">
                    <p><strong>I can't sign in although I have had already an old CMAS ID, created before 2010.</strong><br/>Please
                        make a new registration! You can't sign in with your old account on this new
                        site.<br/><br/><strong>I have an account but I forgot my password.<br/></strong>To retrieve the
                        password of an existing CMAS ID please <a href="javascript:ShowForgetPassword()">click here</a>
                        and follow the instructions.<br/><strong><br/>I haven't had any confirmation e-mail from the
                            site.<br/></strong>Be sure you have given your e-mail correctly and please check whether
                        your SPAM filter has not filtered the system messages from @cmas.org.</p>
                </div>
            </div>
        </div>
        <div class="white-box-bottom"></div>
    </div>
    <!--end content section-->

    <!--ad boxes-->
    <div class="white-box full">
        <div class="white-box-top"></div>
        <div class="white-box-middle clearfix promo-containers">

            <ul class="promo-boxes">
                <li class="first">
                    <div class="promo-container">
                        <a href="http://www.cressi.com/" title="" target="_blank">
                            <img src="http://www.cmas.org/php_images/cressi-146x90.jpg" alt=""/>
                        </a>
                    </div>
                </li>
                <li>
                    <div class="promo-container">
                        <a href="http://www.najadefins.org" title="" target="_blank">
                            <img src="http://www.cmas.org/php_images/najade_fins_homologated1-150x90.jpg" alt=""/>
                        </a>
                    </div>
                </li>
                <li>
                    <div class="promo-container">
                        <a href="http://www.finsuits.com/" title="" target="_blank">
                            <img src="http://www.cmas.org/php_images/banner_finsuit-140317093946-150x90.jpg" alt=""/>
                        </a>
                    </div>
                </li>
                <li>
                    <div class="promo-container">
                        <a href="http://www.finsuits.com/" title="" target="_blank">
                            <img src="http://www.cmas.org/php_images/banner_diana-150x90.jpg" alt=""/>
                        </a>
                    </div>
                </li>
                <li>
                    <div class="promo-container">
                        <a href="http://www.finsuits.com/" title="" target="_blank">
                            <img src="http://www.cmas.org/php_images/banner_jaked-150x90.jpg" alt=""/>
                        </a>
                    </div>
                </li>
                <li>
                    <div class="promo-container">
                        <a href="http://www.murenafin.com/" title="" target="_blank">
                            <img src="http://www.cmas.org/php_images/cmas_bann-150x90.jpg" alt=""/>
                        </a>
                    </div>
                </li>
                <li>
                    <div class="promo-container">
                        <a href="http://www.arenafinswim.com" title="" target="_blank">
                            <img src="http://www.cmas.org/php_images/arena_banner-150x90.jpg" alt=""/>
                        </a>
                    </div>
                </li>

            </ul>
        </div>
        <div class="white-box-bottom"></div>
    </div>

</my:basePage>
