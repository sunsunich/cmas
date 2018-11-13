<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<my:helppage>
    <div class="header2-text faq-header"><s:message code="cmas.face.client.cookies"/></div>
    <div>
        <div class="question">
            <div class="answer">
                Cookies are text values stored on web browsers on computers, phones and other devices.
                <br/>
                CMAS Family uses <a id="viewCookies" class='link-in-text-flow' href="#">cookies</a>
                to deliver some of the divers' services: determine whether or not you
                are registered or logged in. Enabling cookies is required to use CMAS Family.
            </div>
        </div>
        <div class="answer">
            This policy explains how CMAS Family uses cookies. Except as otherwise stated in this policy,
            CMAS Family <a class='link-in-text-flow'
                                href="${pageContext.request.contextPath}/termsAndCond.html"><s:message
                code="cmas.face.client.termsAndCondHeader"/></a> will apply to CMAS Family processing of
            the data that is being collected by CMAS Family using cookies.
        </div>
        <div class="answer">
            CMAS Family uses cookies to determine when you are logged in.
            This is required to navigate through CMAS Family pages an give you access to your personal page
            and data.
        </div>
        <div class="answer">
            CMAS Family may place cookies on your computer or device, and receive information stored in
            cookies, when you visit CMAS Family website.
            CMAS Family uses cookies and receives information when you visit CMAS Family website, including
            device information and information about your activity, without any further action from you. This
            occurs whether or not you are registered with CMAS Family or are logged in.
        </div>
        <div class="answer">
            No third parties use or have access to cookies created by CMAS Family
            unless you explicitly provide them with this data.
        </div>
        <div class="answer">
            Even though cookies are required to use CMAS Family, your browser or device may offer settings
            to enable and disable browser cookies and to delete them. For more information
            about these controls, see your browser or device's manuals. Please note that CMAS Family
            will not work properly if you have disabled cookies in your browser.
        </div>
        <div class="answer">
            Date of last revision: 2nd of November, 2018
        </div>
    </div>

    <my:dialog id="cookiesTable" title="cmas.face.client.cookies.used.title">
        <table>
            <tr>
                <th>Purpose</th>
                <th>Cookie</th>
                <th>Expires</th>
            </tr>
            <tr>
                <td>Authentication</td>
                <td>JSESSIONID</td>
                <td>Browser session</td>
            </tr>
            <tr>
                <td>Site usability</td>
                <td>COOKIE_AGREE</td>
                <td>5 years</td>
            </tr>
        </table>
    </my:dialog>

    <script type="application/javascript">
        $(document).ready(function () {
            $('#viewCookies').click(function (e) {
                e.preventDefault();
                $('#cookiesTable').show();
            });

            $('#cookiesTableClose').click(function () {
                $('#cookiesTable').hide();
            })
        });
    </script>
</my:helppage>