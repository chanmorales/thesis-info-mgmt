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

  /**
   * Creates a validation response with bad request status, NG validation result and specified
   * error message.
   *
   * @param errorMessage validation error message
   * @return bad request validation response
   */
  public static ValidationResponse badRequest(String errorMessage) {
    return new ValidationResponse(
        ValidationResult.NG,
        HttpStatus.BAD_REQUEST,
        new ErrorMessage(errorMessage));
  }

  /**
   * Creates a validation response with OK validation result and having none of both
   * response status and error message
   *
   * @return ok validation response
   */
  public static ValidationResponse ok() {
    return new ValidationResponse(ValidationResult.OK, null, null);
  }
}
