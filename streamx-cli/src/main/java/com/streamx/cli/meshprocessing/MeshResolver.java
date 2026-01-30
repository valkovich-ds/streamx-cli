package com.streamx.cli.meshprocessing;

import org.jetbrains.annotations.NotNull;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

import java.nio.file.Path;
import java.util.Optional;
import static java.lang.System.out;

public class MeshResolver {
  @Spec
  CommandSpec spec;

  public static final String MESH_YAML = "mesh.yaml";
  public static final String MESH_YML = "mesh.yml";

  @NotNull
  public Path resolveMeshPath(MeshConfig meshConfig) {
    return resolveMeshPath(meshConfig, true);
  }

  @NotNull
  public Path resolveMeshPath(MeshConfig meshConfig, boolean requireMeshExistence) {
    return Optional.ofNullable(meshConfig)
        .flatMap(MeshConfig::meshDefinitionFile)
        .map(Path::of)
        .orElseGet(() -> resolveCurrentDirectoryMeshPath(requireMeshExistence));
  }

  @NotNull
  private Path resolveCurrentDirectoryMeshPath(boolean requireMeshExistence) {
    String currentDirectory = System.getProperty("user.dir");
    Path pathToYaml = Path.of(currentDirectory, MESH_YAML);
    Path pathToYml = Path.of(currentDirectory, MESH_YML);

    var yamlExists = pathToYaml.toFile().exists();
    var ymlExists = pathToYml.toFile().exists();

    if (yamlExists && ymlExists) {
      out.printf("Warning! Both '%s' and '%s' exist. Starting '%s' as it has higher priority.%n",
          pathToYaml, pathToYml, pathToYaml);
    }

    if (yamlExists) {
      return pathToYaml;
    } else if (ymlExists) {
      return pathToYml;
    } else if (requireMeshExistence) {
      throw new ParameterException(spec.commandLine(),
          "Missing mesh definition. Use '-f' to select mesh file or "
          + "make sure 'mesh.yaml' (or 'mesh.yml') exists in current directory.");
    } else {
      out.printf("Warning! Neither '%s' nor '%s' exist. Selecting '%s' as mesh file definition.%n",
          pathToYaml, pathToYml, pathToYaml);
      return pathToYaml;
    }
  }
}
