package org.cmas.util.http;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.Cookie;

public class Cookies {

    private Cookies() {
    }

    @Nullable
    public static String getCookieValueByName(Cookie[] cookies, String name) {
        Cookie searchedCookie = searchCookie(cookies, name);
        return searchedCookie == null ? null : searchedCookie.getValue();
    }

    @NotNull
    public static Cookie rewriteValueInCookie(Cookie[] cookies, String cookieName, String value, int cookieMaxAge) {
        Cookie cookieToRewrite = getCookieOrCreateNew(cookies, cookieName, value, cookieMaxAge);

        cookieToRewrite.setValue(value);
        cookieToRewrite.setMaxAge(cookieMaxAge);

        return cookieToRewrite;
    }

    @NotNull
    public static Cookie getCookieOrCreateNew(Cookie[] cookies, String cookieName, String defaultValue, int cookieMaxAge) {
        Cookie searchedCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    searchedCookie = cookie;
                    break;
                }
            }
        }
        if (searchedCookie == null) {
            searchedCookie = createCookie(cookieName, defaultValue, cookieMaxAge);
        }
        return searchedCookie;
    }

    @Nullable
    public static Cookie getCookie(Cookie[] cookies, String cookieName) {
        Cookie searchedCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    searchedCookie = cookie;
                    break;
                }
            }
        }
        return searchedCookie;
    }

    @NotNull
    public static Cookie createCookie(String cookieName, String value, int cookieMaxAge) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(cookieMaxAge);
        cookie.setPath("/");
        return cookie;
    }

    @Nullable
    private static Cookie searchCookie(Cookie[] cookies, String name) {
        Cookie searchedCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    searchedCookie = cookie;
                    break;
                }
            }
        }
        return searchedCookie;
    }
}
