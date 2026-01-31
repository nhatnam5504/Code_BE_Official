package com.example.code_be.controller;

import com.example.code_be.entity.*;
import com.example.code_be.enums.Mood;
import com.example.code_be.service.MoodService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/mood")
@RequiredArgsConstructor
public class MoodController {

    private final MoodService moodService;

    @GetMapping
    public String today(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        List<DailyMood> todayMoods = moodService.getTodayMoods();
        DailyMood myMood = moodService.getUserMoodToday(user.getId()).orElse(null);

        model.addAttribute("moods", Mood.values());
        model.addAttribute("todayMoods", todayMoods);
        model.addAttribute("myMood", myMood);
        model.addAttribute("userId", user.getId());

        return "mood/today";
    }

    @PostMapping
    public String setMood(@RequestParam Mood mood,
            @RequestParam(required = false) String note,
            HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        moodService.setMood(user.getId(), mood, note);
        redirectAttributes.addFlashAttribute("success", "Đã lưu cảm xúc! " + mood.getEmoji());
        return "redirect:/mood";
    }

    @GetMapping("/history")
    public String history(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        List<DailyMood> history = moodService.getUserMoodHistory(user.getId());
        model.addAttribute("history", history);

        return "mood/history";
    }
}
