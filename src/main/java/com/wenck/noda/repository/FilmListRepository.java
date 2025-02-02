package com.wenck.noda.repository;

import com.wenck.noda.entity.FilmList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmListRepository extends JpaRepository<FilmList, Integer> {

    @Query("SELECT l FROM list l WHERE l.name = :name")
    FilmList findByName(String name);
}
