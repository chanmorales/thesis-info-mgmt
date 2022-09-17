package com.portfolio.dev.thesisinfomgmt.dtos;

import com.portfolio.dev.thesisinfomgmt.entities.Degree;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DegreeDTO {

  /**
   * Creates a new Degree DTO based on the given Degree Entity
   *
   * @param degree degree entity
   */
  public DegreeDTO(Degree degree) {
    this.setId(degree.getId());
    this.setAbbr(degree.getAbbr());
    this.setName(degree.getName());
  }

  private long id;

  private String abbr;

  private String name;
}
