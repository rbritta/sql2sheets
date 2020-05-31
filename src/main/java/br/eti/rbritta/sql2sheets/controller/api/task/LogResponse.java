package br.eti.rbritta.sql2sheets.controller.api.task;

import br.eti.rbritta.sql2sheets.model.TaskLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Getter
@AllArgsConstructor
public class LogResponse {

    private final List<String> lines;

    public static LogResponse of(String line) {
        return new LogResponse(asList(line));
    }

    public static LogResponse of(List<TaskLog> lines) {
        return new LogResponse(lines.stream()
                .map(TaskLog::asLine)
                .collect(Collectors.toList()));
    }
}
