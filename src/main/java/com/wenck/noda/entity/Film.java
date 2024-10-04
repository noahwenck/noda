package com.wenck.noda.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity(name="film")
public class Film {

    private int id;
    private String name;
    private Set<Director> director;
    private int year;
    // todo: convert to language
    private String primaryLanguage;
    private Set<Language> spokenLanguage;
    private Set<String> country;
    private int runtime;
    private double averageRating;
    private Set<String> genre;
    private Set<String> studio;

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

//    @ManyToOne
    @Column(name = "PRIMARY_LANGUAGE")
    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
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

    @ElementCollection
    @Column(name = "COUNTRY")
    public Set<String> getCountry() {
        return country;
    }

    public void setCountry(Set<String> country) {
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

    @ElementCollection
    @Column(name = "GENRE")
    public Set<String> getGenre() {
        return genre;
    }

    public void setGenre(Set<String> genre) {
        this.genre = genre;
    }

    @ElementCollection
    @Column(name = "STUDIO")
    public Set<String> getStudio() {
        return studio;
    }

    public void setStudio(Set<String> studio) {
        this.studio = studio;
    }
}
