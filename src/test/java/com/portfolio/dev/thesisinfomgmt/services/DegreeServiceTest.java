package com.portfolio.dev.thesisinfomgmt.services;

import static com.portfolio.dev.thesisinfomgmt.utilities.Constants.DEGREE_ABBR_ALREADY_EXISTS;
import static com.portfolio.dev.thesisinfomgmt.utilities.Constants.DEGREE_ABBR_REQUIRED;
import static com.portfolio.dev.thesisinfomgmt.utilities.Constants.DEGREE_NAME_REQUIRED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.portfolio.dev.thesisinfomgmt.dtos.DegreeDTO;
import com.portfolio.dev.thesisinfomgmt.dtos.ErrorMessage;
import com.portfolio.dev.thesisinfomgmt.entities.Degree;
import com.portfolio.dev.thesisinfomgmt.repositories.DegreeRepository;
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
class DegreeServiceTest {

  @Mock
  private DegreeRepository degreeRepository;
  private DegreeService degreeService;

  private static final ValidationResponse validResponse =
      new ValidationResponse().withValidationResult(ValidationResult.OK);

  @BeforeEach
  void init() {
    degreeService = new DegreeServiceImpl(degreeRepository);
  }

  @DisplayName("[TEST] Get the list of all degrees.")
  @Test
  void testGetAllDegrees() {

    // Mock the return of degreeRepository.findAll
    Degree degree1 = new Degree().withId(1).withAbbr("BSCS")
        .withName("Bachelor of Science in Computer Science");
    Degree degree2 = new Degree().withId(2).withAbbr("MBA")
        .withName("Master of Business Administration");
    when(degreeRepository.findAll()).thenReturn(Arrays.asList(degree1, degree2));

    // Assert the return of degreeService.getAllDegrees to expected object
    List<DegreeDTO> expectedDegrees = new ArrayList<>(
        Arrays.asList(new DegreeDTO(degree1), new DegreeDTO(degree2)));
    List<DegreeDTO> actualDegrees = degreeService.getAllDegrees();
    assertThat(expectedDegrees).isEqualTo(actualDegrees);

    verify(degreeRepository).findAll();
  }

  @DisplayName("[TEST] Create a degree.")
  @Test
  void testCreateDegree() {

    // Mock the return of degreeRepository.save
    Degree mockDegree = new Degree().withId(1).withAbbr("BSCS")
        .withName("Bachelor of Science in Computer Science");
    DegreeDTO mockDegreeDto = new DegreeDTO(mockDegree);
    when(degreeRepository.save(any(Degree.class))).thenReturn(mockDegree);

    // Assert the return of degreeService.createDegree to expected object
    DegreeDTO actualDegree = degreeService.createDegree(mockDegreeDto);
    assertThat(actualDegree).isEqualTo(mockDegreeDto);

    verify(degreeRepository).save(any(Degree.class));
  }

  @DisplayName("[TEST] Get an existing degree.")
  @Test
  void testGetDegreeById() {

    // Mock the return of degreeRepository.findById
    Degree mockDegree = new Degree().withId(1).withAbbr("BSCS")
        .withName("Bachelor of Science in Computer Science");
    when(degreeRepository.findById(1L)).thenReturn(Optional.of(mockDegree));

    // Assert the return of degreeService.getDegree to expected object
    Optional<DegreeDTO> actualDegree = degreeService.getDegree(1);
    assertThat(actualDegree).isNotEmpty().contains(new DegreeDTO(mockDegree));

    verify(degreeRepository).findById(1L);
  }

  @DisplayName("[TEST] Get a non-existing degree.")
  @Test
  void testGetDegreeByIdNotFound() {

    // Mock the return of degreeRepository.findById
    when(degreeRepository.findById(anyLong())).thenReturn(Optional.empty());

    // Assert the return of degreeService.getDegree is empty
    Optional<DegreeDTO> actualDegree = degreeService.getDegree(1);
    assertThat(actualDegree).isEmpty();

    verify(degreeRepository).findById(anyLong());
  }

  @DisplayName("[TEST] Update an existing degree.")
  @Test
  void testUpdateDegree() {

    // Mock the return of degreeRepository.findById and degreeRepository.save
    Degree initDegree = new Degree().withId(1).withAbbr("BSCS")
        .withName("Bachelor of Science in Computer Science");
    Degree mockDegree = new Degree().withId(1).withAbbr("BSCS-2")
        .withName("Bachelor of Science in Computer Science - Updated");
    when(degreeRepository.findById(1L)).thenReturn(Optional.of(initDegree));
    when(degreeRepository.save(any(Degree.class))).thenReturn(mockDegree);

    // Assert the return of degreeService.updateDegree is not empty and is equal to the expected
    DegreeDTO mockDegreeDto = new DegreeDTO(mockDegree);
    Optional<DegreeDTO> actualDegree = degreeService.updateDegree(1, mockDegreeDto);
    assertThat(actualDegree).isNotEmpty().contains(mockDegreeDto);

    verify(degreeRepository).findById(1L);
    verify(degreeRepository).save(any(Degree.class));
  }

  @DisplayName("[TEST] Update a non-existing degree.")
  @Test
  void testUpdateDegreeNotExisting() {

    // Mock the return of degreeRepository.findById
    when(degreeRepository.findById(1L)).thenReturn(Optional.empty());

    // Assert the return of degreeService.updateDegree is empty since it does not exist
    Degree updatedDegree = new Degree().withId(1).withAbbr("BSCS")
        .withName("Bachelor of Science in Computer Science");
    Optional<DegreeDTO> actualDegree =
        degreeService.updateDegree(1, new DegreeDTO(updatedDegree));
    assertThat(actualDegree).isEmpty();

    verify(degreeRepository).findById(1L);
    verify(degreeRepository, never()).save(any(Degree.class));
  }

  @DisplayName("[TEST] Delete an existing degree.")
  @Test
  void testDeleteDegree() {

    // Mock the return of degreeRepository.findById and degreeRepository.deleteById
    // should do nothing
    Degree deleteDegree = new Degree().withId(1).withAbbr("BSCS")
        .withName("Bachelor of Science in Computer Science");
    when(degreeRepository.findById(1L)).thenReturn(Optional.of(deleteDegree));
    doNothing().when(degreeRepository).deleteById(1L);

    // Assert the return of degreeService.deleteDegree is not empty and is equal to the expected
    Optional<DegreeDTO> deletedDegree = degreeService.deleteDegree(1);
    assertThat(deletedDegree).isNotEmpty().contains(new DegreeDTO(deleteDegree));

    verify(degreeRepository).findById(1L);
    verify(degreeRepository).deleteById(1L);
  }

  @DisplayName("[TEST] Delete a non-existing degree.")
  @Test
  void testDeleteDegreeNotExisting() {

    // Mock the return of degreeRepository.findById
    when(degreeRepository.findById(1L)).thenReturn(Optional.empty());

    // Assert the return of degreeService.deleteDegree is empty since it does not exist
    Optional<DegreeDTO> deletedDegree = degreeService.deleteDegree(1L);
    assertThat(deletedDegree).isEmpty();

    verify(degreeRepository).findById(1L);
    verify(degreeRepository, never()).deleteById(1L);
  }

  @DisplayName("[TEST] Validate a valid new degree.")
  @Test
  void testValidateNewDegree() {

    // Mock the return of degreeRepository.findFirstByAbbrEqualsIgnoreCase
    when(degreeRepository.findFirstByAbbrEqualsIgnoreCase(anyString()))
        .thenReturn(Optional.empty());

    // Assert that the return of degreeService.validateDegree is the same as expected
    Degree validateDegree = new Degree().withId(0).withAbbr("BSCS")
        .withName("Bachelor of Science in Computer Science");
    ValidationResponse actualResponse =
        degreeService.validateDegree(0, new DegreeDTO(validateDegree));
    assertThat(actualResponse).isEqualTo(validResponse);

    verify(degreeRepository).findFirstByAbbrEqualsIgnoreCase(anyString());
  }

  @DisplayName("[TEST] Validate a valid updated degree where abbreviation was updated.")
  @Test
  void testValidateUpdateDegreeDifferentAbbreviation() {

    // Mock the return of degreeRepository.findFirstByAbbrEqualsIgnoreCase
    Degree validateDegree = new Degree().withId(1).withAbbr("BSCS-2")
        .withName("Bachelor of Science in Computer Science - Updated");
    when(degreeRepository.findFirstByAbbrEqualsIgnoreCase(validateDegree.getAbbr()))
        .thenReturn(Optional.empty());

    // Assert that the return of degreeService.validateDegree is the same as expected
    ValidationResponse actualResponse =
        degreeService.validateDegree(1, new DegreeDTO(validateDegree));
    assertThat(actualResponse).isEqualTo(validResponse);

    verify(degreeRepository).findFirstByAbbrEqualsIgnoreCase(validateDegree.getAbbr());
  }

  @DisplayName("[TEST] Validate a valid updated degree where abbreviation was not updated.")
  @Test
  void testValidateUpdateDegreeSameAbbr() {

    // Mock the return of degreeRepository.findFirstByAbbrEqualsIgnoreCase
    Degree validateDegree = new Degree().withId(1).withAbbr("BSCS")
        .withName("Bachelor of Science in Computer Science - Updated");
    Degree existingDegree = new Degree().withId(1).withAbbr("BSCS")
        .withName("Bachelor of Science in Computer Science");
    when(degreeRepository.findFirstByAbbrEqualsIgnoreCase(validateDegree.getAbbr()))
        .thenReturn(Optional.of(existingDegree));

    // Assert that the return of degreeService.validateDegree is the same as expected
    ValidationResponse actualResponse =
        degreeService.validateDegree(1, new DegreeDTO(validateDegree));
    assertThat(actualResponse).isEqualTo(validResponse);

    verify(degreeRepository).findFirstByAbbrEqualsIgnoreCase(validateDegree.getAbbr());
  }

  @DisplayName("[TEST] Validate an invalid degree since its abbreviation already exists.")
  @Test
  void testValidateDegreeExistingAbbr() {

    // Mock the return of degreeRepository.findFirstByAbbrEqualsIgnoreCase
    Degree existingDegree = new Degree().withId(1).withAbbr("BSCS")
        .withName("Bachelor of Science in Computer Science");
    Degree validateDegree = new Degree().withId(0).withAbbr("BSCS")
        .withName("Duplicate of Bachelor of Science in Computer Science");
    when(degreeRepository.findFirstByAbbrEqualsIgnoreCase(validateDegree.getAbbr())).
        thenReturn(Optional.of(existingDegree));

    // Assert that the return of degreeService.validateDegree is the same as expected
    ValidationResponse actualResponse =
        degreeService.validateDegree(validateDegree.getId(), new DegreeDTO(validateDegree));
    ValidationResponse expectedResponse = new ValidationResponse()
        .withValidationResult(ValidationResult.NG)
        .withHttpStatus(HttpStatus.BAD_REQUEST)
        .withErrorMessage(new ErrorMessage(
            String.format(DEGREE_ABBR_ALREADY_EXISTS, validateDegree.getAbbr())));
    assertThat(actualResponse).isEqualTo(expectedResponse);

    verify(degreeRepository).findFirstByAbbrEqualsIgnoreCase(validateDegree.getAbbr());
  }

  @DisplayName("[TEST] Validate an invalid degree since its abbreviation is null.")
  @Test
  void testValidateDegreeNullAbbr() {

    // Assert that the return of degreeService.validateDegree is the same as expected
    Degree validateDegree = new Degree().withId(0).withAbbr(null).withName("Name");
    ValidationResponse actualResponse =
        degreeService.validateDegree(validateDegree.getId(), new DegreeDTO(validateDegree));
    ValidationResponse expectedResponse = new ValidationResponse()
        .withValidationResult(ValidationResult.NG)
        .withHttpStatus(HttpStatus.BAD_REQUEST)
        .withErrorMessage(new ErrorMessage(DEGREE_ABBR_REQUIRED));
    assertThat(actualResponse).isEqualTo(expectedResponse);
  }

  @DisplayName("[TEST] Validate an invalid degree since its abbreviation is empty.")
  @Test
  void testValidateDegreeEmptyAbbr() {

    // Assert that the return of degreeService.validateDegree is the same as expected
    Degree validateDegree = new Degree().withId(0).withAbbr("").withName("Name");
    ValidationResponse actualResponse =
        degreeService.validateDegree(validateDegree.getId(), new DegreeDTO(validateDegree));
    ValidationResponse expectedResponse = new ValidationResponse()
        .withValidationResult(ValidationResult.NG)
        .withHttpStatus(HttpStatus.BAD_REQUEST)
        .withErrorMessage(new ErrorMessage(DEGREE_ABBR_REQUIRED));
    assertThat(actualResponse).isEqualTo(expectedResponse);
  }

  @DisplayName("[TEST] Validate an invalid degree since its name is null.")
  @Test
  void testValidateDegreeNullName() {

    // Assert that the return of degreeService.validateDegree is the same as expected
    Degree validateDegree = new Degree().withId(0).withAbbr("BSCS").withName(null);
    ValidationResponse actualResponse =
        degreeService.validateDegree(validateDegree.getId(), new DegreeDTO(validateDegree));
    ValidationResponse expectedResponse = new ValidationResponse()
        .withValidationResult(ValidationResult.NG)
        .withHttpStatus(HttpStatus.BAD_REQUEST)
        .withErrorMessage(new ErrorMessage(DEGREE_NAME_REQUIRED));
    assertThat(actualResponse).isEqualTo(expectedResponse);
  }

  @DisplayName("[TEST] Validate an invalid degree since its name is empty.")
  @Test
  void testValidateDegreeEmptyName() {

    // Assert that the return of degreeService.validateDegree is the same as expected
    Degree validateDegree = new Degree().withId(0).withAbbr("BSCS").withName("");
    ValidationResponse actualResponse =
        degreeService.validateDegree(validateDegree.getId(), new DegreeDTO(validateDegree));
    ValidationResponse expectedResponse = new ValidationResponse()
        .withValidationResult(ValidationResult.NG)
        .withHttpStatus(HttpStatus.BAD_REQUEST)
        .withErrorMessage(new ErrorMessage(DEGREE_NAME_REQUIRED));
    assertThat(actualResponse).isEqualTo(expectedResponse);
  }
}
