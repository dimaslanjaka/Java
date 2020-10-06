package com.dimaslanjaka.tools.Helpers.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class htmlparser {
  static Document doc;

  public static Document fromUrl(String url) {
    try {
      doc = Jsoup.connect(url.trim()).get();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return doc;
  }
}
