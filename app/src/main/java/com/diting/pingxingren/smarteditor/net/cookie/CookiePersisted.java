package com.diting.pingxingren.smarteditor.net.cookie;

import java.util.Collection;
import java.util.List;

import okhttp3.Cookie;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/20, 17:53.
 * A Cookie Persisted handles the persistent cookie storage.
 */

public interface CookiePersisted {

    List<Cookie> loadAll();

    /**
     * Persist all cookies, existing cookies will be overwritten.
     *
     * @param cookies cookies persist
     */
    void saveAll(Collection<Cookie> cookies);

    /**
     * Removes indicated cookies from persistence.
     *
     * @param cookies cookies to remove from persistence
     */
    void removeAll(Collection<Cookie> cookies);

    /**
     * Clear all cookies from persistence.
     */
    void clear();
}
