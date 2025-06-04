package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
            SELECT u.*
            FROM users u
            LEFT JOIN sessions s ON s.user_id = u.id
            WHERE u.deleted = false
            GROUP BY u.id
            ORDER BY COUNT(s.id) DESC
            LIMIT 1
            """, nativeQuery = true)
    User findUserWithMostSessions();

    @Query(value = """
            SELECT DISTINCT u.*
            FROM users u
            JOIN sessions s ON s.user_id = u.id
            WHERE s.device_type = 1 -- 1 = MOBILE
              AND u.deleted = false
            """, nativeQuery = true)
    List<User> findUsersWithMobileSession();
}