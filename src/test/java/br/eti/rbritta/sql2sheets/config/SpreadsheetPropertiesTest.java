package br.eti.rbritta.sql2sheets.config;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpreadsheetPropertiesTest {

    private static final String DEFAULT_PATTERN = "https://docs.google.com/spreadsheets/d/{0}/";

    private SpreadsheetProperties properties;

    @BeforeEach
    void setUp() {
        properties = new SpreadsheetProperties();
        properties.setUrlPattern(DEFAULT_PATTERN);
    }

    @Test
    void givenDefaultPatternWhenSheetIdIsOkThenGetUrlReturnsCompleteUrl() {
        Assert.assertEquals(
                "https://docs.google.com/spreadsheets/d/123456789/",
                properties.getUrl("123456789"));
    }

    @Test
    void givenDefaultPatternWhenSheetIdIsNullThenGetUrlReturnsEmpty() {
        Assert.assertEquals("", properties.getUrl(null));
    }

    @Test
    void givenDefaultPatternWhenSheetIdIsEmptyThenGetUrlReturnsEmpty() {
        Assert.assertEquals("", properties.getUrl(""));
    }

    @Test
    void givenNullPatternWhenSheetIdIsNullThenGetUrlReturnsEmpty() {
        properties.setUrlPattern(null);
        Assert.assertEquals("", properties.getUrl(null));
    }

    @Test
    void givenNullPatternWhenSheetIdIsEmptyThenGetUrlReturnsEmpty() {
        properties.setUrlPattern(null);
        Assert.assertEquals("", properties.getUrl(""));
    }
}