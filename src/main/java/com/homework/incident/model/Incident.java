package com.homework.incident.model;

import javax.validation.constraints.NotNull;

public class Incident {
    @NotNull(message = "Accident ID is required.")
    private Long accidentId;// 必须提供事故 ID
    private String title;
    private String description;
    private String location;
    private int severity;
    private String status;

    // 无参构造函数
    public Incident() {}

    // 带参构造函数
    public Incident(Long accidentId, String title, String description, String location, Integer severity, String status) {
        this.accidentId = accidentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.severity = severity;
        this.status = status;
    }
    public Long getAccidentId() {
        return accidentId;
    }

    public void setAccidentId(Long accidentId) {
        this.accidentId = accidentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
