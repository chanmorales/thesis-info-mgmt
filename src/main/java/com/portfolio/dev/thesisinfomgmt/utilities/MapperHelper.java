package com.portfolio.dev.thesisinfomgmt.utilities;

import com.portfolio.dev.thesisinfomgmt.dtos.DegreeDTO;
import com.portfolio.dev.thesisinfomgmt.dtos.RoleDTO;
import com.portfolio.dev.thesisinfomgmt.entities.Degree;
import com.portfolio.dev.thesisinfomgmt.entities.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperHelper {

  /**
   * Maps a Role DTO to Role Entity
   *
   * @param roleDTO role DTO
   * @return role entity
   */
  public static Role mapToRole(RoleDTO roleDTO) {
    Role role = new Role();
    role.setId(roleDTO.getId());
    role.setName(roleDTO.getName());
    role.setDescription(roleDTO.getDescription());
    return role;
  }

  /**
   * Maps a Degree DTO to Degree Entity
   * @param degreeDTO degree DTO
   * @return degree entity
   */
  public static Degree mapToDegree(DegreeDTO degreeDTO) {
    Degree degree = new Degree();
    degree.setId(degreeDTO.getId());
    degree.setAbbr(degreeDTO.getAbbr());
    degree.setName(degreeDTO.getName());
    return degree;
  }
}
