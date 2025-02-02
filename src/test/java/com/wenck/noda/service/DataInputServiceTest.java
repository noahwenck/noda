package com.wenck.noda.service;

import com.wenck.noda.TestData;
import com.wenck.noda.entity.Director;
import com.wenck.noda.entity.FilmList;
import com.wenck.noda.entity.film.*;
import com.wenck.noda.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Test Class for {@link DataInputService}
 */
@ExtendWith(MockitoExtension.class)
class DataInputServiceTest {

    @InjectMocks
    private DataInputService dataInputService;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private DirectorRepository directorRepository;
    @Mock
    private FilmListRepository filmListRepository;
    @Mock
    private FilmRepository filmRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private LanguageRepository languageRepository;
    @Mock
    private StudioRepository studioRepository;

    // todo mock objectMapper call, even just to return the same TestData string

    @Test
    void testParseFromJSONFilmsSuccessful() {
        when(filmRepository.save(any(Film.class))).thenReturn(null);
        when(directorRepository.save(any(Director.class))).thenReturn(null);
        when(languageRepository.save(any(Language.class))).thenReturn(null);
        when(countryRepository.save(any(Country.class))).thenReturn(null);
        when(genreRepository.save(any(Genre.class))).thenReturn(null);
        when(studioRepository.save(any(Studio.class))).thenReturn(null);

        final boolean parsed = dataInputService.parseFromJSON(TestData.SUCCESSFUL_FILMS_3, false);
        assertTrue(parsed);
    }

    @Test
    void testParseFromJSONFilmsDuplicateFilmSuccessful() {
        when(filmRepository.save(any(Film.class))).thenReturn(null);
        when(directorRepository.save(any(Director.class))).thenReturn(null);
        when(languageRepository.save(any(Language.class))).thenReturn(null);
        when(countryRepository.save(any(Country.class))).thenReturn(null);
        when(genreRepository.save(any(Genre.class))).thenReturn(null);
        when(studioRepository.save(any(Studio.class))).thenReturn(null);

        final boolean parsed = dataInputService.parseFromJSON(TestData.SUCCESSFUL_FILMS_2_DUPLICATE, false);
        assertTrue(parsed);
    }

    @Test
    void testParseFromJSONFilmsNotJSONFailure() {
        final boolean parsed = dataInputService.parseFromJSON(TestData.FAILURE_FILMS_1_NOT_JSON, false);
        assertFalse(parsed);
    }

    @Test
    void testParseFromJSONListSuccessful() {
        when(filmListRepository.save(any(FilmList.class))).thenReturn(null);
        when(filmRepository.save(any(Film.class))).thenReturn(null);
        when(directorRepository.save(any(Director.class))).thenReturn(null);
        when(languageRepository.save(any(Language.class))).thenReturn(null);
        when(countryRepository.save(any(Country.class))).thenReturn(null);
        when(genreRepository.save(any(Genre.class))).thenReturn(null);
        when(studioRepository.save(any(Studio.class))).thenReturn(null);

        final boolean parsed = dataInputService.parseFromJSON(TestData.SUCCESSFUL_LIST, true);
        assertTrue(parsed);
    }

    /**
     * The following two methods should be enough testing for the following methods:
     *  1. parseEntity()
     *  2. parseDirector()
     *  3. parseCountry()
     *  4. parseStudio()
     *  5. parseGenre()
     */
    @Test
    void testParseEntityAllNewEntities() {
        final Set<String> expectedDirectorSetNames = Set.of("Fede Álvarez", "Wim Wenders", "Robert Eggers");
        
        when(directorRepository.findByName(anyString())).thenReturn(null);
        when(directorRepository.save(any(Director.class))).thenReturn(null);
        
        final Set<Director> directorSet =
                ReflectionTestUtils.invokeMethod(dataInputService, "parseDirector", TestData.DIRECTORS_3);

        assertNotNull(directorSet);
        assertEquals(directorSet.size(), expectedDirectorSetNames.size());
        
        // Make sure set is not only the same size, but contains the correct values
        int directorsFound = 0;
        for (Director director : directorSet) {
            if (expectedDirectorSetNames.contains(director.getName())) {
                directorsFound++;
            }
        }
        assertEquals(directorsFound, expectedDirectorSetNames.size());
    }

    @Test
    void testParseEntitiesAllExistingEntities() {
        final Set<String> expectedDirectorSetNames = Set.of("Fede Álvarez", "Wim Wenders", "Robert Eggers");
        final Director director1 = new Director("Fede Álvarez");
        final Director director2 = new Director("Wim Wenders");
        final Director director3 = new Director("Robert Eggers");

        when(directorRepository.findByName("Fede Álvarez")).thenReturn(director1);
        when(directorRepository.findByName("Wim Wenders")).thenReturn(director2);
        when(directorRepository.findByName("Robert Eggers")).thenReturn(director3);

        final Set<Director> directorSet =
                ReflectionTestUtils.invokeMethod(dataInputService, "parseDirector", TestData.DIRECTORS_3);

        assertNotNull(directorSet);
        assertEquals(directorSet.size(), expectedDirectorSetNames.size());

        // Make sure set is not only the same size, but contains the correct values
        int directorsFound = 0;
        for (Director director : directorSet) {
            if (expectedDirectorSetNames.contains(director.getName())) {
                directorsFound++;
            }
        }
        assertEquals(directorsFound, expectedDirectorSetNames.size());
    }

    @Test
    void testParseEntitiesEmptyJSON() {
        final Set<Director> directorSet =
                ReflectionTestUtils.invokeMethod(dataInputService, "parseDirector", new ArrayList<>());

        assertNotNull(directorSet);
        assertTrue(directorSet.isEmpty());
    }

    // todo: this would be a good test case, HOWEVER, first entity is not saved before we search the second time. May have to rethink this saving
//    @Test
//    void testParseDirectorDuplicateDirectors() {
//        final Set<String> expectedDirectorSetNames = Set.of("Wim Wenders");
//
//        when(directorRepository.findByName(anyString())).thenReturn(null);
//        when(directorRepository.save(any(Director.class))).thenReturn(null);
//
//        final Set<Director> directorSet =
//                ReflectionTestUtils.invokeMethod(dataInputService, "parseDirector", TestData.DIRECTORS_2_DUPLICATE);
//
//        assertNotNull(directorSet);
//        assertEquals(expectedDirectorSetNames.size(), directorSet.size());
//        assertEquals("Wim Wenders", directorSet.getClass().getName());
//    }
}
