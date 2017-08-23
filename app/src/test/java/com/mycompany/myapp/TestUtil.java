package com.mycompany.myapp;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;

import okio.Okio;

public class TestUtil {
    public static String loadResourceAsString(String path) throws Exception {
        URL url = TestUtil.class.getResource(path);
        File file = new File(url.getFile());
        return Okio.buffer(Okio.source(file)).readString(Charset.forName("UTF-8"));
    }
}
