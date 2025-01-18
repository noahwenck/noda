package com.wenck.noda.entity;

import com.wenck.noda.entity.film.Film;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "director")
public class Director extends BaseEntity {

    private int id;
    private String name;
    private Set<Film> films;

    public Director() {}

    public Director(String name) {
        this.name = name;
        this.films = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "DIRECTOR_ID")
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

    @ManyToMany(mappedBy = "director")
    public Set<Film> getFilms() {
        return films;
    }

    public void setFilms(Set<Film> films) {
        this.films = films;
    }
}
