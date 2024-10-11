package com.wenck.noda.repository;

import com.wenck.noda.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GenreRepository extends JpaRepository<Genre, String> {

    @Query("SELECT g from genre g WHERE g.name = :name")
    Genre findByName(String name);
}
