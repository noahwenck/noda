package com.wenck.noda.entity.film;

import com.wenck.noda.entity.BaseEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name="language")
public class Language extends BaseEntity {
    private String name;
    private Set<Film> films;
    private boolean primaryLanguage;

    public Language() {

    }

    public Language(String name, boolean primaryLanguage) {
        this.name = name;
        this.films = new HashSet<>();
        this.primaryLanguage = primaryLanguage;
    }

    @Id
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

    @Column(name = "PRIMARY_LANGUAGE")
    public boolean isPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(boolean primary) {
        this.primaryLanguage = primary;
    }
}
