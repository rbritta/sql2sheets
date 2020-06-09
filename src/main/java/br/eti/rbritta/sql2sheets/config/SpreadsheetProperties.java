package br.eti.rbritta.sql2sheets.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;

import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Setter
@Configuration
@ConfigurationProperties("sql2sheets.spreadsheet")
public class SpreadsheetProperties {

    private String urlPattern;

    public String getUrl(String sheetId) {
        return isBlank(urlPattern) || isBlank(sheetId) ? EMPTY : MessageFormat.format(urlPattern, sheetId);
    }

}