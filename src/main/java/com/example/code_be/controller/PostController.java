package com.example.code_be.controller;

import com.example.code_be.entity.*;
import com.example.code_be.enums.*;
import com.example.code_be.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Page<Post> posts = postService.findVisiblePosts(user.getId(), PageRequest.of(page, 10));
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("userId", user.getId());

        return "posts/list";
    }

    @GetMapping("/new")
    public String newForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        model.addAttribute("moods", Mood.values());
        model.addAttribute("visibilities", Visibility.values());
        model.addAttribute("post", new Post());
        return "posts/form";
    }

    @PostMapping("/new")
    public String create(@RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) Mood mood,
            @RequestParam Visibility visibility,
            @RequestParam(required = false) String occurredAt,
            HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Post post = Post.builder()
                .ownerId(user.getId())
                .title(title)
                .content(content)
                .mood(mood)
                .visibility(visibility)
                .occurredAt(occurredAt != null && !occurredAt.isEmpty() ? LocalDate.parse(occurredAt) : LocalDate.now())
                .build();

        postService.save(post);
        redirectAttributes.addFlashAttribute("success", "Đã lưu bài viết! 📝");
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Post post = postService.findById(id);
        if (post == null || !postService.canView(post, user.getId())) {
            return "redirect:/posts";
        }

        model.addAttribute("post", post);
        model.addAttribute("userId", user.getId());
        return "posts/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Post post = postService.findById(id);
        if (post == null || !post.getOwnerId().equals(user.getId())) {
            return "redirect:/posts";
        }

        model.addAttribute("post", post);
        model.addAttribute("moods", Mood.values());
        model.addAttribute("visibilities", Visibility.values());
        return "posts/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) Mood mood,
            @RequestParam Visibility visibility,
            HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Post post = postService.findById(id);
        if (post == null || !post.getOwnerId().equals(user.getId())) {
            return "redirect:/posts";
        }

        post.setTitle(title);
        post.setContent(content);
        post.setMood(mood);
        post.setVisibility(visibility);
        postService.save(post);

        redirectAttributes.addFlashAttribute("success", "Đã cập nhật!");
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Post post = postService.findById(id);
        if (post != null && post.getOwnerId().equals(user.getId())) {
            postService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa bài viết!");
        }
        return "redirect:/posts";
    }
}
