package com.streamx.cli.license;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.streamx.cli.license.input.AcceptingStrategy;
import com.streamx.cli.license.input.StdInLineReadStrategy;
import com.streamx.cli.license.proceeding.LicenseProceedingStrategy;
import com.streamx.cli.license.proceeding.ProceedingLicenseDisabled;
import com.streamx.cli.license.proceeding.ProceedingLicenseEnabled;
import com.streamx.cli.license.source.ConfigurableLicenseSource;
import com.streamx.cli.license.source.LicenseSource;
import com.streamx.cli.license.source.ProdLicenseSource;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.arc.properties.IfBuildProperty;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import static com.streamx.cli.license.source.ProdLicenseSource.PROD_LICENSE_SOURCE_URL;

@Dependent
class LicenseBeanConfiguration {

  @Produces
  @Singleton
  @LicenseProcessing
  ObjectMapper licensesProcessingObjectMapper() {
    return new ObjectMapper(new YAMLFactory());
  }

  @Produces
  @DefaultBean
  AcceptingStrategy stdInLineReadValue() {
    return new StdInLineReadStrategy();
  }

  @Produces
  @DefaultBean
  LicenseProceedingStrategy proceedingLicenseEnabled() {
    return new ProceedingLicenseEnabled();
  }

  @Produces
  @IfBuildProperty(name = "streamx.cli.license.proceeding.enabled", stringValue = "false")
  LicenseProceedingStrategy proceedingLicenseDisabled() {
    return new ProceedingLicenseDisabled();
  }

  @Produces
  @IfBuildProfile("prod")
  LicenseSource prodLicenseSource() {
    return new ProdLicenseSource();
  }

  @Produces
  @DefaultBean
  LicenseSource configurableLicenseSource(
      @ConfigProperty(name = "streamx.cli.license.current-license-url",
          defaultValue = PROD_LICENSE_SOURCE_URL)
      String url
  ) {
    return new ConfigurableLicenseSource(url);
  }
}
