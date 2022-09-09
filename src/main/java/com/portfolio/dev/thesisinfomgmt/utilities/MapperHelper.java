package com.portfolio.dev.thesisinfomgmt.utilities;

import com.portfolio.dev.thesisinfomgmt.dtos.RoleDTO;
import com.portfolio.dev.thesisinfomgmt.entities.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperHelper {

  /**
   * Converts a RoleDTO to Role Object
   *
   * @param roleDTO role DTO to be converted
   * @return equivalent Role object
   */
  public static Role convertToRole(RoleDTO roleDTO) {
    Role role = new Role();
    role.setId(roleDTO.getId());
    role.setName(roleDTO.getName());
    role.setDescription(roleDTO.getDescription());
    return role;
  }
}
