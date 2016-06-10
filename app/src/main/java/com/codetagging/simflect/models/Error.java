package com.codetagging.simflect.models;

import java.io.Serializable;

/**
 * Created by govind on 23/05/16.
 */
public class Error implements Serializable {

    private String errorText;

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
