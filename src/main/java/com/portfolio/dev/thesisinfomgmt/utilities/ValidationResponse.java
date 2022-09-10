package com.portfolio.dev.thesisinfomgmt.utilities;

import com.portfolio.dev.thesisinfomgmt.dtos.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
@With
public class ValidationResponse {

  public enum ValidationResult {
    OK,
    NG
  }

  private ValidationResult validationResult;

  private HttpStatus httpStatus;

  private ErrorMessage errorMessage;
}
