package com.diting.pingxingren.smarteditor.net.cookie;

import java.util.Collection;

import okhttp3.Cookie;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/20, 17:50.
 * CookieCache.
 * A CookieCache handles the volatile cookie session storage.
 */

public interface CookieCache extends Iterable<Cookie> {

    /**
     * Add all the new cookies to the session, existing cookies will be overwritten.
     */
    void addAll(Collection<Cookie> cookies);

    /**
     * Clear all the cookies from the session.
     */
    void clear();
}

