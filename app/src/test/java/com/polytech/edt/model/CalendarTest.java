package com.polytech.edt.model;

import com.alamkanak.weekview.WeekViewEvent;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalendarTest {

    private static InputStream buffer;

    private static List<String> starts = new ArrayList<>();
    private static List<String> ends = new ArrayList<>();
    private static List<String> labels = new ArrayList<>();
    private static List<String> locations = new ArrayList<>();

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        buffer = new FileInputStream("src/test/resources/test-calendar.ics");

        starts.add("08:30:00");
        starts.add("13:30:00");
        starts.add("08:30:00");
        starts.add("08:30:00");
        starts.add("13:30:00");
        starts.add("08:30:00");
        starts.add("13:30:00");
        starts.add("08:30:00");
        starts.add("13:30:00");
        starts.add("08:30:00");
        starts.add("13:30:00");

        ends.add("12:30:00");
        ends.add("17:30:00");
        ends.add("12:30:00");
        ends.add("12:30:00");
        ends.add("17:30:00");
        ends.add("12:30:00");
        ends.add("17:30:00");
        ends.add("12:30:00");
        ends.add("20:00:00");
        ends.add("12:30:00");
        ends.add("20:00:00");

        labels.add("Communication professionnelle");
        labels.add("Algèbre");
        labels.add("Analyse");
        labels.add("Economie d'Entreprise");
        labels.add("Anglais");
        labels.add("Communication professionnelle");
        labels.add("Evaluation");
        labels.add("Législation sociale : droit du travail");
        labels.add("Parrainage");
        labels.add("Analyse");
        labels.add("Parrainage");

        locations.add("620 - A101");
        locations.add("620 - A102");
        locations.add("620 - A104");
        locations.add("620 - A102");
        locations.add("Centre Langues - Batiment Eiffel  (3è et 4è étage)");
        locations.add("620 - A200");
        locations.add("620 - Amphi");
        locations.add("620 - A200");
        locations.add("UNDEFINED");
        locations.add("620 - A104");
        locations.add("UNDEFINED");
    }

    @Test
    public void loadTest() throws Exception {
        // Simple load
        new ADECalendar(Collections.singletonList(new ADEResource(2128))).load();

        // Mocked load
        ADECalendar mock = mock(ADECalendar.class);
        when(mock.fetchCalendar()).thenReturn(new BufferedInputStream(buffer));
        when(mock.events()).thenCallRealMethod();
        when(mock.load()).thenCallRealMethod();
        mock = mock.load();

        Calendar calendar;
        int i = 0;
        for (WeekViewEvent event : mock.events()) {
            calendar = event.getStartTime();
            Assert.assertEquals(starts.get(i), String.format("%1$2s", calendar.get(Calendar.HOUR_OF_DAY)).replace(" ", "0") + ":" + String.format("%1$2s", calendar.get(Calendar.MINUTE)).replace(" ", "0") + ":" + String.format("%1$2s", calendar.get(Calendar.SECOND)).replace(" ", "0"));

            calendar = event.getEndTime();
            Assert.assertEquals(ends.get(i), String.format("%1$2s", calendar.get(Calendar.HOUR_OF_DAY)).replace(" ", "0") + ":" + String.format("%1$2s", calendar.get(Calendar.MINUTE)).replace(" ", "0") + ":" + String.format("%1$2s", calendar.get(Calendar.SECOND)).replace(" ", "0"));

            Assert.assertEquals(labels.get(i), event.getName());
            Assert.assertEquals(locations.get(i), event.getLocation());

            i++;
        }
    }
}
