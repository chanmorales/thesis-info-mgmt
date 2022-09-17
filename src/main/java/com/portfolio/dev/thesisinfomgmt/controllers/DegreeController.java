package com.portfolio.dev.thesisinfomgmt.controllers;

import static com.portfolio.dev.thesisinfomgmt.utilities.Constants.DEGREE_NOT_FOUND;

import com.portfolio.dev.thesisinfomgmt.dtos.DegreeDTO;
import com.portfolio.dev.thesisinfomgmt.dtos.ErrorMessage;
import com.portfolio.dev.thesisinfomgmt.services.DegreeService;
import com.portfolio.dev.thesisinfomgmt.utilities.ValidationResponse;
import com.portfolio.dev.thesisinfomgmt.utilities.ValidationResponse.ValidationResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/degrees")
public class DegreeController {

  private final DegreeService degreeService;

  public DegreeController(
      DegreeService degreeService
  ) {
    this.degreeService = degreeService;
  }

  /**
   * Retrieves list of all degrees
   *
   * @return 200 / OK if successful. Every other results indicates an error.
   */
  @Operation(summary = "Retrieves list of all degrees.",
      description = "Retrieves list of all degrees for Thesis Information Management System.",
      tags = "Degree")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Degrees successfully retrieved.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = DegreeDTO.class)),
              examples = @ExampleObject(value =
                  "["
                + "    {"
                + "        \"id\": 1,\n"
                + "        \"abbr\": \"BSCS\","
                + "        \"name\": \"Bachelor of Science in Computer Science\""
                + "    }"
                + "]")))
  })
  @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<Object> getAllDegrees() {
    List<DegreeDTO> retrievedDegrees = degreeService.getAllDegrees();
    return ResponseEntity.ok().body(retrievedDegrees);
  }

  /**
   * Creates a new degree
   *
   * @param newDegree degree details
   * @return 201 / CREATED if successful. Every other results indicates an error.
   */
  @Operation(summary = "Creates a new degree.",
      description = "Creates a new degree for Thesis Information Management System.",
      tags = "Degree")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Degree successfully created.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DegreeDTO.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"id\": 0,\n"
                + "    \"abbr\": \"BSCS\","
                + "    \"name\": \"Bachelor of Science in Computer Science\""
                + "}"))),
      @ApiResponse(responseCode = "400", description = "Degree was not created due to some" +
          " invalid details.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorMessage.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"message\": \"Degree with abbreviation '<ABBR>' already exists.\""
                + "}")))
  })
  @PostMapping(value = "",
      produces = { MediaType.APPLICATION_JSON_VALUE },
      consumes = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<Object> createDegree(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "New degree request data", required = true,
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DegreeDTO.class,
                  requiredProperties = { "abbr", "name" }),
              examples = @ExampleObject(value =
                  "{"
                + "    \"id\": 0,\n"
                + "    \"abbr\": \"BSCS\","
                + "    \"name\": \"Bachelor of Science in Computer Science\""
                + "}")))
      @RequestBody DegreeDTO newDegree
  ) {
    // Validate new degree details
    ValidationResponse validationResponse =
        degreeService.validateDegree(newDegree.getId(), newDegree);
    if (validationResponse.getValidationResult() == ValidationResult.NG) {
      return ResponseEntity
          .status(validationResponse.getHttpStatus())
          .body(validationResponse.getErrorMessage());
    }

    // Create the new degree
    DegreeDTO createdDegree = degreeService.createDegree(newDegree);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdDegree);
  }

  /**
   * Retrieves a degree
   *
   * @param degreeId id of the degree to be retrieved
   * @return 200 / OK if successful. Every other results indicates an error.
   */
  @Operation(summary = "Retrieves a degree.",
      description = "Retrieves a degree for Thesis Information Management System.",
      tags = "Degree")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Degree successfully retrieved.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DegreeDTO.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"id\": 1,\n"
                + "    \"abbr\": \"BSCS\","
                + "    \"name\": \"Bachelor of Science in Computer Science\""
                + "}"))),
      @ApiResponse(responseCode = "404", description = "Degree to be retrieved not found.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorMessage.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"message\": \"Degree with id '1' not found.\""
                + "}")))
  })
  @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<Object> getDegree(
      @Parameter(name = "id", description = "Id of the degree to be retrieved")
      @PathVariable(name = "id") long degreeId
  ) {
    Optional<DegreeDTO> retrievedDegree = degreeService.getDegree(degreeId);
    if (retrievedDegree.isPresent()) {
      return ResponseEntity.ok(retrievedDegree.get());
    } else {
      ErrorMessage errorMessage = new ErrorMessage(String.format(DEGREE_NOT_FOUND, degreeId));
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
  }

  /**
   * Updates a degree
   *
   * @param degreeId      id of the degree to be updated
   * @param updatedDegree updated degree details
   * @return 200 / OK if successful. Every other result indicates an error.
   */
  @Operation(summary = "Updates a degree.",
      description = "Updates a degree for Thesis Information Management System.",
      tags = "Degree")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Degree successfully updated.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DegreeDTO.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"id\": 1,\n"
                + "    \"abbr\": \"BSCS-2\","
                + "    \"name\": \"Bachelor of Science in Computer Science - Updated\""
                + "}"))),
      @ApiResponse(responseCode = "400", description = "Degree was not updated due to some" +
          " invalid details.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorMessage.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"message\": \"Degree with abbreviation '<ABBR>' already exists.\""
                + "}"))),
      @ApiResponse(responseCode = "404", description = "Degree was not updated due to resource" +
          " does not exists.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorMessage.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"message\": \"Degree with id '1' not found.\""
                + "}")))
  })
  @PutMapping(value = "/{id}",
      produces = { MediaType.APPLICATION_JSON_VALUE },
      consumes = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<Object> updateDegree(
      @Parameter(name = "id", description = "Id of the degree to be updated.")
      @PathVariable(name = "id") long degreeId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Updated degree request data", required = true,
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DegreeDTO.class,
                  requiredProperties = { "abbr", "name" }),
              examples = @ExampleObject(value =
                  "{"
                + "    \"id\": 1,\n"
                + "    \"abbr\": \"BSCS-2\","
                + "    \"name\": \"Bachelor of Science in Computer Science - Updated\""
                + "}")))
      @RequestBody DegreeDTO updatedDegree
  ) {
    // Validate degree details
    ValidationResponse validationResponse = degreeService.validateDegree(degreeId, updatedDegree);
    if (validationResponse.getValidationResult() == ValidationResult.NG) {
      return ResponseEntity
          .status(validationResponse.getHttpStatus())
          .body(validationResponse.getErrorMessage());
    }

    Optional<DegreeDTO> optUpdatedDegree = degreeService.updateDegree(degreeId, updatedDegree);
    if (optUpdatedDegree.isPresent()) {
      return ResponseEntity.ok(optUpdatedDegree.get());
    } else {
      ErrorMessage errorMessage = new ErrorMessage(String.format(DEGREE_NOT_FOUND, degreeId));
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
  }

  /**
   * Deletes a degree
   *
   * @param degreeId id of the degree to be deleted
   * @return 200 / OK if successful. Every other results indicates an error.
   */
  @Operation(summary = "Deletes a degree.",
      description = "Deletes a degree for Thesis Information Management System.", tags = "Degree")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Degree successfully deleted.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DegreeDTO.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"id\": 1,\n"
                + "    \"abbr\": \"BSCS\","
                + "    \"name\": \"Bachelor of Science in Computer Science\""
                + "}"))),
      @ApiResponse(responseCode = "404", description = "Degree was not deleted due to resource" +
          " does not exists or it is already deleted.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorMessage.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"message\": \"Degree with id '1' not found.\""
                + "}")))
  })
  @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<Object> deleteDegree(
      @Parameter(name = "id", description = "Id of the degree to be deleted.")
      @PathVariable(name = "id") long degreeId
  ) {
    Optional<DegreeDTO> deletedDegree = degreeService.deleteDegree(degreeId);
    if (deletedDegree.isPresent()) {
      return ResponseEntity.ok(deletedDegree.get());
    } else {
      ErrorMessage errorMessage = new ErrorMessage(String.format(DEGREE_NOT_FOUND, degreeId));
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
  }
}
