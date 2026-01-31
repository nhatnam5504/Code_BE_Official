package com.example.code_be.controller;

import com.example.code_be.entity.*;
import com.example.code_be.enums.OpenType;
import com.example.code_be.service.LetterService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/letters")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;

    @GetMapping
    public String inbox(@RequestParam(defaultValue = "inbox") String box, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        List<Letter> letters;
        if ("sent".equals(box)) {
            letters = letterService.findSent(user.getId());
        } else {
            letters = letterService.findInbox(user.getId());
        }

        model.addAttribute("letters", letters);
        model.addAttribute("box", box);
        model.addAttribute("userId", user.getId());
        return "letters/inbox";
    }

    @GetMapping("/compose")
    public String composeForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        User partner = (User) session.getAttribute("partner");

        model.addAttribute("openTypes", OpenType.values());
        model.addAttribute("partner", partner);
        return "letters/compose";
    }

    @PostMapping("/compose")
    public String compose(@RequestParam String content,
            @RequestParam OpenType openType,
            @RequestParam(required = false) String openAt,
            HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        User partner = (User) session.getAttribute("partner");
        if (user == null || partner == null)
            return "redirect:/login";

        Letter letter = Letter.builder()
                .fromUserId(user.getId())
                .toUserId(partner.getId())
                .content(content)
                .openType(openType)
                .openAt(openAt != null && !openAt.isEmpty() ? LocalDateTime.parse(openAt + "T00:00:00") : null)
                .senderConfirmed(openType == OpenType.BOTH_CONFIRM)
                .build();

        letterService.save(letter);
        redirectAttributes.addFlashAttribute("success", "Đã gửi thư! 💌");
        return "redirect:/letters?box=sent";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Letter letter = letterService.findById(id);
        if (letter == null)
            return "redirect:/letters";

        // Check if user can access this letter
        if (!letter.getFromUserId().equals(user.getId()) && !letter.getToUserId().equals(user.getId())) {
            return "redirect:/letters";
        }

        boolean canOpen = letterService.canOpen(letter, user.getId());
        boolean isRecipient = letter.getToUserId().equals(user.getId());
        boolean isSender = letter.getFromUserId().equals(user.getId());

        // If can open and hasn't been opened yet, mark as opened
        if (canOpen && !letter.getIsOpened() && isRecipient) {
            letter = letterService.openLetter(id);
        }

        model.addAttribute("letter", letter);
        model.addAttribute("canOpen", canOpen);
        model.addAttribute("isRecipient", isRecipient);
        model.addAttribute("isSender", isSender);
        model.addAttribute("userId", user.getId());

        return "letters/detail";
    }

    @PostMapping("/{id}/confirm")
    public String confirmOpen(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        letterService.confirmOpen(id, user.getId());
        redirectAttributes.addFlashAttribute("success", "Đã xác nhận! Chờ người kia xác nhận nữa nhé 💕");
        return "redirect:/letters/" + id;
    }
}
