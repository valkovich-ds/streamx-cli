package com.streamx.cli.util;

import com.streamx.cli.framework.CliException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class StreamxMavenPropertiesUtils {

  private static final Properties PROPERTIES;

  static {
    URL url = StreamxMavenPropertiesUtils.class.getResource("/streamx-maven.properties");
    Properties properties = null;
    if (url != null) {
      try {
        var loadedProperties = new Properties();
        loadedProperties.load(url.openStream());

        properties = loadedProperties;
      } catch (IOException e) {
        throw new CliException("Failed to initialize streamx-maven properties", e);
      }
    }

    PROPERTIES = properties;
  }

  public static String getStreamxCliVersion() {
    return getProperty("streamx.cli.version");
  }

  private static String getProperty(String key) {
    return PROPERTIES.getProperty(key);
  }

}
