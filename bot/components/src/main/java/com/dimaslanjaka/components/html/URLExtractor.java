package com.dimaslanjaka.components.html;

import com.dimaslanjaka.components.log.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class URLExtractor {
    public static void from(String url) throws IOException {
        File input = new File("input.html");
        Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
        Elements links = doc.select("a[href]");
        for (Element element : links) {
            Log.i(element.getElementsByAttribute("href"));
        }
    }
}
