package com.example.code_be.service;

import com.example.code_be.entity.Post;
import com.example.code_be.enums.Visibility;
import com.example.code_be.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Page<Post> findVisiblePosts(Long userId, Pageable pageable) {
        return postRepository.findVisiblePosts(userId, pageable);
    }

    public List<Post> findByOwner(Long ownerId) {
        return postRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public boolean canView(Post post, Long userId) {
        if (post == null)
            return false;

        switch (post.getVisibility()) {
            case BOTH:
                return true;
            case ONLY_ME:
                return post.getOwnerId().equals(userId);
            case ONLY_PARTNER:
                return !post.getOwnerId().equals(userId);
            default:
                return false;
        }
    }

    public List<Post> findOnThisDay(Long userId, int month, int day) {
        return postRepository.findOnThisDay(userId, month, day);
    }
}
