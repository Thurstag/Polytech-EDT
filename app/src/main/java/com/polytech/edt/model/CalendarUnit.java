package com.polytech.edt.model;

public enum CalendarUnit {

    Week("Week"),
    Month("Month");

    private String label;

    CalendarUnit(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    /**
     * Method to convert a CalendarUnit into a url parameter (For CalendarURL)
     *
     * @return Parameter
     */
    public String toURLParameter() throws Exception {
        switch (this) {
            case Week:
                return "nbWeeks";

            case Month:
                return "nbWeeks";

            default:
                throw new Exception("Unknown scope: " + this);
        }
    }

    /**
     * Method to transform scope depends on unit
     *
     * @param scope Scope
     * @return Transformed scope
     */
    public int transformScope(int scope) throws Exception {
        switch (this) {
            case Week:
                return scope;

            case Month:
                return scope * 5;

            default:
                throw new Exception("Unknown scope: " + this);
        }
    }
}
