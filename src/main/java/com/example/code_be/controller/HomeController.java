package com.example.code_be.controller;

import com.example.code_be.entity.*;
import com.example.code_be.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemoryService memoryService;
    private final PostService postService;
    private final MoodService moodService;
    private final QuickMessageService quickMessageService;
    private final MilestoneService milestoneService;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        User partner = (User) session.getAttribute("partner");

        // On This Day memories
        LocalDate today = LocalDate.now();
        List<MemoryService.OnThisDayMemory> memories = memoryService.getOnThisDay(user.getId(), today);
        model.addAttribute("memories", memories);

        // Days together
        if (user.getCoupleStartDate() != null) {
            long daysTogether = ChronoUnit.DAYS.between(user.getCoupleStartDate(), today);
            model.addAttribute("daysTogether", daysTogether);
            model.addAttribute("coupleStartDate", user.getCoupleStartDate());
        }

        // Today's moods
        List<DailyMood> todayMoods = moodService.getTodayMoods();
        model.addAttribute("todayMoods", todayMoods);

        // Quick messages for this user
        List<QuickMessage> quickMessages = quickMessageService.getActiveMessages(user.getId());
        model.addAttribute("quickMessages", quickMessages);

        // Recent milestones
        List<Milestone> milestones = milestoneService.findAllDesc();
        model.addAttribute("milestones", milestones.size() > 5 ? milestones.subList(0, 5) : milestones);

        model.addAttribute("user", user);
        model.addAttribute("partner", partner);
        model.addAttribute("today", today);

        return "home";
    }
}
