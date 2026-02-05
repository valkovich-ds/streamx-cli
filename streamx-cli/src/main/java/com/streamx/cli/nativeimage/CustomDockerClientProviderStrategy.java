package com.streamx.cli.nativeimage;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.streamx.cli.framework.CliException;
import org.testcontainers.dockerclient.DockerClientProviderStrategy;
import org.testcontainers.dockerclient.TransportConfig;

import java.net.URI;
import java.time.Duration;

public final class CustomDockerClientProviderStrategy
    extends DockerClientProviderStrategy {

  @Override
  public TransportConfig getTransportConfig() {
    URI dockerSock;
    try {
      dockerSock = new URI("unix:///var/run/docker.sock");
    } catch (Exception e) {
      throw new CliException("Unable to init " + CustomDockerClientProviderStrategy.class.getName(), e);
    }

    return TransportConfig.builder()
        .dockerHost(dockerSock)
        .build();
  }

  @Override
  public DockerClient getDockerClient() {
    TransportConfig transportConfig = getTransportConfig();

    DockerHttpClient httpClient =
        new ApacheDockerHttpClient.Builder()
            .dockerHost(transportConfig.getDockerHost())
            .sslConfig(transportConfig.getSslConfig())
            .connectionTimeout(Duration.ofSeconds(30))
            .build();

    return DockerClientBuilder.getInstance()
        .withDockerHttpClient(httpClient)
        .build();
  }

  @Override
  public boolean isApplicable() {
    return true; // force usage
  }

  @Override
  public String getDescription() {
    return "Custom native-image-safe Docker client strategy";
  }
}
