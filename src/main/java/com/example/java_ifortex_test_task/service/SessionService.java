package com.example.java_ifortex_test_task.service;

import com.example.java_ifortex_test_task.dto.SessionResponseDTO;
import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    // Return single DTO or null
    public SessionResponseDTO getFirstDesktopSession() {
        try {
            List<Object[]> rows = sessionRepository.getFirstDesktopSessionAsArray();
            if (rows == null || rows.isEmpty()) {
                return null;
            }
            return convertToDto(rows.get(0));
        } catch (Exception e) {
            System.err.println("Failed to get first desktop session: " + e.getMessage());
            return null;
        }
    }

    public List<SessionResponseDTO> getSessionsFromActiveUsersEndedBefore2025() {
        try {
            List<Object[]> rows = sessionRepository.getSessionsFromActiveUsersEndedBefore2025AsArray();
            return rows.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Failed to get sessions from active users ended before 2025: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private SessionResponseDTO convertToDto(Object[] row) {
        SessionResponseDTO dto = new SessionResponseDTO();

        Long id = ((Number) row[0]).longValue();
        int deviceTypeCode = ((Number) row[1]).intValue();
        LocalDateTime endedAt = ((java.sql.Timestamp) row[2]).toLocalDateTime();
        LocalDateTime startedAt = ((java.sql.Timestamp) row[3]).toLocalDateTime();
        Long userId = ((Number) row[4]).longValue();
        String firstName = (String) row[5];
        String middleName = (String) row[6];
        String lastName = (String) row[7];

        dto.setId(id);
        dto.setDeviceType(DeviceType.fromCode(deviceTypeCode));
        dto.setEndedAtUtc(endedAt);
        dto.setStartedAtUtc(startedAt);
        dto.setUserId(userId);

        StringBuilder fullNameBuilder = new StringBuilder();
        if (firstName != null) fullNameBuilder.append(firstName);
        if (middleName != null) fullNameBuilder.append(" ").append(middleName);
        if (lastName != null) fullNameBuilder.append(" ").append(lastName);
        dto.setUserFullName(fullNameBuilder.toString().trim());

        return dto;
    }
}
