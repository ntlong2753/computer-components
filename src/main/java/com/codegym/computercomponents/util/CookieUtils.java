package com.codegym.computercomponents.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class CookieUtils {

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObjectFromCookie(HttpServletRequest request, String name, Class<T> clazz) {
        String value = getCookieValue(request, name);
        if (value != null && !value.isEmpty()) {
            try {
                String decodedValue = java.net.URLDecoder.decode(value, java.nio.charset.StandardCharsets.UTF_8);
                byte[] data = Base64.getDecoder().decode(decodedValue);
                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                    return (T) ois.readObject();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void setObjectToCookie(HttpServletResponse response, String name, Object object, int maxAge) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(object);
            }
            String base64Value = Base64.getEncoder().encodeToString(baos.toByteArray());
            String encodedValue = java.net.URLEncoder.encode(base64Value, java.nio.charset.StandardCharsets.UTF_8);
            setCookie(response, name, encodedValue, maxAge);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
