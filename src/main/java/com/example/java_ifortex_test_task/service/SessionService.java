package com.example.java_ifortex_test_task.service;

import com.example.java_ifortex_test_task.dto.SessionResponseDTO;
import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionResponseDTO getFirstDesktopSession() {
        try {
            List<Object[]> rows = sessionRepository.getFirstDesktopSessionRaw();
            if (rows.isEmpty()) {
                return null;
            }

            Object[] row = rows.get(0);
            SessionResponseDTO dto = new SessionResponseDTO();
            dto.setId(((Number) row[0]).longValue());
            dto.setUserId(((Number) row[1]).longValue());
            dto.setDeviceType(DeviceType.fromCode(((Number) row[2]).intValue()));
            dto.setStartedAtUtc(convertToLocalDateTime(row[3]));
            dto.setEndedAtUtc(convertToLocalDateTime(row[4]));
            return dto;

        } catch (Exception e) {
            log.error("Failed to get first desktop session: {}", e.getMessage(), e);
            return null;
        }
    }

    public List<SessionResponseDTO> getSessionsFromActiveUsersEndedBefore2025() {
        try {
            List<Object[]> rows = sessionRepository.getSessionsFromActiveUsersEndedBefore2025Raw();
            List<SessionResponseDTO> result = new ArrayList<>();
            for (Object[] row : rows) {
                SessionResponseDTO dto = new SessionResponseDTO();
                dto.setId(((Number) row[0]).longValue());
                dto.setUserId(((Number) row[1]).longValue());
                dto.setDeviceType(DeviceType.fromCode(((Number) row[2]).intValue()));
                dto.setStartedAtUtc(convertToLocalDateTime(row[3]));
                dto.setEndedAtUtc(convertToLocalDateTime(row[4]));
                result.add(dto);
            }
            return result;

        } catch (Exception e) {
            log.error("Failed to get sessions ended before 2025: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private LocalDateTime convertToLocalDateTime(Object value) {
        return value instanceof Timestamp
                ? ((Timestamp) value).toLocalDateTime()
                : null;
    }
}
