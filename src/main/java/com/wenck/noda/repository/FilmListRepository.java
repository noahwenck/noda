package com.wenck.noda.repository;

import com.wenck.noda.entity.FilmList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmListRepository extends JpaRepository<FilmList, Integer> {

    // List because we are (as of time of commenting) allowing lists with duplicate names
    @Query("SELECT l FROM list l WHERE l.name = :name")
    List<FilmList> findByName(String name);
}
