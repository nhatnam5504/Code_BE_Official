package com.example.code_be.controller;

import com.example.code_be.entity.*;
import com.example.code_be.service.MilestoneService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/milestones")
@RequiredArgsConstructor
public class MilestoneController {

    private final MilestoneService milestoneService;

    @GetMapping
    public String timeline(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        List<Milestone> milestones = milestoneService.findAllAsc();
        model.addAttribute("milestones", milestones);
        return "milestones/timeline";
    }

    @GetMapping("/new")
    public String newForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        model.addAttribute("milestone", new Milestone());
        return "milestones/form";
    }

    @PostMapping("/new")
    public String create(@RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam String date,
            @RequestParam(required = false) String icon,
            HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Milestone milestone = Milestone.builder()
                .title(title)
                .description(description)
                .date(LocalDate.parse(date))
                .icon(icon != null && !icon.isEmpty() ? icon : "💕")
                .build();

        milestoneService.save(milestone);
        redirectAttributes.addFlashAttribute("success", "Đã thêm mốc kỉ niệm! 🎉");
        return "redirect:/milestones";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Milestone milestone = milestoneService.findById(id);
        if (milestone == null)
            return "redirect:/milestones";

        model.addAttribute("milestone", milestone);
        return "milestones/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam String date,
            @RequestParam(required = false) String icon,
            HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Milestone milestone = milestoneService.findById(id);
        if (milestone == null)
            return "redirect:/milestones";

        milestone.setTitle(title);
        milestone.setDescription(description);
        milestone.setDate(LocalDate.parse(date));
        milestone.setIcon(icon != null && !icon.isEmpty() ? icon : "💕");
        milestoneService.save(milestone);

        redirectAttributes.addFlashAttribute("success", "Đã cập nhật!");
        return "redirect:/milestones";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        milestoneService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Đã xóa mốc kỉ niệm!");
        return "redirect:/milestones";
    }
}
