package com.diting.pingxingren.net.cookie;

import okhttp3.CookieJar;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/20, 17:57.
 * This interface extends {@link CookieJar} and adds methods to clear the cookies.
 */

public interface ClearableCookieJar extends CookieJar {

    /**
     * Clear all the session cookies while maintaining the persisted ones.
     */
    void clearSession();

    /**
     * Clear all the cookies from persistence and from the cache.
     */
    void clear();
}
