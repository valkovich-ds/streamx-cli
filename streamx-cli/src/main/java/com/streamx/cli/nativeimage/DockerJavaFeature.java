package com.streamx.cli.nativeimage;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;

public class DockerJavaFeature implements Feature {

  @Override
  public void beforeAnalysis(BeforeAnalysisAccess access) {
    // Initialize docker-java classes at runtime
    RuntimeClassInitialization.initializeAtRunTime(
        "com.github.dockerjava.transport.NamedPipeSocket",
        "com.github.dockerjava.transport.NamedPipeSocket$Kernel32"
    );
  }
}