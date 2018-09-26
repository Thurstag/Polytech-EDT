package com.polytech.edt.model;

import com.alamkanak.weekview.WeekViewEvent;
import com.polytech.edt.config.AppConfig;
import com.polytech.edt.config.AppProperty;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AppConfig.class})
public class CalendarTest {

    private static InputStream buffer;

    private static List<String> starts = new ArrayList<>();
    private static List<String> ends = new ArrayList<>();
    private static List<String> labels = new ArrayList<>();
    private static List<String> locations = new ArrayList<>();

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        buffer = new FileInputStream("src/test/res/raw/test-calendar.ics");

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
        ends.add("19:00:00");
        ends.add("12:30:00");
        ends.add("19:00:00");

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

        // Mock config
        PowerMockito.mockStatic(AppConfig.class);
        PowerMockito.when(AppConfig.get(eq(AppProperty.RESOURCES_LIST))).thenReturn("13,1777,1630,1838,1857,1739,1746,1953,2020,1732,1795,1823,2117,346,2139,2154,2180,2218");
    }

    @Test
    public void loadTest() throws Exception {
        // Simple load
        ADECalendar c = new ADECalendar(Collections.singletonList(new ADEResource(2128))).load();
        Assert.assertNotNull(c);

        // Mocked load
        c = mock(ADECalendar.class);
        when(c.fetchCalendar()).thenReturn(new BufferedInputStream(buffer));
        when(c.events()).thenCallRealMethod();
        when(c.load()).thenCallRealMethod();
        c = c.load();

        Calendar calendar;
        int i = 0;
        for (WeekViewEvent event : c.events()) {
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
