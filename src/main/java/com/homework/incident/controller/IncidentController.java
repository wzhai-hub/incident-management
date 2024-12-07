package com.homework.incident.controller;

import com.homework.incident.model.Incident;
import com.homework.incident.response.ErrorResponse;
import com.homework.incident.response.PaginatedIncidents;
import com.homework.incident.response.PaginationResponse;
import com.homework.incident.response.SuccessResponse;
import com.homework.incident.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private static final Logger logger = LoggerFactory.getLogger(IncidentController.class);
    @Autowired
    private IncidentService incidentService;

    // 1. 创建事件
    @PostMapping
    public ResponseEntity<?> createIncident(@RequestBody @Valid Incident incident, WebRequest request) {
        try {

            String requestUrl = request.getDescription(false); // 获取请求的描述信息
            logger.info("Received request: URL = {}", requestUrl);
            // 校验 accidentId 是否有效
            if (incident.getAccidentId() == null) {
                logger.warn("Bad Request: Accident ID is missing.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Bad Request", "Accident ID is required."));
            }

            // 校验 accidentId 是否已经存在
            if (incidentService.existsById(incident.getAccidentId())) {
                logger.warn("Conflict: Incident with AccidentID {} already exists.", incident.getAccidentId());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse("Conflict", "Incident with the given AccidentID already exists."));
            }
            // 创建事件
            Incident createdIncident = incidentService.createIncident(incident);
            logger.info("Incident created successfully with AccidentID = {}", createdIncident.getAccidentId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SuccessResponse(createdIncident.getAccidentId(), "Incident created successfully."));
        } catch (IllegalArgumentException e) {
            logger.error("Error occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Bad Request", "Missing required fields or invalid data."));
        }
    }

    // 2. 删除事件
    @DeleteMapping("/{accidentId}")
    public ResponseEntity<?> deleteIncident(@PathVariable Long accidentId) {
        logger.info("Received delete request for Accident ID: {}", accidentId);
        try {
            incidentService.deleteIncident(accidentId);
            logger.info("Incident with Accident ID: {} deleted successfully.", accidentId);
            return ResponseEntity.ok("Incident deleted successfully.");
        } catch (IllegalArgumentException e) {
            // 捕获非法参数异常
            logger.warn("Failed to delete incident with Accident ID: {}. Reason: {}", accidentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常
            logger.error("Unexpected error occurred while deleting Accident ID: {}. Error: {}", accidentId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // 3. 更新事件
    @PutMapping("/{accidentId}")
    public ResponseEntity<?> updateIncident(
            @PathVariable Long accidentId,  // 从路径参数获取 accidentId
            @RequestBody Incident updatedIncident) {  // 请求体只包含更新信息（不包含 accidentId）
        try {
            // 记录收到的请求信息
            logger.info("Received update request: Accident ID = {}, Updated Incident = {}", accidentId, updatedIncident);

            if (!incidentService.existsById(accidentId)) {
                logger.warn("Incident with Accident ID = {} not found. Cannot update.", accidentId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Not Found", "Incident with the given AccidentID not found."));
            }

            // 记录更新操作前的信息
            logger.info("Proceeding to update incident with Accident ID = {}", accidentId);

            // 调用服务层的更新方法，完成更新逻辑
            incidentService.updateIncident(accidentId, updatedIncident);

            // 记录更新成功的信息
            logger.info("Incident with Accident ID = {} updated successfully.", accidentId);

            return ResponseEntity.ok(new SuccessResponse(accidentId, "Incident updated successfully."));
        } catch (IllegalArgumentException e) {
            logger.error("Error occurred while updating Incident with Accident ID = {}. Error: {}", accidentId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Bad Request", "Invalid data provided for updating the incident."));
        }
    }


    // 4. 获取所有事件
    // 获取所有事件，支持分页和筛选
    @GetMapping
    public ResponseEntity<?> getAllIncidents(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer severity,
            @RequestParam(defaultValue = "1") int page,  // page从1开始
            @RequestParam(defaultValue = "10") int limit) {

        logger.info("Received request to get all incidents with parameters: status={}, severity={}, page={}, limit={}",
                status, severity, page, limit);

        // 校验分页参数，防止page为负数
        if (page < 1) {
            logger.error("Invalid page number: {}. Page number must be >= 1", page);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Bad Request", "Page number must be >= 1"));
        }

        // 获取总记录数
        int totalRecords = incidentService.getTotalRecords(status, severity);
        if (totalRecords == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Not Found", "No incidents found."));
        }

        // 计算分页的起始和结束索引
        int fromIndex = (page - 1) * limit;
        int toIndex = page * limit;

        // 确保 toIndex 不超过总记录数
        if (toIndex > totalRecords) {
            toIndex = totalRecords;
        }

        logger.info("Paging: fromIndex={}, toIndex={}", fromIndex, toIndex);

        List<Incident> incidents = incidentService.getAllIncidents(status, severity, fromIndex, toIndex);
        int totalPages = (int) Math.ceil((double) totalRecords / limit);

        logger.info("Fetched {} incidents out of {} total records. Total pages: {}", incidents.size(), totalRecords, totalPages);

        // 构建分页响应
        PaginationResponse paginationResponse = new PaginationResponse(page, totalPages, totalRecords);
        PaginatedIncidents paginatedIncidents = new PaginatedIncidents(incidents, paginationResponse);

        logger.info("Returning paginated incidents response.");
        return ResponseEntity.ok(paginatedIncidents);
    }


}
