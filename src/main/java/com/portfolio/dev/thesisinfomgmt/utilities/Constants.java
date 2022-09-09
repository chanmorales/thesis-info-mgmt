package com.portfolio.dev.thesisinfomgmt.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

  // Error messages
  public static final String ROLE_NAME_REQUIRED = "Role name is required.";
  public static final String ROLE_NAME_ALREADY_EXISTS = "Role with name '%s' already exists.";
  public static final String ROLE_NOT_FOUND = "Role with id '%d' not found.";
}
