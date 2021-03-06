var cookie_controller = {
    init: function () {
        var self = this;
        if (this.isCookieExists('COOKIE_AGREE')) {
            $('#cookieWarning').hide();
        } else {
            $('#cookieWarningClose').hide();
            $('#cookieWarningOk').click(function () {
                self.createCookie("COOKIE_AGREE", "", 365 * 5);
                $('#cookieWarning').hide();
            });
            $('#cookieWarning').show();
        }
    },

    acceptCookiePolicy: function () {

    },

    isAuth: function () {
        return this.isCookieExists('AUTH_COOKIE');
    },

    addAuthCookie: function () {
        // this.createCookie("AUTH_COOKIE", "", 0);
    },

    removeAuthCookie: function () {
        // this.eraseCookie("AUTH_COOKIE");
    },

    isCookieExists: function (cookieName) {
        // first we'll split this cookie up into name/value pairs
        // note: document.cookie only returns name=value, not the other components
        var a_all_cookies = document.cookie.split(';'),
            a_temp_cookie = '',
            cookie_name = '';
        for (var i = 0; i < a_all_cookies.length; i++) {
            a_temp_cookie = a_all_cookies[i].split('=');
            cookie_name = a_temp_cookie[0].replace(/^\s+|\s+$/g, '');
            if (cookie_name == cookieName) {
                return true;
            }
        }
        return false;
    },

    createCookie: function (name, value, days) {
        var expires;
        if (days) {
            var date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            expires = "; expires=" + date.toGMTString();
        }
        else {
            expires = "";
        }
        document.cookie = name + "=" + value + expires + "; path=/";
    },

    readCookie: function (name) {
        var nameEQ = name + "=",
            ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1, c.length);
            }
            if (c.indexOf(nameEQ) == 0) {
                return c.substring(nameEQ.length, c.length);
            }
        }
        return null;
    },

    getCookie: function (name) {
        var prefix = name + "=",
            cookieStartIndex = document.cookie.indexOf(prefix),
            cooikeEndIndex;
        if (cookieStartIndex == -1) {
            return null;
        }
        cookieEndIndex = document.cookie.indexOf(";", cookieStartIndex + prefix.length);
        if (cookieEndIndex == -1) {
            cookieEndIndex = document.cookie.length;
        }
        return unescape(document.cookie.substring(cookieStartIndex + prefix.length, cookieEndIndex));
    },

    eraseCookie: function (name) {
        this.createCookie(name, "", -1);
    }
};

$(document).ready(function () {
    cookie_controller.init();
});

