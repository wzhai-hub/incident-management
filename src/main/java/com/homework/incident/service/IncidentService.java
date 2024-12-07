package com.homework.incident.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.Collections;

import com.homework.incident.model.Incident;

@Service
public class IncidentService {

    private final Map<Long, Incident> incidentStorage = new ConcurrentHashMap<>();

    public Incident createIncident(Incident incident) {
        incidentStorage.put(incident.getAccidentId(), incident);
        return incident;
    }

    public Incident findById(Long id) {
        return incidentStorage.get(id);
    }

    public void addIncident(Incident incident) {
        if (incident == null || incident.getAccidentId() == null || incident.getTitle() == null || incident.getTitle().isEmpty() || incident.getStatus() == null || incident.getStatus().isEmpty()) {
            throw new IllegalArgumentException("Incident details are required and cannot be null or empty.");
        }
        incidentStorage.put(incident.getAccidentId(), incident);
    }


    public boolean existsById(long accidentId) {
        return incidentStorage.containsKey(accidentId);
    }

    public void deleteIncident(long id) {
        if (!incidentStorage.containsKey(id)) {
            throw new IllegalArgumentException("Incident not found");
        }
        incidentStorage.remove(id);
    }

    public void updateIncident(long accidentId, Incident updatedIncident) {
        Incident existingIncident = incidentStorage.get(accidentId);
        if (existingIncident == null) {
            throw new IllegalArgumentException("Incident not found");
        }

        if (updatedIncident.getTitle() != null) {
            existingIncident.setTitle(updatedIncident.getTitle());
        }
        if (updatedIncident.getDescription() != null) {
            existingIncident.setDescription(updatedIncident.getDescription());
        }
        if (updatedIncident.getLocation() != null) {
            existingIncident.setLocation(updatedIncident.getLocation());
        }
        existingIncident.setSeverity(updatedIncident.getSeverity());
        if (updatedIncident.getStatus() != null) {
            existingIncident.setStatus(updatedIncident.getStatus());
        }

        incidentStorage.put(accidentId, existingIncident);
    }

    public List<Incident> getAllIncidents(String status, Integer severity, int fromIndex, int toIndex) {
        // 筛选符合条件的数据
        List<Incident> incidents = incidentStorage.values().stream()
                .filter(incident -> (status == null || status.equals(incident.getStatus())) &&
                        (severity == null || severity.equals(incident.getSeverity())))
                .collect(Collectors.toList());

        // 确保 fromIndex 和 toIndex 的范围合法
        if (fromIndex >= incidents.size()) {
            return Collections.emptyList();  // 如果起始索引超出数据范围，返回空列表
        }
        toIndex = Math.min(toIndex, incidents.size());  // 确保结束索引不超出范围

        // 返回分页后的数据
        return incidents.subList(fromIndex, toIndex);
    }

    // 获取事件总数
    public int getTotalRecords(String status, Integer severity) {
        return (int) incidentStorage.values().stream()
                .filter(incident -> (status == null || status.equals(incident.getStatus())) &&
                        (severity == null || severity.equals(incident.getSeverity())))
                .count();
    }
}
