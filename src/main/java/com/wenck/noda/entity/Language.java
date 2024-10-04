package com.wenck.noda.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name="language")
public class Language {

    private int id;
    private String name;
    private Set<Film> films;

    public Language() {

    }

    public Language(String name) {
        this.name = name;
        this.films = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "LANGUAGE_ID")
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

    @ManyToMany(mappedBy = "spokenLanguage")
    public Set<Film> getFilms() {
        return films;
    }

    public void setFilms(Set<Film> films) {
        this.films = films;
    }
}
