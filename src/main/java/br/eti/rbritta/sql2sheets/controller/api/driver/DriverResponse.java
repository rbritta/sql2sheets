package br.eti.rbritta.sql2sheets.controller.api.driver;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@Getter
@AllArgsConstructor
public class DriverResponse {

    private final String filename;

    public static DriverResponse of(File file) {
        return new DriverResponse(file.getName());
    }
}
