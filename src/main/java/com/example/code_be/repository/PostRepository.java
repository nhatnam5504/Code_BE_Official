package com.example.code_be.repository;

import com.example.code_be.entity.Post;
import com.example.code_be.enums.Visibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

        Optional<Post> findByOwnerIdAndTitle(Long ownerId, String title);

        Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

        @Query("SELECT p FROM Post p WHERE p.visibility = 'BOTH' OR " +
                        "(p.visibility = 'ONLY_ME' AND p.ownerId = :userId) OR " +
                        "(p.visibility = 'ONLY_PARTNER' AND p.ownerId != :userId) " +
                        "ORDER BY p.createdAt DESC")
        Page<Post> findVisiblePosts(Long userId, Pageable pageable);

        List<Post> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);

        @Query(value = "SELECT * FROM posts p WHERE (p.visibility = 'BOTH' OR " +
                        "(p.visibility = 'ONLY_ME' AND p.owner_id = :userId) OR " +
                        "(p.visibility = 'ONLY_PARTNER' AND p.owner_id != :userId)) " +
                        "AND EXTRACT(MONTH FROM p.occurred_at) = :month AND EXTRACT(DAY FROM p.occurred_at) = :day", nativeQuery = true)
        List<Post> findOnThisDay(Long userId, int month, int day);
}
