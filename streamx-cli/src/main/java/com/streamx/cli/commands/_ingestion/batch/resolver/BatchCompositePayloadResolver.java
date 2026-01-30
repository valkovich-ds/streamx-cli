package com.streamx.cli.commands.ingestion.batch.resolver;

import com.fasterxml.jackson.databind.JsonNode;
import com.streamx.cli.commands.ingestion.batch.EventSourceDescriptor;
import com.streamx.cli.commands.ingestion.batch.resolver.step.BinaryResolverStep;
import com.streamx.cli.commands.ingestion.batch.resolver.step.JsonResolverStep;
import com.streamx.cli.commands.ingestion.batch.resolver.step.VariablesResolverStep;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;

@ApplicationScoped
public class BatchCompositePayloadResolver implements BatchPayloadResolver {

  @Inject
  VariablesResolverStep variablesResolverStep;
  @Inject
  BinaryResolverStep binaryResolverStep;
  @Inject
  JsonResolverStep jsonResolverStep;

  @Override
  public JsonNode createPayload(EventSourceDescriptor currentDescriptor,
      Map<String, String> variables) {

    JsonNode payload = currentDescriptor.getPayload();
    // This has to be first to be able to find payloads
    payload = variablesResolverStep.resolve(payload, variables);
    // Binary and JSON are replaceable
    payload = binaryResolverStep.resolve(payload, variables);
    return jsonResolverStep.resolve(payload, variables);
  }
}
