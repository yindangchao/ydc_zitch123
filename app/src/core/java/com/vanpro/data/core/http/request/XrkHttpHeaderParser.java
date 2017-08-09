package com.vanpro.data.core.http.request;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * 重写HeaderParser
 * 强制缓存，忽略服务器的设置
 */
public class XrkHttpHeaderParser extends HttpHeaderParser {
    /**
     * @param response  The network response to parse headers from
     * @param cacheTime 缓存时间，单位为毫秒
     * @return a cache entry for the given response, or null if the response is not cacheable.
     */
    public static Cache.Entry parseCacheHeaders(NetworkResponse response, long cacheTime) {
        Cache.Entry entry = parseCacheHeaders(response);
        long now = System.currentTimeMillis();
        long softExpire = now + cacheTime;
        entry.softTtl = softExpire;
        entry.ttl = entry.softTtl;
        return entry;
    }
}