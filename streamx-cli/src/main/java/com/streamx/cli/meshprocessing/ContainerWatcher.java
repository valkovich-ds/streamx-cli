package com.streamx.cli.meshprocessing;

import static java.lang.System.out;

import com.streamx.runner.event.ContainerFailed;
import com.streamx.runner.event.ContainerStarted;
import com.streamx.runner.event.ContainerStopped;
import jakarta.enterprise.event.Observes;

public class ContainerWatcher {
  void onContainerStarted(@Observes ContainerStarted event) {
    out.println("🟢 " + event.containerName() + " ready.");
  }

  void onContainerStopped(@Observes ContainerStopped event) {
    out.println("🔴 " + event.containerName() + " stopped.");
  }

  void onContainerFailed(@Observes ContainerFailed event) {
    out.println("❌ " + event.containerName() + " failed.");
  }
}
