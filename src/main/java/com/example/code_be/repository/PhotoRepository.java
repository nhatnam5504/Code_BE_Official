package com.example.code_be.repository;

import com.example.code_be.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Optional<Photo> findByUploaderIdAndCaption(Long uploaderId, String caption);

    Page<Photo> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Photo> findByAlbumOrderByTakenAtDesc(String album);

    @Query("SELECT DISTINCT p.album FROM Photo p WHERE p.album IS NOT NULL")
    List<String> findAllAlbums();

    @Query(value = "SELECT * FROM photos p WHERE EXTRACT(MONTH FROM p.taken_at) = :month AND EXTRACT(DAY FROM p.taken_at) = :day", nativeQuery = true)
    List<Photo> findByMonthAndDay(int month, int day);
}
