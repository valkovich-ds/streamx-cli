package com.streamx.cli.commands.local.run;

import com.streamx.cli.framework.AbstractSilentCommand;
import com.streamx.cli.framework.CliException;
import com.streamx.cli.framework.CommandResult;
import com.streamx.runner.StreamxRunner;
import com.streamx.runner.exception.ContainerStartupTimeoutException;
import com.streamx.cli.exception.DockerException;
import com.streamx.cli.meshprocessing.MeshConfig;
import com.streamx.cli.meshprocessing.MeshManager;
import com.streamx.cli.meshprocessing.MeshResolver;
import com.streamx.cli.meshprocessing.MeshSource;
import com.streamx.cli.util.BannerPrinter;
import jakarta.inject.Inject;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;

import java.nio.file.Path;

@Command(name = "run",
    mixinStandardHelpOptions = true,
    description = "Run a StreamX Mesh locally.")
public class RunCommand extends AbstractSilentCommand {

  @ArgGroup
  MeshSource meshSource;

  @Inject
  MeshConfig meshConfig;

  @Inject
  MeshResolver meshResolver;

  @Inject
  StreamxRunner runner;

  @Inject
  BannerPrinter bannerPrinter;

  @Inject
  MeshManager meshManager;

  @Override
  public CommandResult<Void> runCommand() {
    try {
      Path meshPath = meshResolver.resolveMeshPath(meshConfig);
      meshManager.initializeMesh(meshPath);

      bannerPrinter.printBanner();
      meshManager.initializeRunMode(meshPath);

      meshManager.start();

      return new CommandResult<>(null);
    } catch (ContainerStartupTimeoutException e) {
      DockerException dockerException = DockerException.containerStartupFailed(
          e.getContainerName(),
          runner.getContext().getStreamxBaseConfig().getContainerStartupTimeout());

      throw new CliException(dockerException.getMessage(), dockerException);
    }
  }
}
