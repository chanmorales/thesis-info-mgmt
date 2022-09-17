package com.portfolio.dev.thesisinfomgmt.services;

import com.portfolio.dev.thesisinfomgmt.dtos.RoleDTO;
import com.portfolio.dev.thesisinfomgmt.utilities.ValidationResponse;
import java.util.List;
import java.util.Optional;

public interface RoleService {

  /**
   * Retrieves list of all roles
   *
   * @return list of roles
   */
  List<RoleDTO> getAllRoles();

  /**
   * Creates a role
   *
   * @param roleDTO role to be created
   * @return created role
   */
  RoleDTO createRole(RoleDTO roleDTO);

  /**
   * Retrieves a role
   *
   * @param roleId id of the role to be retrieved
   * @return optional retrieved role
   */
  Optional<RoleDTO> getRole(long roleId);

  /**
   * Updates a role
   *
   * @param roleId  id of the role to be updated
   * @param roleDTO updated role details
   * @return optional updated role
   */
  Optional<RoleDTO> updateRole(long roleId, RoleDTO roleDTO);

  /**
   * Deletes a role if it exists
   *
   * @param roleId id of the role to be deleted
   * @return optional deleted role
   */
  Optional<RoleDTO> deleteRole(long roleId);

  /**
   * Validate role details
   *
   * @param roleId  id of the role to be validated
   * @param roleDTO role to be validated
   * @return validation response
   */
  ValidationResponse validateRole(long roleId, RoleDTO roleDTO);
}
