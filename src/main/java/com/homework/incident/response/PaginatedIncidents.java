package com.homework.incident.response;

import com.homework.incident.model.Incident;

import java.util.List;

import java.util.List;

public class PaginatedIncidents {

    private List<Incident> incidents;
    private PaginationResponse pagination;

    // 构造函数
    public PaginatedIncidents(List<Incident> incidents, PaginationResponse pagination) {
        this.incidents = incidents;
        this.pagination = pagination;
    }

    // Getter 和 Setter
    public List<Incident> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<Incident> incidents) {
        this.incidents = incidents;
    }

    public PaginationResponse getPagination() {
        return pagination;
    }

    public void setPagination(PaginationResponse pagination) {
        this.pagination = pagination;
    }
}