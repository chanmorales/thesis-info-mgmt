package com.portfolio.dev.thesisinfomgmt.services;

import com.portfolio.dev.thesisinfomgmt.dtos.DegreeDTO;
import com.portfolio.dev.thesisinfomgmt.entities.Degree;
import com.portfolio.dev.thesisinfomgmt.repositories.DegreeRepository;
import com.portfolio.dev.thesisinfomgmt.utilities.MapperHelper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
}
