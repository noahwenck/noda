package com.wenck.noda.repository;

import com.wenck.noda.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Integer> {

    /**
     * Get the {@link Language} associated with the name
     */
    @Query("SELECT l FROM language l WHERE l.name = :name")
    Language findByName(String name);

    @Query("SELECT l FROM language l WHERE l.primaryLanguage")
    List<Language> findPrimaryLanguages();
}
