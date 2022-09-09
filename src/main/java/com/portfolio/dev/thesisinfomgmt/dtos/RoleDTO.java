package com.portfolio.dev.thesisinfomgmt.dtos;

import com.portfolio.dev.thesisinfomgmt.entities.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDTO {

  /**
   * Create a new Role DTO based on the given Role object
   *
   * @param role role object to based from
   */
  public RoleDTO(Role role) {
    this.setId(role.getId());
    this.setName(role.getName());
    this.setDescription(role.getDescription());
  }

  private long id;

  private String name;

  private String description;
}
