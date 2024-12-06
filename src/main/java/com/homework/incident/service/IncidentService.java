package com.homework.incident.service;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import com.homework.incident.model.Incident;

@Service
public class IncidentService {

    private final Map<Long, Incident> incidentStorage = new ConcurrentHashMap<>();

    public Incident createIncident(Incident incident) {
        incidentStorage.put(incident.getAccidentId(), incident);
        return incident;
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

        existingIncident.setTitle(updatedIncident.getTitle());
        existingIncident.setDescription(updatedIncident.getDescription());
        existingIncident.setLocation(updatedIncident.getLocation());
        existingIncident.setSeverity(updatedIncident.getSeverity());
        existingIncident.setStatus(updatedIncident.getStatus());

        incidentStorage.put(accidentId, existingIncident);
    }

    // 获取所有事件（可按条件筛选）
    public List<Incident> getAllIncidents(String status, Integer severity, int page, int limit) {
        // 先筛选数据
        List<Incident> incidents = incidentStorage.values().stream()
                .filter(incident -> (status == null || status.equals(incident.getStatus())) &&
                        (severity == null || severity.equals(incident.getSeverity())))
                .collect(Collectors.toList());

        // 计算总记录数和总页数
        int totalRecords = incidents.size();
        int totalPages = (int) Math.ceil((double) totalRecords / limit);

        // 计算分页起始和结束位置
        int start = page * limit;  // 根据当前页码和每页记录数计算起始索引
        int end = Math.min(start + limit, totalRecords);  // 计算结束索引，防止越界

        // 获取当前页的记录
        List<Incident> pagedIncidents = incidents.subList(start, end);

        // 返回分页后的数据
        return pagedIncidents;
    }

    // 获取事件总数
    public int getTotalRecords(String status, Integer severity) {
        return (int) incidentStorage.values().stream()
                .filter(incident -> (status == null || status.equals(incident.getStatus())) &&
                        (severity == null || severity.equals(incident.getSeverity())))
                .count();
    }
}
