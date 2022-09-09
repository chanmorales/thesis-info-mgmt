package com.portfolio.dev.thesisinfomgmt.services;

import static com.portfolio.dev.thesisinfomgmt.utilities.Constants.ROLE_NAME_ALREADY_EXISTS;
import static com.portfolio.dev.thesisinfomgmt.utilities.Constants.ROLE_NAME_REQUIRED;

import com.portfolio.dev.thesisinfomgmt.dtos.ErrorMessage;
import com.portfolio.dev.thesisinfomgmt.dtos.RoleDTO;
import com.portfolio.dev.thesisinfomgmt.entities.Role;
import com.portfolio.dev.thesisinfomgmt.repositories.RoleRepository;
import com.portfolio.dev.thesisinfomgmt.utilities.MapperHelper;
import com.portfolio.dev.thesisinfomgmt.utilities.ValidationResponse;
import com.portfolio.dev.thesisinfomgmt.utilities.ValidationResponse.ValidationResult;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;

  public RoleServiceImpl(
      RoleRepository roleRepository
  ) {
    this.roleRepository = roleRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<RoleDTO> getAllRoles() {
    List<Role> roles = Streamable.of(roleRepository.findAll()).toList();
    return roles.stream().map(RoleDTO::new).collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RoleDTO createRole(RoleDTO roleDTO) {

    Role role = MapperHelper.convertToRole(roleDTO);
    role = roleRepository.save(role);
    return new RoleDTO(role);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<RoleDTO> getRole(long roleId) {

    Optional<Role> role = roleRepository.findById(roleId);
    return role.map(RoleDTO::new);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<RoleDTO> updateRole(long roleId, RoleDTO roleDTO) {

    Optional<Role> role = roleRepository.findById(roleId);
    if (role.isPresent()) {
      Role updatedRole = role.get();
      updatedRole.setName(roleDTO.getName());
      updatedRole.setDescription(roleDTO.getDescription());
      updatedRole = roleRepository.save(updatedRole);
      return Optional.of(new RoleDTO(updatedRole));
    } else {
      return Optional.empty();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<RoleDTO> deleteRole(long roleId) {

    Optional<Role> role = roleRepository.findById(roleId);
    if (role.isPresent()) {
      roleRepository.deleteById(roleId);
      return role.map(RoleDTO::new);
    } else {
      return Optional.empty();
    }
  }

  /**
   * {@inheritDoc}
   */
  public ValidationResponse validateRole(long roleId, RoleDTO roleDTO) {

    ValidationResponse validationResponse = new ValidationResponse();

    // Check if name is empty
    if (StringUtils.isEmpty(roleDTO.getName())) {
      validationResponse.setValidationResult(ValidationResult.NG);
      validationResponse.setErrorMessage(new ErrorMessage(ROLE_NAME_REQUIRED));
      validationResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
      return validationResponse;
    }

    // Check if name already exists (should be different id)
    Optional<Role> role = roleRepository.findFirstByNameEqualsIgnoreCase(roleDTO.getName());
    if (role.isPresent() && role.get().getId() != roleId) {
      validationResponse.setValidationResult(ValidationResult.NG);
      validationResponse.setErrorMessage(
          new ErrorMessage(String.format(ROLE_NAME_ALREADY_EXISTS, roleDTO.getName())));
      validationResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
      return validationResponse;
    }

    validationResponse.setValidationResult(ValidationResult.OK);
    return validationResponse;
  }
}
