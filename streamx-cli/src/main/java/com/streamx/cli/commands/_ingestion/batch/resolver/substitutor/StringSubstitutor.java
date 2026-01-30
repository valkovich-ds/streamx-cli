package com.streamx.cli.commands.ingestion.batch.resolver.substitutor;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

import static java.util.Objects.requireNonNull;

@ApplicationScoped
public class StringSubstitutor implements Substitutor {

  @Override
  public Map<String, String> createSubstitutionVariables(String payloadPath,
      String eventType, String eventSource, String relativePath) {
    return Map.of(
        "payloadPath", requireNonNull(payloadPath),
        "eventType", requireNonNull(eventType),
        "eventSource", requireNonNull(eventSource),
        "relativePath", requireNonNull(relativePath)
    );
  }

  @Override
  public String substitute(Map<String, String> variables, String text) {
    String result = text;
    for (Map.Entry<String, String> entry : variables.entrySet()) {
      // FixMe this should be a better solution
      result = result.replace("${" + entry.getKey() + "}", entry.getValue());
    }
    return result;
  }
}
