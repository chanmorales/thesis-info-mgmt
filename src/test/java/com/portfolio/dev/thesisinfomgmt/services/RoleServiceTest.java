package com.portfolio.dev.thesisinfomgmt.services;

import static com.portfolio.dev.thesisinfomgmt.utilities.Constants.ROLE_NAME_ALREADY_EXISTS;
import static com.portfolio.dev.thesisinfomgmt.utilities.Constants.ROLE_NAME_REQUIRED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.portfolio.dev.thesisinfomgmt.dtos.ErrorMessage;
import com.portfolio.dev.thesisinfomgmt.dtos.RoleDTO;
import com.portfolio.dev.thesisinfomgmt.entities.Role;
import com.portfolio.dev.thesisinfomgmt.repositories.RoleRepository;
import com.portfolio.dev.thesisinfomgmt.utilities.ValidationResponse;
import com.portfolio.dev.thesisinfomgmt.utilities.ValidationResponse.ValidationResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

  @Mock
  private RoleRepository roleRepository;
  private RoleService roleService;

  private static final ValidationResponse validResponse =
      new ValidationResponse().withValidationResult(ValidationResult.OK);

  @BeforeEach
  void init() {
    roleService = new RoleServiceImpl(roleRepository);
  }

  @DisplayName("[TEST] Get the list of all roles.")
  @Test
  void testGetAllRoles() {

    // Mock the return of roleRepository.findAll
    Role mockRole1 = new Role().withId(1).withName("Researcher")
        .withDescription("Thesis researcher.");
    Role mockRole2 = new Role().withId(2).withName("Adviser")
        .withDescription("Thesis adviser.");
    when(roleRepository.findAll()).thenReturn(Arrays.asList(mockRole1, mockRole2));

    // Assert the return of roleService.getAllRoles to expected object
    List<RoleDTO> expectedRoles = new ArrayList<>(
        Arrays.asList(new RoleDTO(mockRole1), new RoleDTO(mockRole2)));
    List<RoleDTO> actualRoles = roleService.getAllRoles();
    assertThat(expectedRoles).isEqualTo(actualRoles);

    verify(roleRepository).findAll();
  }

  @DisplayName("[TEST] Create a role.")
  @Test
  void testCreateRole() {

    // Mock the return of roleRepository.save
    Role mockRole = new Role().withId(1).withName("Researcher")
        .withDescription("This is a description.");
    RoleDTO mockRoleDto = new RoleDTO(mockRole);
    when(roleRepository.save(any(Role.class))).thenReturn(mockRole);

    // Assert the return of roleService.createRole to expected object
    RoleDTO actualRole = roleService.createRole(mockRoleDto);
    assertThat(actualRole).isEqualTo(mockRoleDto);

    verify(roleRepository).save(any(Role.class));
  }

  @DisplayName("[TEST] Get an existing role.")
  @Test
  void testGetRoleById() {

    // Mock the return of roleRepository.findById
    Role mockRole = new Role().withId(1).withName("Researcher")
        .withDescription("This is a description.");
    when(roleRepository.findById(1L)).thenReturn(Optional.of(mockRole));

    // Assert the return of roleService.getRole to expected object
    Optional<RoleDTO> actualRole = roleService.getRole(1);
    assertThat(actualRole).isNotEmpty().contains(new RoleDTO(mockRole));

    verify(roleRepository).findById(1L);
  }

  @DisplayName("[TEST] Get a non-existing role.")
  @Test
  void testGetRoleByIdNotFound() {

    // Mock the return of roleRepository.findById
    when(roleRepository.findById(1L)).thenReturn(Optional.empty());

    // Assert the return of roleService.getRole is empty
    Optional<RoleDTO> actualRole = roleService.getRole(1);
    assertThat(actualRole).isEmpty();

    verify(roleRepository).findById(1L);
  }

  @DisplayName("[TEST] Update an existing role.")
  @Test
  void testUpdateRole() {

    // Mock the return of roleRepository.findById and roleRepository.save
    Role initRole = new Role().withId(1).withName("Researcher")
        .withDescription("This is a description.");
    Role mockRole = new Role().withId(1).withName("Researcher Updated")
        .withDescription("This is an updated description.");
    RoleDTO mockRoleDto = new RoleDTO(mockRole);
    when(roleRepository.findById(1L)).thenReturn(Optional.of(initRole));
    when(roleRepository.save(any(Role.class))).thenReturn(mockRole);

    // Assert the return of roleService.updateRole is not empty and is equal to the expected
    Optional<RoleDTO> actualRole = roleService.updateRole(1, mockRoleDto);
    assertThat(actualRole).isNotEmpty().contains(mockRoleDto);

    verify(roleRepository).findById(1L);
    verify(roleRepository).save(any(Role.class));
  }

  @DisplayName("[TEST] Update a non-existing role.")
  @Test
  void testUpdateRoleNotExisting() {

    // Mock the return of roleRepository.findById
    when(roleRepository.findById(1L)).thenReturn(Optional.empty());

    // Assert the return of roleService.updateRole is empty since it does not exist
    Role updatedRole = new Role().withId(1).withName("Researcher Updated")
        .withDescription("This is an updated description.");
    Optional<RoleDTO> actualRole = roleService.updateRole(1, new RoleDTO(updatedRole));
    assertThat(actualRole).isEmpty();

    verify(roleRepository).findById(1L);
    verify(roleRepository, never()).save(any(Role.class));
  }

  @DisplayName("[TEST] Delete an existing role.")
  @Test
  void testDeleteRole() {

    // Mock the return of roleRepository.findById and roleRepository.deleteById should do nothing
    Role deleteRole = new Role().withId(1).withName("Researcher")
        .withDescription("This is a description.");
    when(roleRepository.findById(1L)).thenReturn(Optional.of(deleteRole));
    doNothing().when(roleRepository).deleteById(1L);

    // Assert the return of roleService.deleteRole is not empty and is equal to the expected
    Optional<RoleDTO> deletedRole = roleService.deleteRole(1);
    assertThat(deletedRole).isNotEmpty().contains(new RoleDTO(deleteRole));

    verify(roleRepository).findById(1L);
    verify(roleRepository).deleteById(1L);
  }

  @DisplayName("[TEST] Delete a non-existing role.")
  @Test
  void testDeleteRoleNotExisting() {

    // Mock the return of roleRepository.findById
    when(roleRepository.findById(1L)).thenReturn(Optional.empty());

    // Assert the return of roleService.deleteRole is empty since it does not exist
    Optional<RoleDTO> deletedRole = roleService.deleteRole(1L);
    assertThat(deletedRole).isEmpty();

    verify(roleRepository).findById(1L);
    verify(roleRepository, never()).deleteById(1L);
  }

  @DisplayName("[TEST] Validate a valid new role.")
  @Test
  void testValidateNewRole() {

    // Mock the return of roleRepository.findFirstByNameEqualsIgnoreCase
    when(roleRepository.findFirstByNameEqualsIgnoreCase(anyString()))
        .thenReturn(Optional.empty());

    // Assert that the return of roleService.validateRole is the same as expected
    Role validateRole = new Role().withId(0).withName("Researcher")
        .withDescription("This is a description.");
    ValidationResponse actualResponse =
        roleService.validateRole(0, new RoleDTO(validateRole));
    assertThat(actualResponse).isEqualTo(validResponse);

    verify(roleRepository).findFirstByNameEqualsIgnoreCase(anyString());
  }

  @DisplayName("[TEST] Validate a valid updated role where name was updated.")
  @Test
  void testValidateUpdateRoleDifferentName() {

    // Mock the return of roleRepository.findFirstByNameEqualsIgnoreCase
    Role validateRole = new Role().withId(1).withName("Researcher Updated")
        .withDescription("This is an updated description.");
    when(roleRepository.findFirstByNameEqualsIgnoreCase(validateRole.getName()))
        .thenReturn(Optional.empty());

    // Assert that the return of roleService.validateRole is the same as expected
    ValidationResponse actualResponse =
        roleService.validateRole(1, new RoleDTO(validateRole));
    assertThat(actualResponse).isEqualTo(validResponse);

    verify(roleRepository).findFirstByNameEqualsIgnoreCase(validateRole.getName());
  }

  @DisplayName("[TEST] Validate a valid updated role where name was not updated.")
  @Test
  void testValidateUpdateRoleSameName() {

    // Mock the return of roleRepository.findFirstByNameEqualsIgnoreCase
    Role validateRole = new Role().withId(1).withName("Researcher")
        .withDescription("This is an updated description.");
    when(roleRepository.findFirstByNameEqualsIgnoreCase(validateRole.getName()))
        .thenReturn(Optional.of(validateRole));

    // Assert that the return of roleService.validateRole is the same as expected
    ValidationResponse actualResponse =
        roleService.validateRole(1, new RoleDTO(validateRole));
    assertThat(actualResponse).isEqualTo(validResponse);

    verify(roleRepository).findFirstByNameEqualsIgnoreCase(validateRole.getName());
  }

  @DisplayName("[TEST] Validate an invalid role since its name already existed.")
  @Test
  void testValidateRoleExistingName() {

    // Mock the return of roleRepository.findFirstByNameEqualsIgnoreCase
    Role existingRole = new Role().withId(1).withName("Researcher")
        .withDescription("This is a description.");
    Role validateRole = new Role().withId(0).withName("Researcher")
        .withDescription("This is a role with name already existing.");
    when(roleRepository.findFirstByNameEqualsIgnoreCase(validateRole.getName()))
        .thenReturn(Optional.of(existingRole));

    // Assert that the return of roleService.validateRole is the same as expected
    ValidationResponse actualResponse =
        roleService.validateRole(validateRole.getId(), new RoleDTO(validateRole));
    ValidationResponse expectedResponse =
        new ValidationResponse().withValidationResult(ValidationResult.NG)
            .withHttpStatus(HttpStatus.BAD_REQUEST)
            .withErrorMessage(
                new ErrorMessage(String.format(ROLE_NAME_ALREADY_EXISTS, validateRole.getName())));
    assertThat(actualResponse).isEqualTo(expectedResponse);

    verify(roleRepository).findFirstByNameEqualsIgnoreCase(validateRole.getName());
  }

  @DisplayName("[TEST] Validated an invalid role since its name is null.")
  @Test
  void testValidateRoleNullName() {

    // Assert that the return of roleService.validateRole is the same as expected
    Role validateRole = new Role().withId(0).withName(null).withDescription("");
    ValidationResponse actualResponse =
        roleService.validateRole(validateRole.getId(), new RoleDTO(validateRole));
    ValidationResponse expectedResponse =
        new ValidationResponse().withValidationResult(ValidationResult.NG)
            .withHttpStatus(HttpStatus.BAD_REQUEST)
            .withErrorMessage(new ErrorMessage(ROLE_NAME_REQUIRED));
    assertThat(actualResponse).isEqualTo(expectedResponse);

    verify(roleRepository, never()).findFirstByNameEqualsIgnoreCase(anyString());
  }

  @DisplayName("[TEST] Validated an invalid role since its name is empty.")
  @Test
  void testValidateRoleEmptyName() {

    // Assert that the return of roleService.validateRole is the same as expected
    Role validateRole = new Role().withId(0).withName("").withDescription("");
    ValidationResponse actualResponse =
        roleService.validateRole(validateRole.getId(), new RoleDTO(validateRole));
    ValidationResponse expectedResponse =
        new ValidationResponse().withValidationResult(ValidationResult.NG)
            .withHttpStatus(HttpStatus.BAD_REQUEST)
            .withErrorMessage(new ErrorMessage(ROLE_NAME_REQUIRED));
    assertThat(actualResponse).isEqualTo(expectedResponse);

    verify(roleRepository, never()).findFirstByNameEqualsIgnoreCase(anyString());
  }
}
