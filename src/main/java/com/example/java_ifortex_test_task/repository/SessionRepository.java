package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query(value = """
        SELECT s.id, s.device_type, s.ended_at_utc, s.started_at_utc, u.id, u.first_name, u.middle_name, u.last_name
        FROM sessions s
        JOIN users u ON s.user_id = u.id
        WHERE s.device_type = 2
        ORDER BY s.started_at_utc ASC
        LIMIT 1
        """, nativeQuery = true)
    List<Object[]> getFirstDesktopSessionAsArray();

    @Query(value = """
        SELECT s.id, s.device_type, s.ended_at_utc, s.started_at_utc, u.id, u.first_name, u.middle_name, u.last_name
        FROM sessions s
        JOIN users u ON s.user_id = u.id
        WHERE u.deleted = false AND s.ended_at_utc < '2025-01-01'
        ORDER BY s.started_at_utc DESC
        """, nativeQuery = true)
    List<Object[]> getSessionsFromActiveUsersEndedBefore2025AsArray();
}
