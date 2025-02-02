package com.wenck.noda.entity;

import com.wenck.noda.entity.film.Film;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity comprising Lists of films, pulled from Letterboxd or created by user.
 */
@Entity(name = "list")
public class FilmList {
    private int id;
    private String name;
    private boolean ranked;
    private List<Film> films;

    public FilmList() {

    }

    public FilmList(String name) {
        this.name = name;
        this.films = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "LIST_ID")
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

    @ManyToMany(mappedBy = "listsFoundIn")
    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }
}
