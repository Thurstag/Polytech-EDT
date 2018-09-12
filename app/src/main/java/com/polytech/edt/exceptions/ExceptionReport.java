package com.polytech.edt.exceptions;

import java.io.Serializable;

public class ExceptionReport implements Serializable {

    private Throwable throwable;
    private long threadId;

    /**
     * Constructor
     *
     * @param threadId  Thread ID
     * @param throwable Throwable
     */
    public ExceptionReport(long threadId, Throwable throwable) {
        this.threadId = threadId;
        this.throwable = throwable;
    }
}
