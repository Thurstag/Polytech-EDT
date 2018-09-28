package com.polytech.edt.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.polytech.edt.model.CalendarUnit;

public class CalendarScope {
    //region Fields

    @JsonProperty
    private CalendarUnit unit;

    @JsonProperty
    private int duration;

    @JsonProperty
    private int viewScope;

    //endregion

    //region Constructors

    /**
     * Constructor
     *
     * @param scope     Calendar unit
     * @param duration  Scope duration
     * @param viewScope UI unit
     */
    public CalendarScope(@JsonProperty("unit") CalendarUnit scope, @JsonProperty("duration") int duration, @JsonProperty("viewScope") int viewScope) {
        this.unit = scope;
        this.duration = duration;
        this.viewScope = viewScope;
    }

    //endregion

    //region GET & SET

    public int viewScope() {
        return viewScope;
    }

    public void setViewScope(int viewScope) {
        this.viewScope = viewScope;
    }

    public CalendarUnit unit() {
        return unit;
    }

    public void setUnit(CalendarUnit unit) {
        this.unit = unit;
    }

    public int duration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    //endregion
}
