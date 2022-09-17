package com.portfolio.dev.thesisinfomgmt.services;

import static com.portfolio.dev.thesisinfomgmt.utilities.Constants.DEGREE_ABBR_ALREADY_EXISTS;
import static com.portfolio.dev.thesisinfomgmt.utilities.Constants.DEGREE_ABBR_REQUIRED;
import static com.portfolio.dev.thesisinfomgmt.utilities.Constants.DEGREE_NAME_REQUIRED;

import com.portfolio.dev.thesisinfomgmt.dtos.DegreeDTO;
import com.portfolio.dev.thesisinfomgmt.entities.Degree;
import com.portfolio.dev.thesisinfomgmt.repositories.DegreeRepository;
import com.portfolio.dev.thesisinfomgmt.utilities.MapperHelper;
import com.portfolio.dev.thesisinfomgmt.utilities.ValidationResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class DegreeServiceImpl implements DegreeService {

  private final DegreeRepository degreeRepository;

  public DegreeServiceImpl(
      DegreeRepository degreeRepository
  ) {
    this.degreeRepository = degreeRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<DegreeDTO> getAllDegrees() {

    return degreeRepository.findAll()
        .stream()
        .map(DegreeDTO::new)
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DegreeDTO createDegree(DegreeDTO degreeDTO) {

    Degree degree = MapperHelper.mapToDegree(degreeDTO);
    degree = degreeRepository.save(degree);
    return new DegreeDTO(degree);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<DegreeDTO> getDegree(long degreeId) {

    Optional<Degree> degree = degreeRepository.findById(degreeId);
    return degree.map(DegreeDTO::new);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<DegreeDTO> updateDegree(long degreeId, DegreeDTO degreeDTO) {

    Optional<Degree> degree = degreeRepository.findById(degreeId);
    if (degree.isPresent()) {
      Degree updatedDegree = degree.get();
      updatedDegree.setAbbr(degreeDTO.getAbbr());
      updatedDegree.setName(degreeDTO.getName());
      updatedDegree = degreeRepository.save(updatedDegree);
      return Optional.of(new DegreeDTO(updatedDegree));
    } else {
      return Optional.empty();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<DegreeDTO> deleteDegree(long degreeId) {

    Optional<Degree> degree = degreeRepository.findById(degreeId);
    if (degree.isPresent()) {
      degreeRepository.deleteById(degreeId);
      return degree.map(DegreeDTO::new);
    } else {
      return Optional.empty();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ValidationResponse validateDegree(long degreeId, DegreeDTO degreeDTO) {

    // Check if abbreviation is empty
    if (StringUtils.isEmpty(degreeDTO.getAbbr())) {
      return ValidationResponse.badRequest(DEGREE_ABBR_REQUIRED);
    }

    // Check if name is empty
    if (StringUtils.isEmpty(degreeDTO.getName())) {
      return ValidationResponse.badRequest(DEGREE_NAME_REQUIRED);
    }

    // Check if abbreviation already exists and with different id
    Optional<Degree> degree = degreeRepository.findFirstByAbbrEqualsIgnoreCase(degreeDTO.getAbbr());
    if (degree.isPresent() && degree.get().getId() != degreeId) {
      return ValidationResponse.badRequest(
          String.format(DEGREE_ABBR_ALREADY_EXISTS, degreeDTO.getAbbr()));
    }

    return ValidationResponse.ok();
  }
}
