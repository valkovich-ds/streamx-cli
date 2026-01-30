package com.streamx.cli.license;

import com.streamx.cli.license.model.LastLicenseFetch;
import com.streamx.cli.license.model.LicenseSettings;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@ApplicationScoped
class LicenseAcceptanceVerifier {

  boolean isAcceptanceRequired(LicenseSettings licenseSettings) {
    String licenseUrlRequiredToBeAccepted = licenseSettings.lastLicenseFetch()
        .map(LastLicenseFetch::licenseUrl)
        .orElseThrow(() -> new IllegalStateException(
            "Acceptance is required only if it's preceded by successful fetch"));

    boolean requiredLicenseAccepted = licenseSettings.licenseApprovals().stream()
        .filter(Objects::nonNull)
        .anyMatch(licenseApproval ->
            StringUtils.equals(licenseApproval.url(), licenseUrlRequiredToBeAccepted));

    return !requiredLicenseAccepted;
  }
}
