package com.polytech.edt.model;


public enum Resource {

    //region Constants

    APP3_TC_GROUP_C(2128);
    // TODO: Add more !

    //endregion

    //region Fields

    private int id;

    //endregion

    //region Constructors

    private Resource(int id) {
        this.id = id;
    }

    //endregion

    //region Methods

    public int id() {
        return id;
    }

    @Override
    public String toString() {
        return id() + "";
    }

    //endregion
}
