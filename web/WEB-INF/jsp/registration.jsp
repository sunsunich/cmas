<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<jsp:useBean id="passwordStrength" scope="request" type="java.lang.String"/>

<my:basePage title="Members' Area" indexpage="false"
             customScripts="/js/model/registration.js"
        >    <script type="text/javascript" src="/js/app/models/welcome/registration.js"></script>
    <script type="text/javascript" src="/js/app/controllers/welcome/registration.controller.js"></script>
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
            <script type="text/javascript" src="http://www.cmas.org/js/login.js"></script>

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

                        <div id="errorMessage2"></div>
                        <div id="regInfoPanel">
                            <input type="button" value="Create a CMAS account" class="createacc"
                                   onclick="ShowRegistration()"/>

                            <P>CMAS&nbsp;Membership is free and registration only takes a minute. As a member, you will
                                have access to the Document Area of the site.</P>
                        </div>
                        <div id="registrationPanel">

                            <p>
                                <label for="txbEmail">Your e-mail address:</label><br/>
                                <input type="text" id="txbEmail" name="txbEmail" value="" class='input'/><br/>
                                <label for="oplCountries">Your country:</label><br/>
                                <select name="oplCountries" id="oplCountries" size=1 onChange="" class='optionlist'>
                                    <option value='' class='optionlist'></option>
                                    <option value='AFG' class='optionlist'>Afghanistan</option>
                                    <option value='RSA' class='optionlist'>South Africa</option>
                                    <option value='ALB' class='optionlist'>Albania</option>
                                    <option value='ALG' class='optionlist'>Algeria</option>
                                    <option value='GER' class='optionlist'>Germany</option>
                                    <option value='RDA' class='optionlist'>Germany Democratic Rep</option>
                                    <option value='RFA' class='optionlist'>Germany Federal Rep</option>
                                    <option value='AND' class='optionlist'>Andorra</option>
                                    <option value='ANG' class='optionlist'>Angola</option>
                                    <option value='KSA' class='optionlist'>Saudi Arabia</option>
                                    <option value='ARG' class='optionlist'>Argentina</option>
                                    <option value='ARM' class='optionlist'>Armenia</option>
                                    <option value='AUS' class='optionlist'>Australia</option>
                                    <option value='AUT' class='optionlist'>Austria</option>
                                    <option value='BAH' class='optionlist'>Bahamas</option>
                                    <option value='BEL' class='optionlist'>Belgium</option>
                                    <option value='BLR' class='optionlist'>Byelorussia</option>
                                    <option value='BOL' class='optionlist'>Bolivia</option>
                                    <option value='BIH' class='optionlist'>Bosnia-Herzegovina</option>
                                    <option value='BRA' class='optionlist'>Brazil</option>
                                    <option value='BWI' class='optionlist'>British West Indies</option>
                                    <option value='BUL' class='optionlist'>Bulgaria</option>
                                    <option value='CAM' class='optionlist'>Cambodgia</option>
                                    <option value='CMR' class='optionlist'>Cameroon</option>
                                    <option value='CAN' class='optionlist'>Canada</option>
                                    <option value='CPV' class='optionlist'>Cape Verde</option>
                                    <option value='CHI' class='optionlist'>Chile</option>
                                    <option value='CHN' class='optionlist'>China, P. R. of</option>
                                    <option value='CYP' class='optionlist'>Cyprus</option>
                                    <option value='COL' class='optionlist'>Colombia</option>
                                    <option value='KOR' class='optionlist'>South Korea</option>
                                    <option value='CRC' class='optionlist'>Costa Rica</option>
                                    <option value='CRO' class='optionlist'>Croatia</option>
                                    <option value='CUB' class='optionlist'>Cuba</option>
                                    <option value='DEN' class='optionlist'>Denmark</option>
                                    <option value='DOM' class='optionlist'>Dominican Rep</option>
                                    <option value='EGY' class='optionlist'>Egypt</option>
                                    <option value='UEA' class='optionlist'>Arab Emirates</option>
                                    <option value='ECU' class='optionlist'>Ecuador</option>
                                    <option value='ESP' class='optionlist'>Spain</option>
                                    <option value='EST' class='optionlist'>Estonia</option>
                                    <option value='USA' class='optionlist'>United States of America</option>
                                    <option value='FIN' class='optionlist'>Finland</option>
                                    <option value='MKD' class='optionlist'>Former Yugoslav Republic of Macedonia
                                    </option>
                                    <option value='FRA' class='optionlist'>France</option>
                                    <option value='GEO' class='optionlist'>Georgia</option>
                                    <option value='GBR' class='optionlist'>Great Britain</option>
                                    <option value='GRE' class='optionlist'>Greece</option>
                                    <option value='GUM' class='optionlist'>Guam</option>
                                    <option value='HON' class='optionlist'>Honduras</option>
                                    <option value='HKG' class='optionlist'>Hong Kong</option>
                                    <option value='HUN' class='optionlist'>Hungary</option>
                                    <option value='IND' class='optionlist'>India</option>
                                    <option value='INA' class='optionlist'>Indonesia</option>
                                    <option value='INT' class='optionlist'>International</option>
                                    <option value='IRI' class='optionlist'>Iran</option>
                                    <option value='IRL' class='optionlist'>Ireland</option>
                                    <option value='ISR' class='optionlist'>Israel</option>
                                    <option value='ITA' class='optionlist'>Italy</option>
                                    <option value='JPN' class='optionlist'>Japan</option>
                                    <option value='JOR' class='optionlist'>Jordan</option>
                                    <option value='KAZ' class='optionlist'>Kazkhstan</option>
                                    <option value='KEN' class='optionlist'>Kenya</option>
                                    <option value='KUW' class='optionlist'>Kuwait</option>
                                    <option value='KGZ' class='optionlist'>Kyrgyz Republic</option>
                                    <option value='LAT' class='optionlist'>Latvia</option>
                                    <option value='LIB' class='optionlist'>Lebanon</option>
                                    <option value='LBA' class='optionlist'>Libya</option>
                                    <option value='LIE' class='optionlist'>Liechtenstein</option>
                                    <option value='LTU' class='optionlist'>Lithuania</option>
                                    <option value='LUX' class='optionlist'>Luxembourg Gd Duchy</option>
                                    <option value='MAD' class='optionlist'>Madagascar</option>
                                    <option value='MAS' class='optionlist'>Malaysia</option>
                                    <option value='MDV' class='optionlist'>Maldives</option>
                                    <option value='MLT' class='optionlist'>Malta</option>
                                    <option value='IMA' class='optionlist'>Marianas</option>
                                    <option value='MAR' class='optionlist'>Marocco</option>
                                    <option value='MRI' class='optionlist'>Mauritius</option>
                                    <option value='MEX' class='optionlist'>Mexico</option>
                                    <option value='MDA' class='optionlist'>Republic of Moldova</option>
                                    <option value='MON' class='optionlist'>Monaco</option>
                                    <option value='MNE' class='optionlist'>Montenegro</option>
                                    <option value='NAM' class='optionlist'>Namibia</option>
                                    <option value='NGR' class='optionlist'>Nigeria</option>
                                    <option value='NOR' class='optionlist'>Norway</option>
                                    <option value='NCL' class='optionlist'>New Caledonia</option>
                                    <option value='NZL' class='optionlist'>New Zealand</option>
                                    <option value='OMA' class='optionlist'>Oman</option>
                                    <option value='PAK' class='optionlist'>Pakistan</option>
                                    <option value='PLE' class='optionlist'>Palestine</option>
                                    <option value='PAN' class='optionlist'>Panama</option>
                                    <option value='NED' class='optionlist'>The Netherlands</option>
                                    <option value='PER' class='optionlist'>Peru</option>
                                    <option value='PHI' class='optionlist'>Philippines</option>
                                    <option value='POL' class='optionlist'>Poland</option>
                                    <option value='TAH' class='optionlist'>French Polynesia</option>
                                    <option value='PUR' class='optionlist'>Puerto Rico</option>
                                    <option value='POR' class='optionlist'>Portugal</option>
                                    <option value='QAT' class='optionlist'>Qatar</option>
                                    <option value='ROM' class='optionlist'>Rumania</option>
                                    <option value='RUS' class='optionlist'>Russia</option>
                                    <option value='SMR' class='optionlist'>San Marino</option>
                                    <option value='ESA' class='optionlist'>El Salvador</option>
                                    <option value='SEN' class='optionlist'>Senegal</option>
                                    <option value='SRB' class='optionlist'>Serbia</option>
                                    <option value='SEY' class='optionlist'>Seychelles</option>
                                    <option value='SIN' class='optionlist'>Singapore</option>
                                    <option value='SVK' class='optionlist'>Slovakia</option>
                                    <option value='SLO' class='optionlist'>Slovenia</option>
                                    <option value='SUD' class='optionlist'>Dudan</option>
                                    <option value='SRI' class='optionlist'>Sri Lanka</option>
                                    <option value='SWE' class='optionlist'>Sweden</option>
                                    <option value='SUI' class='optionlist'>Switzerland</option>
                                    <option value='SYR' class='optionlist'>Syria</option>
                                    <option value='TPE' class='optionlist'>Taipei Chinese</option>
                                    <option value='TAN' class='optionlist'>Tanzania</option>
                                    <option value='CZE' class='optionlist'>Czech Republic</option>
                                    <option value='THA' class='optionlist'>Thailand</option>
                                    <option value='TUN' class='optionlist'>Tunisia</option>
                                    <option value='TUR' class='optionlist'>Turkey</option>
                                    <option value='UKR' class='optionlist'>Ukrainia</option>
                                    <option value='URS' class='optionlist'>USSR</option>
                                    <option value='URU' class='optionlist'>Uruguay</option>
                                    <option value='VEN' class='optionlist'>Venezuela</option>
                                    <option value='VIE' class='optionlist'>Viet Nam</option>
                                    <option value='YUG' class='optionlist'>Yugoslavia</option>
                                    <option value='ZIM' class='optionlist'>Zimbabwe</option>
                                    <option value='TWN' class='optionlist'>Republic of China</option>
                                    <option value='NCYP' class='optionlist'>North Cyprus</option>
                                    <option value='SCG' class='optionlist'>Serbia and Montenegro</option>
                                </select></p>
                            <p>By continuing, you are agreeing to <A href="data_security.php" target=_new>our Privacy
                                Policy.</A></p>
                            <br/>

                            <div class="buttons-container">
                                <img src="i/ajax-loader.gif" id="registration-anim" class="loader-anim"/>
                                <button type="button" onclick="CreateAccount()" value="Continue">Continue</button>
                            </div>
                        </div>
                    </div>
                    <div id="registrationOkPanel"></div>
                    <div class="login_box">
                        <h3>I am a returning member</h3>

                        <div id="errorMessage1"></div>
                        <div id="loginPanel"><p>
                            <label for="txbLogin">CMAS ID (Usually your email address):</label>
                            <input type="text" id="txbLogin" name="txbLogin" value="" class='input'/> <label
                                for="txbPassword">Password:</label>
                            <input type="password" name="txbPassword" id="txbPassword" value=""
                                   onkeypress="if (CheckEnter(event)) {SignIn();}" class='input'/></p>

                            <div class="buttons-container">
                                <img src="i/ajax-loader.gif" id="login-anim" class="loader-anim"/>
                                <button type="button" onclick="SignIn()" value="Sign In">Sign In</button>
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
