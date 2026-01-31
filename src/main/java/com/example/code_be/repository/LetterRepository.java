package com.example.code_be.repository;

import com.example.code_be.entity.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LetterRepository extends JpaRepository<Letter, Long> {

    Optional<Letter> findByFromUserIdAndContentStartingWith(Long fromUserId, String contentPrefix);

    List<Letter> findByToUserIdOrderByCreatedAtDesc(Long toUserId);

    List<Letter> findByFromUserIdOrderByCreatedAtDesc(Long fromUserId);

    List<Letter> findByToUserIdAndIsOpenedFalseOrderByCreatedAtDesc(Long toUserId);
}
