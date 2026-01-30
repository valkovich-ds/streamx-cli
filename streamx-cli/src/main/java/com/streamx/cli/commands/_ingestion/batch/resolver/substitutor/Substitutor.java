package com.streamx.cli.commands.ingestion.batch.resolver.substitutor;

import java.util.Map;

public interface Substitutor {

  Map<String, String> createSubstitutionVariables(String file, String eventType, String eventSource,
      String relativePath);

  String substitute(Map<String, String> variables, String text);
}
