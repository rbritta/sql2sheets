package br.eti.rbritta.sql2sheets.service;

import br.eti.rbritta.sql2sheets.model.TaskSheet;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;

@Service
public class SpreadsheetService {

	public Sheets getSheets(TaskSheet taskSheet) throws GeneralSecurityException, IOException {
		return new Sheets.Builder(
				GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(), createCredential(taskSheet))
			.setApplicationName("Sql 2 Sheets")
			.build();
	}

	private GoogleCredential createCredential(TaskSheet taskSheet) throws IOException {
		return GoogleCredential.fromStream(new ByteArrayInputStream(taskSheet.getAuthorization().getBytes()))
				.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
	}

	public void clear(Sheets sheets, String id, String dataRange) throws IOException {
		sheets.spreadsheets()
				.values()
				.clear(id, dataRange, new ClearValuesRequest())
				.execute();
	}

	private List<Object> parseMapToList(Map<String, Object> map) {
		return map.values().stream()
				.map(this::parse)
				.collect(Collectors.toList());
	}

	public void fill(Sheets sheets, String id, String dataRange, List<Map<String, Object>> data) throws IOException {
		final List<List<Object>> values = data.stream()
				.map(this::parseMapToList)
				.collect(Collectors.toList());
		clear(sheets, id, dataRange);
		updateSpreadsheet(sheets, id, dataRange, values);
	}

	public void fill(Sheets sheets, String id, String dataRange, Object value) throws IOException {
		final List<List<Object>> values = asList(asList(parse(value)));
		clear(sheets, id, dataRange);
		updateSpreadsheet(sheets, id, dataRange, values);
	}

	public Object parse(Object value) {
		if (isNull(value)) {
			return "";
		}
		return value.toString();
	}

	private void updateSpreadsheet(Sheets sheets, String id, String dataRange, List<List<Object>> values) throws IOException {
		sheets.spreadsheets()
				.values()
				.update(id, dataRange, new ValueRange().setValues(values))
				.setValueInputOption("USER_ENTERED")
				.execute();
	}
}