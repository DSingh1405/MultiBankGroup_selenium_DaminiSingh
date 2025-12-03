package multibank.DaminiSinghAssignment.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestDataLoader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, List<String>> loadWhyMultiLinkExpectedTexts() throws IOException {
        //File path: src/test/resources/testdata/why_multilink_expected_texts.json
        try (InputStream is = TestDataLoader.class
                .getResourceAsStream("/testdata/why_multilink_expected_texts.json")) {

            if (is == null) {
                throw new IllegalStateException("Test data JSON not found on classpath.");
            }

            return mapper.readValue(is, new TypeReference<Map<String, List<String>>>() {});
        }
    }
}