package com.streamx.cli.commands.ingestion;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Optional;

import static com.streamx.cli.commands.ingestion.IngestionArguments.DEFAULT_INGESTION_URL;

@ConfigMapping
public interface IngestionClientConfig {

  String STREAMX_INGESTION_URL = "streamx.ingestion.url";
  String STREAMX_INGESTION_AUTH_TOKEN = "streamx.ingestion.auth-token";
  String STREAMX_INGESTION_INSECURE = "streamx.ingestion.insecure";

  @WithName(STREAMX_INGESTION_URL)
  @WithDefault(DEFAULT_INGESTION_URL)
  String url();

  @WithName(STREAMX_INGESTION_AUTH_TOKEN)
  Optional<String> authToken();

  @WithName(STREAMX_INGESTION_INSECURE)
  @WithDefault(BooleanUtils.FALSE)
  boolean insecure();
}
