package com.streamx.cli.interpolation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.arc.All;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;  // Changed import!
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ObjectMapperProducer {

  @Inject
  InterpolationSupport interpolationSupport;

  @Produces
  @Interpolating
  @ApplicationScoped
  public ObjectMapper produce(@All List<ObjectMapperCustomizer> customizers) {  // Added 'public'
    System.out.println("Producing @Interpolating ObjectMapper");  // Debug log
    ObjectMapper mapper = new ObjectMapper(new InterpolatingYamlFactory(interpolationSupport));

    System.out.println("Produced @Interpolating ObjectMapper");
    // Apply all ObjectMapperCustomizer beans (incl. Quarkus)
    for (ObjectMapperCustomizer customizer : customizers) {
      customizer.customize(mapper);
    }
    return mapper;
  }
}