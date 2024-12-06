package com.homework.incident.controller;

import com.homework.incident.model.Incident;
import com.homework.incident.response.ErrorResponse;
import com.homework.incident.response.SuccessResponse;
import com.homework.incident.service.IncidentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IncidentControllerTest {

    @InjectMocks
    private IncidentController incidentController;

    @Mock
    private IncidentService incidentService;

    @Mock
    private WebRequest webRequest;

    public IncidentControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateIncident_Success() {
        // Mock 数据
        Incident incident = new Incident();
        incident.setAccidentId(123L);
        incident.setDescription("Test incident");

        Incident createdIncident = new Incident();
        createdIncident.setAccidentId(123L);
        createdIncident.setDescription("Test incident");

        when(incidentService.existsById(123L)).thenReturn(false);
        when(incidentService.createIncident(incident)).thenReturn(createdIncident);

        ResponseEntity<?> response = incidentController.createIncident(incident, webRequest);

        assertEquals(201, response.getStatusCodeValue());
        SuccessResponse successResponse = (SuccessResponse) response.getBody();
        assertEquals(Long.valueOf(123), successResponse.getAccidentId());
        assertEquals("Incident created successfully.", successResponse.getMessage());

        verify(incidentService, times(1)).existsById(123L);
        verify(incidentService, times(1)).createIncident(incident);
    }


    @Test
    void testCreateIncident_MissingAccidentId() {
        Incident incident = new Incident(); // 不设置 accidentId
        incident.setDescription("Test incident");

        ResponseEntity<?> response = incidentController.createIncident(incident, webRequest);

        assertEquals(400, response.getStatusCodeValue());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("Accident ID is required.", errorResponse.getMessage());

        // 验证没有调用 createIncident 方法
        verify(incidentService, times(0)).createIncident(any());
    }

    @Test
    void testCreateIncident_AccidentIdAlreadyExists() {
        Incident incident = new Incident();
        incident.setAccidentId(123L);
        incident.setDescription("Test incident");

        when(incidentService.existsById(123L)).thenReturn(true); // Mock 数据库中已经存在

        ResponseEntity<?> response = incidentController.createIncident(incident, webRequest);

        assertEquals(409, response.getStatusCodeValue());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Conflict", errorResponse.getError());
        assertEquals("Incident with the given AccidentID already exists.", errorResponse.getMessage());

        verify(incidentService, times(1)).existsById(123);
        verify(incidentService, times(0)).createIncident(any());
    }

}
