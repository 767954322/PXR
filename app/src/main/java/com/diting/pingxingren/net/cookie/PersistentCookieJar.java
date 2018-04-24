package com.diting.pingxingren.net.cookie;

import com.diting.pingxingren.util.TimeUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/20, 17:57.
 */

public class PersistentCookieJar implements ClearableCookieJar {

    private CookieCache cache;
    private CookiePersisted mPersisted;

    public PersistentCookieJar(CookieCache cache, CookiePersisted persisted) {
        this.cache = cache;
        this.mPersisted = persisted;

        this.cache.addAll(mPersisted.loadAll());
    }

    @Override
    synchronized public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cache.addAll(cookies);
        mPersisted.saveAll(filterPersistentCookies(cookies));
    }

    private static List<Cookie> filterPersistentCookies(List<Cookie> cookies) {
        List<Cookie> persistentCookies = new ArrayList<>();

        for (Cookie cookie : cookies) {
            if (cookie.persistent()) {
                persistentCookies.add(cookie);
            }
        }
        return persistentCookies;
    }

    @Override
    synchronized public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookiesToRemove = new ArrayList<>();
        List<Cookie> validCookies = new ArrayList<>();

        for (Iterator<Cookie> it = cache.iterator(); it.hasNext(); ) {
            Cookie currentCookie = it.next();

            if (isCookieExpired(currentCookie)) {
                cookiesToRemove.add(currentCookie);
                it.remove();

            } else if (currentCookie.matches(url)) {
                validCookies.add(currentCookie);
            }
        }

        mPersisted.removeAll(cookiesToRemove);

        return validCookies;
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < TimeUtil.getNowTimeMills();
    }

    @Override
    synchronized public void clearSession() {
        cache.clear();
        cache.addAll(mPersisted.loadAll());
    }

    @Override
    synchronized public void clear() {
        cache.clear();
        mPersisted.clear();
    }
}
