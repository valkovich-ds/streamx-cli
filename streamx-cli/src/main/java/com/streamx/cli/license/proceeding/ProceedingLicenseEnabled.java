package com.streamx.cli.license.proceeding;

public class ProceedingLicenseEnabled implements LicenseProceedingStrategy {

  @Override
  public boolean isEnabled() {
    return true;
  }
}
