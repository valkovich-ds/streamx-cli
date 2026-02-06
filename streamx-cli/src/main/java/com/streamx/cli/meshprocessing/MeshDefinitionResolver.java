package com.streamx.cli.meshprocessing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.streamx.cli.interpolation.InterpolatingYamlFactory;
import com.streamx.cli.interpolation.InterpolationSupport;
import com.streamx.mesh.model.ServiceMesh;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.file.Path;

@ApplicationScoped
public class MeshDefinitionResolver {
  public ServiceMesh resolve(Path meshPath) throws IOException {
    ServiceMesh mesh = null;
    try {
      System.out.println("Step 1: Creating InterpolationSupport");
      InterpolationSupport interpolationSupport = new InterpolationSupport();

      System.out.println("Step 2: Creating InterpolatingYamlFactory");
      InterpolatingYamlFactory factory = new InterpolatingYamlFactory(interpolationSupport);

      System.out.println("Step 3: Creating ObjectMapper");
      ObjectMapper objectMapper = new ObjectMapper(factory);

      System.out.println("Step 4: MeshDefinitionResolver path: " + meshPath.toString());
      System.out.println("Step 5: File exists: " + meshPath.toFile().exists());
      System.out.println("Step 6: File readable: " + meshPath.toFile().canRead());

      System.out.println("Step 7: About to call readValue");
      mesh = objectMapper.readValue(meshPath.toFile(), ServiceMesh.class);

      System.out.println("Step 8: MeshDefinitionResolver read value SUCCESS");
    } catch (Exception e) {
      System.err.println("Exception type: " + e.getClass().getName());
      System.err.println("Exception message: " + e.getMessage());
      System.err.println("Full stack trace:");
      e.printStackTrace(System.err);
      throw e; // Re-throw so caller knows it failed
    }

    return mesh;
  }
}