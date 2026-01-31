package com.example.code_be.controller;

import com.example.code_be.entity.*;
import com.example.code_be.service.PhotoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String album,
            HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        List<String> albums = photoService.findAllAlbums();
        model.addAttribute("albums", albums);
        model.addAttribute("selectedAlbum", album);

        if (album != null && !album.isEmpty()) {
            model.addAttribute("photos", photoService.findByAlbum(album));
        } else {
            Page<Photo> photos = photoService.findAll(PageRequest.of(page, 20));
            model.addAttribute("photos", photos.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", photos.getTotalPages());
        }

        return "photos/list";
    }

    @GetMapping("/upload")
    public String uploadForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        List<String> albums = photoService.findAllAlbums();
        model.addAttribute("albums", albums);
        return "photos/upload";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String caption,
            @RequestParam(required = false) String album,
            @RequestParam(required = false) String newAlbum,
            HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        try {
            String url = photoService.uploadFile(file);

            String finalAlbum = (newAlbum != null && !newAlbum.isEmpty()) ? newAlbum : album;

            Photo photo = Photo.builder()
                    .uploaderId(user.getId())
                    .url(url)
                    .caption(caption)
                    .album(finalAlbum)
                    .takenAt(LocalDateTime.now())
                    .build();

            photoService.save(photo);
            redirectAttributes.addFlashAttribute("success", "Upload thành công! 📸");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Upload thất bại: " + e.getMessage());
        }

        return "redirect:/photos";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Photo photo = photoService.findById(id);
        if (photo == null)
            return "redirect:/photos";

        model.addAttribute("photo", photo);
        return "photos/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        photoService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Đã xóa ảnh!");
        return "redirect:/photos";
    }
}
