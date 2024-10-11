package com.wenck.noda.entity.film;

import com.wenck.noda.entity.film.Film;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "studio")
public class Studio {

    private String name;
    private Set<Film> films;

    public Studio() {}

    public Studio(String name) {
        this.name = name;
        this.films = new HashSet<>();
    }

    @Id
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "studio")
    public Set<Film> getFilms() {
        return films;
    }

    public void setFilms(Set<Film> films) {
        this.films = films;
    }
}
