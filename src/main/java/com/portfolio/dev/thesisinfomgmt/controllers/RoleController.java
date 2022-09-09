package com.portfolio.dev.thesisinfomgmt.controllers;

import static com.portfolio.dev.thesisinfomgmt.utilities.Constants.ROLE_NOT_FOUND;

import com.portfolio.dev.thesisinfomgmt.dtos.ErrorMessage;
import com.portfolio.dev.thesisinfomgmt.dtos.RoleDTO;
import com.portfolio.dev.thesisinfomgmt.services.RoleService;
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
@RequestMapping("/roles")
public class RoleController {

  private final RoleService roleService;

  public RoleController(
      RoleService roleService
  ) {
    this.roleService = roleService;
  }

  /**
   * Retrieves list of all roles
   *
   * @return 200 / OK if successful. Every other results indicates an error.
   */
  @Operation(summary = "Retrieves list of all roles.",
      description = "Retrieves list of all roles for Thesis Information Management System.",
      tags = "Role")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Roles successfully retrieved.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = RoleDTO.class)),
              examples = @ExampleObject(value =
                  "["
                + "    {"
                + "        \"id\": 1,\n"
                + "        \"name\": \"Unique Role Name\",\n"
                + "        \"description\": \"This is a role description.\""
                + "    }"
                + "]")))
  })
  @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<Object> getAllRoles() {
    List<RoleDTO> retrievedRoles = roleService.getAllRoles();
    return ResponseEntity.ok().body(retrievedRoles);
  }

  /**
   * Creates new role
   *
   * @param newRole role details
   * @return 201 / CREATED if successful. Every other results indicates an error.
   */
  @Operation(summary = "Creates a new role.",
      description = "Creates a new role for Thesis Information Management System.", tags = "Role")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Role successfully created.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = RoleDTO.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"id\": 1,\n"
                + "    \"name\": \"Unique Role Name\",\n"
                + "    \"description\": \"This is a role description.\""
                + "}"))),
      @ApiResponse(responseCode = "400", description = "Role was not created due to some" +
          " invalid details.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorMessage.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"message\": \"Role with name '<Name>' already exists.\""
                + "}")))
  })
  @PostMapping(value = "",
      produces = { MediaType.APPLICATION_JSON_VALUE },
      consumes = { MediaType.APPLICATION_JSON_VALUE }
  )
  public ResponseEntity<Object> createRole(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "New role request data", required = true,
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = RoleDTO.class,
              requiredProperties = { "name" }),
              examples = @ExampleObject(value =
                  "{"
                + "    \"id\": 0,\n"
                + "    \"name\": \"Researcher\",\n"
                + "    \"description\": \"Researcher of the thesis / dissertation.\""
                + "}")))
      @RequestBody RoleDTO newRole
  ) {
    // Validate new role details
    ValidationResponse validationResponse = roleService.validateRole(newRole.getId(), newRole);
    if (validationResponse.getValidationResult() == ValidationResult.NG) {
      return ResponseEntity
          .status(validationResponse.getHttpStatus())
          .body(validationResponse.getErrorMessage());
    }

    // Create the new role
    RoleDTO createdRole = roleService.createRole(newRole);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
  }

  /**
   * Retrieves a role
   *
   * @param roleId id of the role to be retrieved
   * @return 200 / OK if successful. Every other results indicates an error.
   */
  @Operation(summary = "Retrieves a role.",
      description = "Retrieves a role for Thesis Information Management System.", tags = "Role")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Role successfully retrieved.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = RoleDTO.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"id\": 1,\n"
                + "    \"name\": \"Unique Role Name\",\n"
                + "    \"description\": \"This is a role description.\""
                + "}"))),
      @ApiResponse(responseCode = "404", description = "Role to be retrieved not found.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorMessage.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"message\": \"Role with id '1' not found.\""
                + "}")))
  })
  @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<Object> getRole(
      @Parameter(name = "id", description = "Id of the role to be retrieved")
      @PathVariable(name = "id") long roleId
  ) {
    Optional<RoleDTO> retrievedRole = roleService.getRole(roleId);
    if (retrievedRole.isPresent()) {
      return ResponseEntity.ok(retrievedRole.get());
    } else {
      ErrorMessage errorMessage = new ErrorMessage(String.format(ROLE_NOT_FOUND, roleId));
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
  }

  /**
   * Updates a role
   *
   * @param roleId      id of the role to be updated
   * @param updatedRole updated role details
   * @return 200 / OK if successful. Every other result indicates an error.
   */
  @Operation(summary = "Updates a role.",
      description = "Updates a role for Thesis Information Management System.", tags = "Role")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Role successfully updated.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = RoleDTO.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"id\": 1,\n"
                + "    \"name\": \"Updated Role Name\",\n"
                + "    \"description\": \"This is an updated role description.\""
                + "}"))),
      @ApiResponse(responseCode = "400", description = "Role was not updated due to some" +
          " invalid details.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorMessage.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"message\": \"Role with name '<Name>' already exists.\""
                + "}"))),
      @ApiResponse(responseCode = "404", description = "Role was not updated due to resource" +
          " does not exists.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorMessage.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"message\": \"Role with id '1' not found.\""
                + "}")))
  })
  @PutMapping(value = "/{id}",
      produces = { MediaType.APPLICATION_JSON_VALUE },
      consumes = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<Object> updateRole(
      @Parameter(name = "id", description = "Id of the role to be updated.")
      @PathVariable(name = "id") long roleId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Updated role request data", required = true,
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = RoleDTO.class, requiredProperties = { "name" }),
              examples = @ExampleObject(value =
                  "{"
                      + "    \"id\": 1,\n"
                      + "    \"name\": \"Researcher\",\n"
                      + "    \"description\": \"Researcher of the thesis / dissertation.\""
                      + "}")))
      @RequestBody RoleDTO updatedRole
  ) {
    // Validate role details
    ValidationResponse validationResponse = roleService.validateRole(roleId, updatedRole);
    if (validationResponse.getValidationResult() == ValidationResult.NG) {
      return ResponseEntity
          .status(validationResponse.getHttpStatus())
          .body(validationResponse.getErrorMessage());
    }

    Optional<RoleDTO> optUpdatedRole = roleService.updateRole(roleId, updatedRole);
    if (optUpdatedRole.isPresent()) {
      return ResponseEntity.ok(optUpdatedRole.get());
    } else {
      ErrorMessage errorMessage = new ErrorMessage(String.format(ROLE_NOT_FOUND, roleId));
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
  }

  /**
   * Deletes a role
   *
   * @param roleId id of the role to be deleted
   * @return 200 / OK if successful. Every other results indicates an error.
   */
  @Operation(summary = "Deletes a role.",
      description = "Deletes a role for Thesis Information Management System.", tags = "Role")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Role successfully deleted.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = RoleDTO.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"id\": 1,\n"
                + "    \"name\": \"Researcher\",\n"
                + "    \"description\": \"Researcher of the thesis / dissertation.\""
                + "}"))),
      @ApiResponse(responseCode = "404", description = "Role was not deleted due to resource" +
          " does not exists or it is already deleted.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorMessage.class),
              examples = @ExampleObject(value =
                  "{"
                + "    \"message\": \"Role with id '1' not found.\""
                + "}")))
  })
  @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<Object> deleteRole(
      @Parameter(name = "id", description = "Id of the role to be deleted.")
      @PathVariable(name = "id") long roleId
  ) {
    Optional<RoleDTO> deletedRole = roleService.deleteRole(roleId);
    if (deletedRole.isPresent()) {
      return ResponseEntity.ok(deletedRole.get());
    } else {
      ErrorMessage errorMessage = new ErrorMessage(String.format(ROLE_NOT_FOUND, roleId));
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
  }
}
