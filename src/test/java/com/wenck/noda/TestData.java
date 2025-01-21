package com.wenck.noda;

import java.util.List;

/**
 * Test data for use in test classes.
 *
 * Format of Descriptions:
 *      1. Expected Successful cases -> {Number of Entries} {Type of Entries} - {Unique/Duplicate}, Successful
 *      2. Expected Failure cases -> {Number of Entries} {Type of Entries} - {Reason for failure}, Failure
 */
public final class TestData {

    /**
     * 3 Films - Unique, Successful
     */
    public static final String SUCCESSFUL_DATA_3 = """
            [{"Name": "Alien: Romulus", "Director": ["Fede Álvarez"], "Year": 2024, "Synopsis": "While scavenging the deep ends of a derelict space station, a group of young space colonizers come face to face with the most terrifying life form in the universe.", "Primary Language": "English", "Spoken Language": ["English"], "Country": ["USA"], "Runtime": 119, "Average Rating": 3.61, "Genre": ["Science Fiction", "Horror"], "Studio": ["20th Century Studios", "Scott Free Productions", "Brandywine Productions"]},
            {"Name": "Perfect Days", "Director": ["Wim Wenders"], "Year": 2023, "Synopsis": "Hirayama is content with his life as a toiletcleaner in Tokyo. Outside of his structured routine, he cherishes music on cassette tapes, books, and taking photos of trees. Through unexpected encounters, he reflects on finding beauty in the world.", "Primary Language": "Japanese", "Spoken Language":["Japanese", "English"], "Country": ["Germany", "Japan"], "Runtime": 124, "Average Rating": 4.29, "Genre": ["Drama"], "Studio": ["Master Mind", "Wenders Images", "NEON"]}, 
            {"Name": "The Northman", "Director": ["Robert Eggers"], "Year": 2022, "Synopsis":"Prince Amleth is on the verge of becoming a man when his father is brutally murdered by his uncle, who kidnaps the boy's mother. Two decades later, Amleth is now a Viking who's on a mission to save his mother, kill his uncle and avenge his father.", "Primary Language": "English", "Spoken Language": ["English", "Icelandic"], "Country": ["USA"], "Runtime": 137, "Average Rating": 3.78, "Genre": ["Fantasy", "Adventure", "Action"], "Studio": ["New Regency Pictures", "Regency Enterprises", "Square Peg"]}]
            """;

    /**
     * 2 Films - Duplicate, Successful
     */
    public static final String SUCCESSFUL_DATA_2_DUPLICATE = """
            [{"Name": "Perfect Days", "Director": ["Wim Wenders"], "Year": 2023, "Synopsis": "Hirayama is content with his life as a toiletcleaner in Tokyo. Outside of his structured routine, he cherishes music on cassette tapes, books, and taking photos of trees. Through unexpected encounters, he reflects on finding beauty in the world.", "Primary Language": "Japanese", "Spoken Language":["Japanese", "English"], "Country": ["Germany", "Japan"], "Runtime": 124, "Average Rating": 4.29, "Genre": ["Drama"], "Studio": ["Master Mind", "Wenders Images", "NEON"]}, 
            {"Name": "Perfect Days", "Director": ["Wim Wenders"], "Year": 2023, "Synopsis": "Hirayama is content with his life as a toiletcleaner in Tokyo. Outside of his structured routine, he cherishes music on cassette tapes, books, and taking photos of trees. Through unexpected encounters, he reflects on finding beauty in the world.", "Primary Language": "Japanese", "Spoken Language":["Japanese", "English"], "Country": ["Germany", "Japan"], "Runtime": 124, "Average Rating": 4.29, "Genre": ["Drama"], "Studio": ["Master Mind", "Wenders Images", "NEON"]}]
            """;

    /**
     * 1 Film - Not JSON, Failure
     */
    public static final String FAILURE_DATA_1_NOT_JSON = """
            "Name": "Perfect Days", "Director": ["Wim Wenders"], "Year": 2023, "Synopsis": "Hirayama is content with his life as a toiletcleaner in Tokyo. Outside of his structured routine, he cherishes music on cassette tapes, books, and taking photos of trees. Through unexpected encounters, he reflects on finding beauty in the world.", "Primary Language": "Japanese", "Spoken Language":["Japanese", "English"], "Country": ["Germany", "Japan"], "Runtime": 124, "Average Rating": 4.29, "Genre": ["Drama"], "Studio": ["Master Mind", "Wenders Images", "NEON"]}
            """;

    /**
     * 3 Directors - Unique, Successful
     */
    public static final List<String> DIRECTORS_3 = List.of("Fede Álvarez", "Wim Wenders", "Robert Eggers");

}
