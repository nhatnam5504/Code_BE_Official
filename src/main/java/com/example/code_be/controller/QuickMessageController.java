package com.example.code_be.controller;

import com.example.code_be.entity.*;
import com.example.code_be.service.QuickMessageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/quick")
@RequiredArgsConstructor
public class QuickMessageController {

    private final QuickMessageService quickMessageService;

    @PostMapping
    public String send(@RequestParam String content,
            HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        User partner = (User) session.getAttribute("partner");
        if (user == null || partner == null)
            return "redirect:/login";

        quickMessageService.send(user.getId(), partner.getId(), content);
        redirectAttributes.addFlashAttribute("success", "Đã gửi lời nhắn! 💬");
        return "redirect:/";
    }
}
