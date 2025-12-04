package multibank.DaminiSinghAssignment.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TestDataLoader
 * --------------------------------------------------
 * Utility class responsible for loading static test data
 * from JSON files located under:
 *
 *      src/test/resources/testdata/
 *
 * This is used primarily to validate UI text against expected
 * values in automated tests (e.g., Why Multibank page).
 *
 * Key Responsibilities:
 *  - Load JSON test data bundled in classpath.
 *  - Deserialize JSON into Java types using Jackson.
 *  - Provide easy reusable methods for tests needing expected input data.
 */
public class TestDataLoader {

    // Jackson ObjectMapper instance used for JSON parsing
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Loads expected text content for the "Why Multibank?" page.
     *
     * File Location:
     *   src/test/resources/testdata/why_multilink_expected_texts.json
     *
     * JSON Structure:
     * {
     *   "mainCardsHeadings": [...],
     *   "mainCardsSubHeadings": [...],
     *   "ourAdvantagesHeadings": [...],
     *   "buttons": [...]
     * }
     *
     * @return Map<String, List<String>>
     *         where each JSON key maps to a list of text values.
     *
     * @throws IOException if JSON fails to load or parse.
     */
    public static Map<String, List<String>> loadWhyMultiLinkExpectedTexts() throws IOException {

        // Load JSON from classpath as InputStream
        try (InputStream is = TestDataLoader.class
                .getResourceAsStream("/testdata/why_multilink_expected_texts.json")) {

            // Fail fast if file is missing
            if (is == null) {
                throw new IllegalStateException("Test data JSON not found on classpath.");
            }

            // Deserialize JSON into Map<String, List<String>>
            return mapper.readValue(is, new TypeReference<Map<String, List<String>>>() {});
        }
    }
}
