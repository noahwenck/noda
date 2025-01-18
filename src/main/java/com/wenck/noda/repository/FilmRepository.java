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

    @Query("SELECT f FROM film f JOIN f.primaryLanguage l WHERE l.name = :languageName")
    List<Film> findByPrimaryLanguage(String languageName);

    @Query("SELECT f FROM film f JOIN f.spokenLanguage s WHERE s.name = :languageName")
    List<Film> findBySpokenLanguage(String languageName);

    @Query("SELECT f FROM film f JOIN f.genre g WHERE g.name = :genreName")
    List<Film> findByGenre(String genreName);

    @Query("SELECT f FROM film f JOIN f.studio s WHERE s.name = :studioName")
    List<Film> findByStudio(String studioName);

    @Query("SELECT f FROM film f JOIN f.country c WHERE c.name = :countryName")
    List<Film> findByCountry(String countryName);
}
