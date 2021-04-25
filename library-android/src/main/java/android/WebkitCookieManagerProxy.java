package android;

import android.content.Context;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Proxy android webkit cookie manager
 * <a href="https://stackoverflow.com/a/18070681">https://stackoverflow.com/a/18070681</a>
 */
@SuppressWarnings("unused")
public class WebkitCookieManagerProxy extends CookieManager {
    private final java.net.CookieManager javaCookieManager = new CookieManager();
    private android.webkit.CookieManager webkitCookieManager;

    public WebkitCookieManagerProxy() {
        this(null, null);
    }

    /**
     * Usage
     * <a href="https://stackoverflow.com/a/18070681">https://stackoverflow.com/a/18070681</a>
     *
     * @param appContext context
     */
    public WebkitCookieManagerProxy(Context appContext) {
        android.webkit.CookieSyncManager.createInstance(appContext);
        // unrelated, just make sure cookies are generally allowed
        android.webkit.CookieManager.getInstance().setAcceptCookie(true);

        // magic starts here
        WebkitCookieManagerProxy coreCookieManager = new WebkitCookieManagerProxy(null, java.net.CookiePolicy.ACCEPT_ALL);
        javaCookieManager.setCookiePolicy(java.net.CookiePolicy.ACCEPT_ALL);
        java.net.CookieHandler.setDefault(coreCookieManager);
    }

    WebkitCookieManagerProxy(CookieStore store, CookiePolicy cookiePolicy) {
        super(null, cookiePolicy);

        this.webkitCookieManager = android.webkit.CookieManager.getInstance();
    }

    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
        // make sure our args are valid
        if ((uri == null) || (responseHeaders == null)) return;

        // add to java.net.CookieManager
        javaCookieManager.put(uri, responseHeaders);

        // save our url once
        String url = uri.toString();

        // go over the headers
        for (String headerKey : responseHeaders.keySet()) {
            // ignore headers which aren't cookie related
            if ((headerKey == null) || !(headerKey.equalsIgnoreCase("Set-Cookie2") || headerKey.equalsIgnoreCase("Set-Cookie")))
                continue;

            // process each of the headers
            List<String> getHeader = responseHeaders.get(headerKey);
            if (getHeader != null) {
                for (String headerValue : getHeader) {
                    this.webkitCookieManager.setCookie(url, headerValue);
                }
            }
        }
    }

    @Override
    public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
        // make sure our args are valid
        if ((uri == null) || (requestHeaders == null))
            throw new IllegalArgumentException("Argument is null");

        // save our url once
        String url = uri.toString();

        // prepare our response
        Map<String, List<String>> res = new java.util.HashMap<String, List<String>>();

        // get the cookie
        String cookie = this.webkitCookieManager.getCookie(url);

        // return it
        if (cookie != null) res.put("Cookie", Arrays.asList(cookie));
        return res;
    }

    @Override
    public CookieStore getCookieStore() {
        return javaCookieManager.getCookieStore();
    }
}
