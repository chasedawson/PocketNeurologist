package com.chase.pocketneurologist;

/**
 * Created by chase on 3/12/17.
 */

public class SymptomSeverity {

    private String name;
    private String severity;
    private String date;

    public SymptomSeverity(String[] constructor) {
        if(constructor.length != 3)
            throw new ArrayIndexOutOfBoundsException();
        setName(constructor[0]);
        setDate(constructor[1]);
        setSeverity(constructor[2]);
    }

    public String getName() {
        return name;
    }
    public String getSeverity() {
        return severity;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String toString() {
        return getName() + " " + getDate() + " " + getSeverity();
    }
}
