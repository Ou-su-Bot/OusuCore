package me.skiincraft.ousucore.language;

import java.io.*;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle.Control;
import java.util.*;

public class UTF8BundleControl extends Control {

    public ResourceBundle newBundle (String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IOException {
        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, "properties");
        ResourceBundle bundle = null;
        InputStream stream = null;
        if (reload) {
            URL url = loader.getResource(resourceName);
            if (url != null) {
                URLConnection connection = url.openConnection();
                if (connection != null) {
                    connection.setUseCaches(false);
                    stream = connection.getInputStream();
                }
            }
        } else {
            stream = loader.getResourceAsStream(resourceName);
        }
        if (stream != null) {
            try {
                // Read properties files as user defined and if not provided then use "UTF-8" encoding.
                String encodingScheme = "UTF-8";
                bundle = new PropertyResourceBundle(new InputStreamReader(stream, encodingScheme));
            } finally {
                stream.close();
            }
        }
        return bundle;
    }
}