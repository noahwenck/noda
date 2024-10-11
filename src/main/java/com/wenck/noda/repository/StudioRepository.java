package com.wenck.noda.repository;

import com.wenck.noda.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudioRepository extends JpaRepository<Studio, String> {

    @Query("SELECT s from studio s WHERE s.name = :name")
    Studio findByName(String name);
}
