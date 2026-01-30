package com.streamx.cli.commands.ingestion.batch.resolver;

import com.fasterxml.jackson.databind.JsonNode;
import com.streamx.cli.commands.ingestion.batch.EventSourceDescriptor;

import java.util.Map;

public interface BatchPayloadResolver {

  JsonNode createPayload(EventSourceDescriptor currentDescriptor, Map<String, String> variables);
}
