package com.portfolio.dev.thesisinfomgmt.utilities;

import com.portfolio.dev.thesisinfomgmt.dtos.ErrorMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
@Setter
public class ValidationResponse {

  public enum ValidationResult {
    OK,
    NG
  }

  private ValidationResult validationResult;

  private HttpStatus httpStatus;

  private ErrorMessage errorMessage;
}
