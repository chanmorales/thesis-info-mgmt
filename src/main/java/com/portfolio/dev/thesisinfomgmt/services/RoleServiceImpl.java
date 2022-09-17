package com.portfolio.dev.thesisinfomgmt.services;

import static com.portfolio.dev.thesisinfomgmt.utilities.Constants.ROLE_NAME_ALREADY_EXISTS;
import static com.portfolio.dev.thesisinfomgmt.utilities.Constants.ROLE_NAME_REQUIRED;

import com.portfolio.dev.thesisinfomgmt.dtos.RoleDTO;
import com.portfolio.dev.thesisinfomgmt.entities.Role;
import com.portfolio.dev.thesisinfomgmt.repositories.RoleRepository;
import com.portfolio.dev.thesisinfomgmt.utilities.MapperHelper;
import com.portfolio.dev.thesisinfomgmt.utilities.ValidationResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
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

    return roleRepository.findAll()
        .stream()
        .map(RoleDTO::new)
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RoleDTO createRole(RoleDTO roleDTO) {

    Role role = MapperHelper.mapToRole(roleDTO);
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

    // Check if name is empty
    if (StringUtils.isEmpty(roleDTO.getName())) {
      return ValidationResponse.badRequest(ROLE_NAME_REQUIRED);
    }

    // Check if name already exists (should be different id)
    Optional<Role> role = roleRepository.findFirstByNameEqualsIgnoreCase(roleDTO.getName());
    if (role.isPresent() && role.get().getId() != roleId) {
      return ValidationResponse.badRequest(
          String.format(ROLE_NAME_ALREADY_EXISTS, roleDTO.getName()));
    }

    return ValidationResponse.ok();
  }
}
