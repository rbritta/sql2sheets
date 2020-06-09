package br.eti.rbritta.sql2sheets.service;

import br.eti.rbritta.sql2sheets.config.SpreadsheetProperties;
import br.eti.rbritta.sql2sheets.model.Task;
import br.eti.rbritta.sql2sheets.model.TaskSheet;
import br.eti.rbritta.sql2sheets.scheduler.TaskScheduler;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TaskServiceTest {

    private static final LocalDateTime DEFAULT_DATE = LocalDateTime.now();
    private static final String DEFAULT_URL = "https://default-url/";

    @Mock
    private SpreadsheetProperties properties;

    @Mock
    private TaskScheduler scheduler;

    @InjectMocks
    private TaskService service;

    @BeforeEach
    void setUp() {
        initMocks(this);
        when(scheduler.getNextExecutionTime(any())).thenReturn(DEFAULT_DATE);
        when(properties.getUrl(any())).thenReturn(DEFAULT_URL);
    }

    @Test
    void givenTaskWithSheetWhenCompleteThenSetUrlAndNextExecution() {
        final Task task = new Task();
        task.setSheet(new TaskSheet());
        service.complete(task);
        Assert.assertEquals(DEFAULT_DATE, task.getNextExecution());
        Assert.assertNotNull(task.getSheet());
        Assert.assertEquals(DEFAULT_URL, task.getSheet().getUrl());
    }

    @Test
    void givenTaskWithoutSheetWhenCompleteThenSetOnlyNextExecution() {
        final Task task = new Task();
        service.complete(task);
        Assert.assertEquals(DEFAULT_DATE, task.getNextExecution());
        Assert.assertNull(task.getSheet());
    }
}