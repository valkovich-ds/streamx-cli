package com.streamx.cli.commands.ingestion;

import com.streamx.clients.ingestion.StreamxClient;
import com.streamx.clients.ingestion.exceptions.StreamxClientException;
import com.streamx.clients.ingestion.publisher.Publisher;
import com.streamx.cli.exception.IngestionClientException;
import com.streamx.cli.util.ExceptionUtils;
import jakarta.inject.Inject;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

import javax.net.ssl.SSLHandshakeException;

public abstract class BaseIngestionCommand implements Runnable {

  @ArgGroup(exclusive = false)
  IngestionArguments ingestionArguments;

  @Spec
  protected CommandSpec spec;

  @Inject
  StreamxClientProvider streamxClientProvider;

  @Inject
  IngestionClientConfig ingestionClientConfig;

  protected abstract void perform(Publisher publisher) throws StreamxClientException;

  @Override
  public final void run() {
    try (StreamxClient client = streamxClientProvider.createStreamxClient(ingestionClientConfig)) {
      Publisher publisher = client.newPublisher();
      perform(publisher);
    } catch (StreamxClientException e) {
      if (e.getCause() instanceof SSLHandshakeException) {
        throw IngestionClientException.sslException(ingestionClientConfig.url());
      }
      throw ExceptionUtils.sneakyThrow(e);
    }
  }
}
