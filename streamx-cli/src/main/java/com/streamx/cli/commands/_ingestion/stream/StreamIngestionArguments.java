package com.streamx.cli.commands.ingestion.stream;

import picocli.CommandLine.Parameters;

public class StreamIngestionArguments {

  @Parameters(index = "0", description = "Source file for the stream publication. "
      + "Can contain one or many events", arity = "1")
  String sourceFile;

  public String getSourceFile() {
    return sourceFile;
  }
}
