package com.dimaslanjaka.components.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.dimaslanjaka.components.App;
import com.dimaslanjaka.components.commons.Url;
import com.dimaslanjaka.components.log.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WebView2 extends android.webkit.WebView {
    public static String UserAccount = "default";
    android.webkit.CookieSyncManager syncManager = android.webkit.CookieSyncManager.createInstance(getContext());
    android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
    jCookieManager jcm;
    private Map<String, ?> AllCookies;

    public WebView2(@NonNull Context context) {
        super(context);
        initWebView();
    }

    public WebView2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    public WebView2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WebView2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initWebView();
    }

    public WebView2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        initWebView();
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences settings = getSharedPreferences(context);
        return settings.edit();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(UserAccount, 0);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        create(UserAccount);
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public void onPageFinished(android.webkit.WebView view, String url) {
                pageFinished(view, url);
            }
        });
    }

    public void setUserAccount(String uid) {
        UserAccount = uid;
    }

    public jCookieManager create(String id) {
        jCookieManager manager = new jCookieManager(new jCookieStore(id), CookiePolicy.ACCEPT_ALL);
        java.net.CookieHandler.setDefault(manager);
        jcm = manager;
        return manager;
    }

    public String pageFinished(android.webkit.WebView view, String url) {
        jcm = create(UserAccount);

        try {
            Log.once(String.format("try to save cookies [%s]", UserAccount));
            String cookie = cookieManager.getCookie(url);

            SharedPreferences.Editor editor = getEditor(getContext());
            editor.putString(Url.getUrlWithoutParameters(url), cookie);
            editor.commit();
            syncManager.sync();

            jcm.add(url, cookie);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            jcm.save();
        }

        SharedPreferences pref = getSharedPreferences(getContext());
        AllCookies = pref.getAll();
        Gson gson = new GsonBuilder().disableHtmlEscaping().disableInnerClassSerialization().setPrettyPrinting().create();
        return gson.toJson(AllCookies);
    }

    public static class jCookieStore implements java.net.CookieStore {

        private final static ThreadLocal<java.net.CookieStore> ms_cookieJars = new ThreadLocal<java.net.CookieStore>() {
            @Override
            protected synchronized java.net.CookieStore initialValue() {
                return (new java.net.CookieManager()).getCookieStore(); /*InMemoryCookieStore*/
            }
        };

        String identifier;

        public jCookieStore(String id) {
            identifier = id;
        }

        @SuppressWarnings({"ConstantConditions", "all"})
        public File getFile() {
            File externalStorage = App.mExternalStoragePath;
            File absolute = new File(externalStorage.getAbsolutePath(), "Facebot/cookie/" + identifier);
            if (!absolute.getParentFile().exists()) absolute.getParentFile().mkdirs();
            return absolute;
        }

        public void save() {
            if (identifier != null) {
                List<HttpCookie> cookie;
                try {
                    cookie = get(new URI("https://m.facebook.com"));
                    if (cookie.size() > 0) {
                        Log.i(cookie);
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }

        @NonNull
        @NotNull
        @Override
        public String toString() {
            return ms_cookieJars.toString();
        }

        @Override
        @SuppressWarnings({"null", "NullPointerException", "ConstantConditions"})
        public void add(URI uri, HttpCookie cookie) {
            ms_cookieJars.get().add(uri, cookie);
        }

        public void add(String url, String cookie) {
            if (url != null && cookie != null) {
                try {
                    URI uri = new URI(url);
                    String[] pairs = cookie.split(";");
                    for (String pair : pairs) {
                        String[] parse = pair.split("=", 2); // get first part of `=`
                        HttpCookie httpCookie = new HttpCookie(parse[0], parse[1]);
                        add(uri, httpCookie);
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }

        public void add(String rawCookie) {
            List<HttpCookie> parse = HttpCookie.parse(rawCookie);
            Log.i(parse);
        }

        @Override
        public List<HttpCookie> get(URI uri) {
            return ms_cookieJars.get().get(uri);
        }

        @Override
        public List<HttpCookie> getCookies() {
            return ms_cookieJars.get().getCookies();
        }

        @Override
        public List<URI> getURIs() {
            return ms_cookieJars.get().getURIs();
        }

        @Override
        public boolean remove(URI uri, HttpCookie cookie) {
            return ms_cookieJars.get().remove(uri, cookie);
        }

        @Override
        public boolean removeAll() {
            return ms_cookieJars.get().removeAll();
        }
    }

    public static class jCookieManager extends java.net.CookieManager {
        private android.webkit.CookieManager webkitCookieManager = android.webkit.CookieManager.getInstance();
        private jCookieStore jCookieStore;

        @SuppressWarnings({"unused"})
        public jCookieManager() {
            this(null, null);
        }

        @SuppressWarnings({"unused"})
        public jCookieManager(java.net.CookieStore store, CookiePolicy cookiePolicy) {
            super(null, cookiePolicy);
            this.webkitCookieManager = android.webkit.CookieManager.getInstance();
        }

        public jCookieManager(jCookieStore store, CookiePolicy cookiePolicy) {
            super(store, cookiePolicy);
            jCookieStore = store;
            this.webkitCookieManager = android.webkit.CookieManager.getInstance();
        }

        /**
         * Save Cookie Store
         */
        public void save() {
            jCookieStore.save();
        }

        @Override
        public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
            // make sure our args are valid
            if ((uri == null) || (responseHeaders == null)) return;

            // save our url once
            String url = uri.toString();

            // go over the headers
            for (String headerKey : responseHeaders.keySet()) {
                // ignore headers which aren't cookie related
                if ((headerKey == null) || !(headerKey.equalsIgnoreCase("Set-Cookie2") || headerKey.equalsIgnoreCase("Set-Cookie")))
                    continue;

                // process each of the headers
                for (String headerValue : responseHeaders.get(headerKey)) {
                    this.webkitCookieManager.setCookie(url, headerValue);
                }
            }
        }

        @Override
        public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
            // make sure our args are valid
            if ((uri == null) || (requestHeaders == null)) throw new IllegalArgumentException("Argument is null");

            // save our url once
            String url = uri.toString();

            // prepare our response
            Map<String, List<String>> res = new java.util.HashMap<String, List<String>>();

            // get the cookie
            String cookie = this.webkitCookieManager.getCookie(url);

            // return it
            if (cookie != null) res.put("Cookie", Collections.singletonList(cookie));
            return res;
        }

        @Override
        public java.net.CookieStore getCookieStore() {
            // we don't want anyone to work with this cookie store directly
            throw new UnsupportedOperationException();
        }

        public jCookieStore getjCookieStore() {
            return jCookieStore;
        }

        /**
         * Add Cookie to Cookie Store
         *
         * @param url    url
         * @param cookie cookiename=cookievalue
         */
        public void add(String url, String cookie) {
            getjCookieStore().add(url, cookie);
        }
    }
}
