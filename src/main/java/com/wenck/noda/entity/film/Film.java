package com.wenck.noda.entity.film;

import com.wenck.noda.entity.*;
import jakarta.persistence.*;

import java.util.Set;

@Entity(name = "film")
public class Film extends BaseEntity {

    private int id;
    private String name;
    private Set<Director> director;
    private int year;
    private Language primaryLanguage;
    private Set<Language> spokenLanguage;
    private Set<Country> country;
    private int runtime;
    private double averageRating;
    private Set<Genre> genre;
    private Set<Studio> studio;

    public Film() {
        // Hi there!
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILM_ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "NAME", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany
    @JoinTable(
            name = "film_director",
            joinColumns = @JoinColumn(name = "FILM_ID"),
            inverseJoinColumns = @JoinColumn(name = "DIRECTOR_ID"))
    public Set<Director> getDirector() {
        return director;
    }

    public void setDirector(Set<Director> director) {
        this.director = director;
    }

    @Column(name = "YEAR")
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @ManyToOne
    @JoinColumn(name = "PRIMARY_LANGUAGE")
    public Language getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(Language primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    @ManyToMany
    @JoinTable(
            name = "film_language",
            joinColumns = @JoinColumn(name = "FILM_ID"),
            inverseJoinColumns = @JoinColumn(name = "LANGUAGE_ID"))
    public Set<Language> getSpokenLanguage() {
        return spokenLanguage;
    }

    public void setSpokenLanguage(Set<Language> spokenLanguage) {
        this.spokenLanguage = spokenLanguage;
    }

    @ManyToMany
    @JoinTable(
            name = "film_country",
            joinColumns = @JoinColumn(name = "FILM_ID"),
            inverseJoinColumns = @JoinColumn(name = "COUNTRY")
    )
    public Set<Country> getCountry() {
        return country;
    }

    public void setCountry(Set<Country> country) {
        this.country = country;
    }

    @Column(name = "RUNTIME")
    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    @Column(name = "AVERAGE_RATING")
    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    @ManyToMany
    @JoinTable(
            name = "film_genre",
            joinColumns = @JoinColumn(name = "FILM_ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE")
    )
    public Set<Genre> getGenre() {
        return genre;
    }

    public void setGenre(Set<Genre> genre) {
        this.genre = genre;
    }

    @ManyToMany
    @JoinTable(
            name = "film_studio",
            joinColumns = @JoinColumn(name = "FILM_ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE")
    )
    public Set<Studio> getStudio() {
        return studio;
    }

    public void setStudio(Set<Studio> studio) {
        this.studio = studio;
    }
}
