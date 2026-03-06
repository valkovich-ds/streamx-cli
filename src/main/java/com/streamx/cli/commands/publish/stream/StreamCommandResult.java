package com.streamx.cli.commands.publish.stream;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public record StreamCommandResult(
    int successCount,
    int failureCount,
    int unknownCount,
    List<EventError> eventErrors,

    int batchSuccessCount,
    int batchFailureCount,
    List<BatchError> batchErrors
) {

  @RegisterForReflection
  public record EventError(
      Integer eventNumber,
      Integer batchNumber,
      String type,
      String subject,
      String errorMessage
  ) {
  }

  @RegisterForReflection
  public record BatchError(
      int batchNumber,
      int eventCount,
      String errorMessage
  ) {
  }
}
