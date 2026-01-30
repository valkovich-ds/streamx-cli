package com.streamx.cli.commands.ingestion.batch.resolver.step;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface ResolverStep {

  JsonNode resolve(JsonNode payload, Map<String, String> variables);
}
