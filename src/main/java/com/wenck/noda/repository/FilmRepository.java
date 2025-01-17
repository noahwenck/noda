package com.wenck.noda.repository;

import com.wenck.noda.entity.film.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {

    @Query("SELECT f FROM film f WHERE f.name = :name AND f.year = :year")
    Film findByNameAndYear(String name, int year);

    @Query("SELECT f FROM film f WHERE f.name = :name")
    List<Film> findByName(String name);

    @Query("SELECT f FROM film f WHERE f.year = ?1")
    List<Film> findByYear(int year);

    @Query("SELECT f FROM film f JOIN f.director d WHERE d.id = :id")
    List<Film> findByDirector(int id);

    @Query("SELECT f FROM film f JOIN f.spokenLanguage s WHERE s.name = :name")
    List<Film> findBySpokenLanguage(String name);

    @Query("SELECT f FROM film f JOIN f.genre g WHERE g.name = :name")
    List<Film> findByGenre(String name);

    @Query("SELECT f FROM film f JOIN f.studio s WHERE s.name = :name")
    List<Film> findByStudio(String name);

    @Query("SELECT f FROM film f JOIN f.country c WHERE c.name = :name")
    List<Film> findByCountry(String name);
}
