package com.wenck.noda.repository;

import com.wenck.noda.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CountryRepository extends JpaRepository<Country, String> {

    @Query("SELECT c FROM country c WHERE c.name = :name")
    Country findByName(String name);
}
