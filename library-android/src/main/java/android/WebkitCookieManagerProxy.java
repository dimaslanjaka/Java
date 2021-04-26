package android;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Proxy android webkit cookie manager
 * <a href="https://stackoverflow.com/a/18070681">https://stackoverflow.com/a/18070681</a>
 */
@SuppressWarnings("unused")
public class WebkitCookieManagerProxy extends CookieManager {
    public java.net.CookieManager javaCookieManager = new CookieManager();
    public android.webkit.CookieManager androidWebkitCookieManager = android.webkit.CookieManager.getInstance();

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
        androidWebkitCookieManager.setAcceptCookie(true);

        // magic starts here
        WebkitCookieManagerProxy coreCookieManager = new WebkitCookieManagerProxy(null, java.net.CookiePolicy.ACCEPT_ALL);
        javaCookieManager.setCookiePolicy(java.net.CookiePolicy.ACCEPT_ALL);
        java.net.CookieHandler.setDefault(coreCookieManager);
    }

    WebkitCookieManagerProxy(CookieStore store, CookiePolicy cookiePolicy) {
        super(null, cookiePolicy);
        javaCookieManager = new java.net.CookieManager(store, cookiePolicy);
    }

    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
        // make sure our args are valid
        if ((uri == null) || (responseHeaders == null)) return;

        // add to java.net.CookieManager
        javaCookieManager.put(uri, responseHeaders);
        Log.i("putX", String.valueOf(responseHeaders));

        // save our url once
        String url = uri.toString();

        // go over the headers
        for (String headerKey : responseHeaders.keySet()) {
            // ignore headers which aren't cookie related
            if ((headerKey == null) || !(headerKey.equalsIgnoreCase("Set-Cookie2") || headerKey.equalsIgnoreCase("Set-Cookie"))) {
                continue;
            }
            // process each of the headers
            List<String> getHeader = responseHeaders.get(headerKey);
            Log.i("HeaderKey", headerKey);
            if (getHeader != null) {
                for (String headerValue : getHeader) {
                    this.androidWebkitCookieManager.setCookie(url, headerValue);
                }
            }

            // go over the values
            for (String headerValue : responseHeaders.get(headerKey)) {
                try {
                    List<HttpCookie> cookies = HttpCookie.parse(headerValue);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cookies.stream().forEach(new Consumer<HttpCookie>() {
                            @Override
                            public void accept(HttpCookie httpCookie) {
                                getCookieStore().add(uri, httpCookie);
                            }
                        });
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
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
        String cookie = this.androidWebkitCookieManager.getCookie(url);

        // return it
        if (cookie != null) res.put("Cookie", Collections.singletonList(cookie));
        return res;
    }

    @Override
    public CookieStore getCookieStore() {
        return javaCookieManager.getCookieStore();
    }

    private void println(Object... obj) {
        for (Object o : obj) {
            System.out.println(o);
        }
    }

    /**
     * @return instance of java.net.CookieManager
     */
    @NotNull
    public CookieManager toJavaNetCookieManager() {
        return javaCookieManager;
    }

    /**
     * @return instance of android.webkit.CookieManager.getInstance();
     */
    @NotNull
    public android.webkit.CookieManager toAndroidWebkitCookieManager() {
        return androidWebkitCookieManager;
    }
}
