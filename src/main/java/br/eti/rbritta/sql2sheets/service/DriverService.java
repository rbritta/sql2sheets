package br.eti.rbritta.sql2sheets.service;

import br.eti.rbritta.sql2sheets.exception.InvalidDriverFileException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static org.apache.http.util.TextUtils.isBlank;

@Service
public class DriverService {

    private static final Logger logger = LoggerFactory.getLogger(DriverService.class);

    @Autowired
    private UserService userService;

    @Value("${sql2sheets.drivers.directory}")
    private String driversDir;

    public File add(String filename, byte[] data) throws IOException {
        if (isBlank(filename) || data.length == 0) {
            logger.error("Invalid Driver file");
            throw new InvalidDriverFileException();
        }

        ensureDriversDir();

        final String filePath = driversDir + "/" + filename;
        final File file = new File(filePath);

        if (file.delete()) {
            logger.info("Driver {} overwrited by {}", file.getAbsolutePath(), userService.getCurrentUsername());
        }

        try {
            IOUtils.write(data, new FileOutputStream(file));
            logger.info("Driver {} uploaded by {}", file.getAbsolutePath(), userService.getCurrentUsername());
            return file;
        } catch (IOException e) {
            logger.error("Could not create Driver file: " + filePath, e);
            throw e;
        }
    }

    private void ensureDriversDir() {
        final File dir = new File(driversDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public <T> List<T> getAllAs(Function<File, T> function) {
        final File[] fileArray = new File(driversDir).listFiles((dir, name) -> name.endsWith(".jar"));

        if (isNull(fileArray)) {
            return new ArrayList<>();
        }
        return Arrays.stream(fileArray)
                .map(function)
                .collect(Collectors.toList());
    }

    public void delete(String id) {
        final String filePath = driversDir + "/" + id;
        final File file = new File(filePath);

        if (file.delete()) {
            logger.info("Driver {} deleted by {}", file.getAbsolutePath(), userService.getCurrentUsername());
        } else {
            logger.error("Could not delete driver file {}", file.getAbsolutePath());
        }
    }
}
