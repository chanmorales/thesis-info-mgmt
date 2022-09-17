package com.portfolio.dev.thesisinfomgmt.services;

import com.portfolio.dev.thesisinfomgmt.dtos.DegreeDTO;
import com.portfolio.dev.thesisinfomgmt.utilities.ValidationResponse;
import java.util.List;
import java.util.Optional;

public interface DegreeService {

  /**
   * Retrieves list of all degrees
   *
   * @return list of degrees
   */
  List<DegreeDTO> getAllDegrees();

  /**
   * Creates a degree
   *
   * @param degreeDTO degree to be created
   * @return created degree
   */
  DegreeDTO createDegree(DegreeDTO degreeDTO);

  /**
   * Retrieves a degree
   *
   * @param degreeId id of the role to be retrieved
   * @return optional retrieved degree
   */
  Optional<DegreeDTO> getDegree(long degreeId);

  /**
   * Updates a degree
   *
   * @param degreeId  id of the degree to be updated
   * @param degreeDTO updated degree details
   * @return optional updated degree
   */
  Optional<DegreeDTO> updateDegree(long degreeId, DegreeDTO degreeDTO);

  /**
   * Deletes a degree
   *
   * @param degreeId id of the degree to be deleted
   * @return optional deleted degree
   */
  Optional<DegreeDTO> deleteDegree(long degreeId);

  /**
   * Validate degree details
   *
   * @param degreeId  id of the degree to be validated
   * @param degreeDTO degree to be validated
   * @return validation response
   */
  ValidationResponse validateDegree(long degreeId, DegreeDTO degreeDTO);
}
