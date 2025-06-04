package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query(value = """
        SELECT s.id, s.user_id, s.device_type, s.started_at_utc, s.ended_at_utc
        FROM sessions s
        WHERE s.device_type = 2
        ORDER BY s.started_at_utc ASC
        LIMIT 1
        """, nativeQuery = true)
    List<Object[]> getFirstDesktopSessionRaw();

    @Query(value = """
        SELECT s.id, s.user_id, s.device_type, s.started_at_utc, s.ended_at_utc
        FROM sessions s
        JOIN users u ON s.user_id = u.id
        WHERE u.deleted = false AND s.ended_at_utc < '2025-01-01'
        ORDER BY s.started_at_utc DESC
        """, nativeQuery = true)
    List<Object[]> getSessionsFromActiveUsersEndedBefore2025Raw();
}
