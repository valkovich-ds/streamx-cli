package com.streamx.cli.config.validation;

import com.streamx.cli.commands.ingestion.IngestionClientConfig;
import com.streamx.cli.license.LicenseConfig;

public enum SecuredProperty {
  LICENSE_ACCEPT(LicenseConfig.STREAMX_ACCEPT_LICENSE,
      ConfigSourceName.CLASSPATH_PROPERTIES_CONFIG_SOURCE,
      ConfigSourceName.LOCAL_CONFIG_FILE_PROPERTIES_CONFIG_SOURCE
  ),
  INGESTION_AUTH_TOKEN(IngestionClientConfig.STREAMX_INGESTION_AUTH_TOKEN,
      ConfigSourceName.ARGUMENT_CONFIG_SOURCE,
      ConfigSourceName.CLASSPATH_PROPERTIES_CONFIG_SOURCE,
      ConfigSourceName.LOCAL_CONFIG_FILE_PROPERTIES_CONFIG_SOURCE
  ),
  ;

  private final String propertyName;
  private final ConfigSourceName[] forbiddenSources;

  SecuredProperty(String propertyName, ConfigSourceName... forbiddenSources) {
    this.propertyName = propertyName;
    this.forbiddenSources = forbiddenSources;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public ConfigSourceName[] getForbiddenSources() {
    return forbiddenSources;
  }
}
