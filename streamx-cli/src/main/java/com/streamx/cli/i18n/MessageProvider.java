package com.streamx.cli.i18n;

import com.streamx.runner.config.StreamxBaseConfig;

public class MessageProvider {

  public static final MessageProvider msg = new MessageProvider();

  public String unsupportedOutputFormat() {
    return "Unsupported output format";
  }

  public String tryForMoreInformationOnAvailableOptions(
      String qualifiedCommandName,
      String helpOptionName) {
    return String.format(
        "Try '%s%s' for more information on the available options.%n",
        qualifiedCommandName,
        helpOptionName);
  }

  public String failedToHandleInteractiveInput() {
    return "Failed to handle interactive input";
  }

  public String unableToSerializeJson() {
    return "Unable to serialize JSON";
  }

  public String somethingWentWrong() {
    return """
        ❌ Something went wrong while running the command.
        
        Please try again with `--verbose` for more details.
        If the problem persists, report it here:
        https://www.streamx.dev/contact-us.html
        """;
  }

  public String noSettingsPropertyFound(String key) {
    return String.format("No such settings property found: %s", key);
  }

  public String unableToGetSettingsProperty() {
    return "Unable to get settings property";
  }

  public String failedToLoadPropertiesFrom(String path) {
    return String.format("Failed to load properties from: %s", path);
  }

  public String unableToSetSettingsProperty() {
    return "Unable to set settings property";
  }

  public String unableToGetSettingsFilePath() {
    return "Unable to get settings file path";
  }

  public String dockerContainerStartupFailed(String containerName, Long timeoutSecs) {
    return String.format(
        """
        Timeout exceeded waiting for the container "%s" after %d seconds.
                
        Try increasing the timeout by setting the %s property.""",
        containerName,
        timeoutSecs,
        StreamxBaseConfig.PN_CONTAINER_STARTUP_TIMEOUT_SECONDS);
  }

  public String failedToStartMeshContainers(String reason) {
    return String.format("Failed to start mesh containers. %s", reason);
  }

  public String invalidDockerEnvironment() {
    return """
        Could not find a valid Docker environment.

        Make sure that:
         * Docker is installed,
         * Docker is running""";
  }

  public String dockerContainerStarted(String containerName) {
    return String.format("🟢 %s ready.", containerName);
  }

  public String dockerContainerStopped(String containerName) {
    return String.format("🟢 %s stopped.", containerName);
  }

  public String dockerContainerFailed(String containerName) {
    return String.format("❌ %s failed.", containerName);
  }

  public String meshFileDeleted() {
    return "Mesh file deleted. Stopping...";
  }

  public String meshStopped() {
    return "Mesh stopped.";
  }

  public String skippingUnknownAction(String action) {
    return String.format("Unknown action: %s. Skipping...", action);
  }

  public String failedToWatchMeshChanges() {
    return "Failed to watch mesh changes.";
  }

  public String settingUpSystemContainers() {
    return "Setting up system containers...";
  }

  public String startingMesh() {
    return "Starting DX mesh...";
  }

  public String stoppingMesh() {
    return "Stopping DX mesh...";
  }

  public String unableToReadMeshDefinition(String fromPath, String details) {
    return String.format(
        """
        Unable to read mesh definition from %s.
        
        Details:
        %s""",
        fromPath,
        details);
  }

  public String meshDefinitionIsInvalidSkipReload() {
    return "Mesh definition is invalid. Skip reloading...";
  }

  public String meshDefinitionIsUnchangedSkipReload() {
    return "Mesh definition is unchanged. Skip reloading...";
  }

  public String meshFileChangedFullReload() {
    return "Mesh file changed. Processing full reload...";
  }

  public String meshFileChangedIncrementalReload() {
    return "Mesh file changed. Processing incremental reload...";
  }

  public String meshReloaded() {
    return "Mesh reloaded.";
  }

  public String meshReloadFailed() {
    return "Mesh reload failed.";
  }

  public String fullLogsCanBeFoundIn(String originalMessage, String logPath) {
    return String.format(
        """
        %s
          
        Full logs can be found in %s""",
        originalMessage,
        logPath);
  }

  public String executionExceptionOccurred() {
    return "Execution exception occurred.";
  }

  public String inputPathMustNotBeNull() {
    return "Input path must not be null";
  }

  public String pathDoesNotHaveParentLevels(String path, int parentLevelsCount) {
    return String.format("Path %s does not have %s parent levels.", path, parentLevelsCount);
  }

  public String failedToInitializeStreamxMavenProperties() {
    return "Failed to initialize streamx-maven properties.";
  }

  public String noVersionInformationIncluded() {
    return "No version information included.";
  }

  public String expressionCannotBeNull() {
    return "Expression cannot be null";
  }

  public String couldNotExpandValueInExpression(String key, String expression) {
    return String.format("Could not expand value %s in expression %s", key, expression);
  }

  public String meshFileNotFound(String path) {
    return String.format("Mesh file not found at: %s", path);
  }
}