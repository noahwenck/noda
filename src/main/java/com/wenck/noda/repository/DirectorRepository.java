package com.wenck.noda.repository;

import com.wenck.noda.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {

    @Query("SELECT d FROM director d WHERE d.name = :name")
    Director findByName(String name);

}
