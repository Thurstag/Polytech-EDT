package com.polytech.edt.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytech.edt.App;
import com.polytech.edt.model.ADEResource;
import com.polytech.edt.model.CalendarUnit;
import com.polytech.edt.util.FileIO;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class UserConfig {
    //region Fields

    private static ObjectMapper mapper = new ObjectMapper();

    @JsonProperty
    private CalendarScope calendarScope;

    @JsonProperty
    private Set<ADEResource> groups;

    //endregion

    //region GET

    /**
     * Method to know if config exists
     *
     * @return True or False
     */
    public static boolean exists() {
        return FileIO.exists(FileIO.ROOT_PATH + "files/config.json");
    }

    /**
     * Method to create config
     *
     * @return Config
     */
    public static UserConfig create() throws JsonProcessingException {
        UserConfig config = new UserConfig();

        // Define default values
        config.calendarScope = new CalendarScope(CalendarUnit.Week, 1, 3);
        config.groups = new HashSet<>();

        // Save
        save(config);

        return config;
    }

    //endregion

    //region Methods

    /**
     * Method to save config
     *
     * @param config Config
     */
    public static void save(UserConfig config) throws JsonProcessingException {
        // Encode to json & save into a file
        FileIO.write(App.context, "config.json", mapper.writeValueAsString(config));
    }

    /**
     * Method to load config
     *
     * @return Config
     */
    public static UserConfig load() throws IOException {
        return mapper.readValue(FileIO.read(App.context, "config.json").getBytes(), new TypeReference<UserConfig>() {
        });
    }

    public void setCalendarViewScope(int calendarScope) throws JsonProcessingException {
        this.calendarScope.setViewScope(calendarScope);

        save(this);
    }

    public Set<ADEResource> groups() {
        return groups;
    }

    public void setGroups(Set<ADEResource> groups) throws JsonProcessingException {
        this.groups = groups;

        save(this);
    }

    public CalendarScope calendarScope() {
        return calendarScope;
    }

    //endregion
}
