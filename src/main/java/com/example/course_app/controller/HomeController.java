package com.example.course_app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String indexPage() {
        return "home";
    }

    @GetMapping(value = "/courses-page")
    public String coursesPage() {
        return "courses-page";
    }

    @GetMapping(value = "/403-page")
    public String accessDenied() {
        return "403";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/sign-in-page")
    public String signInPage() {
        return "signin";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/sign-up-page")
    public String signUpPage() {
        return "signup";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/profile")
    public String profilePage() {
        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/update-password-page")
    public String updatePasswordPage() {
        return "update-password";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/user-courses-page")
    public String userCoursesPage() {
        return "user-courses-page";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/update-profile-page")
    public String updateProfilePage() {
        return "update-profile";
    }

}