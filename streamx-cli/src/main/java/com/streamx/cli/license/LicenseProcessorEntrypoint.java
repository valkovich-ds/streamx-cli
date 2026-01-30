package com.streamx.cli.license;

import com.streamx.cli.exception.LicenseException;
import com.streamx.cli.exception.PublicSettingsFileException;
import com.streamx.cli.exception.SettingsFileException;
import com.streamx.cli.license.input.AcceptingStrategy;
import com.streamx.cli.license.model.LastLicenseFetch;
import com.streamx.cli.license.model.LicenseSettings;
import com.streamx.cli.license.proceeding.LicenseProceedingStrategy;
import com.streamx.cli.util.BannerPrinter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

import static com.streamx.cli.util.Output.print;

@ApplicationScoped
public class LicenseProcessorEntrypoint {

  private static final String LICENSE = """
      Distributed under %s
      %s
      """;

  @Inject
  Logger log;

  @Inject
  BannerPrinter bannerPrinter;

  @Inject
  LicenseProceedingStrategy licenseProceeding;

  @Inject
  LicenseDataRefresher licenseDataRefresher;

  @Inject
  LicenseAcceptanceVerifier licenseAcceptanceVerifier;

  @Inject
  LicenseSettingsStore licenseSettingsStore;

  @Inject
  AcceptingStrategy acceptingStrategy;

  @Inject
  LicenseConfig licenseConfig;

  public void process() {
    try {
      doProcess();
    } catch (SettingsFileException exception) {
      throw new PublicSettingsFileException(exception.getPathToSettings(), exception.getCause());
    }
  }

  private void doProcess() {
    if (!licenseProceeding.isEnabled()) {
      return;
    }

    LocalDateTime now = LocalDateTime.now();
    LicenseSettings refreshedLicenseSettings = licenseDataRefresher.refresh(now);
    boolean acceptanceRequired = licenseAcceptanceVerifier
        .isAcceptanceRequired(refreshedLicenseSettings);
    LicenseSettings licenseSettings = licenseSettingsStore.retrieveSettings();

    if (acceptanceRequired) {
      licenseSettings.lastLicenseFetch().ifPresent(settings -> print(getLicenseText(settings)));
      proceedLicenseAcceptance(licenseSettings, now);
    }
  }

  private void proceedLicenseAcceptance(LicenseSettings licenseSettings, LocalDateTime now) {
    bannerPrinter.printBanner();

    print("");
    print("Do you accept the license agreement? [Y/n]");

    if (licenseConfig.acceptLicense()) {
      proceedAutomaticLicenseAcceptance(licenseSettings, now);
      return;
    }

    if (getAcceptingStrategy().isLicenseAccepted()) {
      licenseSettingsStore.acceptLicense(licenseSettings, now);
    } else {
      throw LicenseException.licenseAcceptanceRejectedException();
    }
  }

  private void proceedAutomaticLicenseAcceptance(LicenseSettings licenseSettings,
      LocalDateTime now) {
    print("Y -> \"" + LicenseConfig.STREAMX_ACCEPT_LICENSE + "\" property was set to \"true\"");
    if (licenseSettings.lastLicenseFetch().isPresent()) {
      licenseSettingsStore.acceptLicense(licenseSettings, now);
    } else {
      log.warn("Couldn't update accepted license data because of missing license data.");
    }
  }

  @NotNull
  private static String getLicenseText(LastLicenseFetch licenseSettings) {
    return LICENSE.formatted(
        licenseSettings.licenseName(),
        licenseSettings.licenseUrl()
    );
  }

  AcceptingStrategy getAcceptingStrategy() {
    return acceptingStrategy;
  }
}
