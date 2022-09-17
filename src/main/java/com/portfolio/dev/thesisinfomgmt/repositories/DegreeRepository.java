package com.portfolio.dev.thesisinfomgmt.repositories;

import com.portfolio.dev.thesisinfomgmt.entities.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Long> { }
