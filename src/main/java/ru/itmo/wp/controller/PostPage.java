package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.security.Guest;
import ru.itmo.wp.service.PostService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class PostPage extends Page {
    private final PostService postService;

    public PostPage(PostService postService) {
        this.postService = postService;
    }

    @Guest
    @GetMapping("post/{id:\\d{1,8}}")
    public String postPageGet(@PathVariable long id, Model model, HttpSession httpSession) {
        if (postService.findById(id) == null) {
            putMessage(httpSession, "Post doesn't exist!");
            return "redirect:/";
        }
        model.addAttribute("commentForm", new Comment());
        model.addAttribute("postGet", postService.findById(id));
        model.addAttribute("isLogged", getUser(httpSession) != null);
        return "PostPage";
    }

    @PostMapping("post/{id:\\d{1,8}}")
    public String postPagePost(@PathVariable long id, @Valid @ModelAttribute("commentForm") Comment comment,
                               BindingResult bindingResult, HttpSession httpSession, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("commentForm", comment);
            model.addAttribute("postGet", postService.findById(id));
            model.addAttribute("isLogged", getUser(httpSession) != null);
            return "PostPage";
        }
        if (postService.findById(id) == null) {
            putMessage(httpSession, "Post doesn't exist!");
            return "redirect:index/";
        }
        postService.writeComment(getUser(httpSession), postService.findById(id), comment);
        putMessage(httpSession, "You published new comment!");
        return "redirect:/post/" + id;
    }
}