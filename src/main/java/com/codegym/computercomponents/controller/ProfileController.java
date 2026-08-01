package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.dto.UserProfileDto;
import com.codegym.computercomponents.model.AppUser;
import com.codegym.computercomponents.repository.UserRepository;
import com.codegym.computercomponents.service.IFileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.codegym.computercomponents.security.CustomUserDetails;
import com.codegym.computercomponents.model.UserAvatar;
import com.codegym.computercomponents.service.impl.CustomUserDetailsService;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final IFileUploadService fileUploadService;
    private final CustomUserDetailsService userDetailsService;

    @GetMapping
    public String viewProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        
        AppUser user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        if (!model.containsAttribute("userDto")) {
            UserProfileDto dto = new UserProfileDto();
            dto.setUsername(user.getUsername());
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setAddress(user.getAddress());
            String currentAvatar = user.getUserAvatar() != null ? user.getUserAvatar().getImageUrl() : null;
            dto.setAvatar(currentAvatar);
            model.addAttribute("userDto", dto);
        }
        
        String currentAvatar = user.getUserAvatar() != null ? user.getUserAvatar().getImageUrl() : null;
        model.addAttribute("currentAvatar", currentAvatar);

        return "user/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute("userDto") UserProfileDto userDto,
                                BindingResult result,
                                @RequestParam("avatarFile") MultipartFile avatarFile,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
                                    
        if (userDetails == null) {
            return "redirect:/login";
        }

        AppUser user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        // Custom duplicate check
        if (!user.getEmail().equals(userDto.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            result.rejectValue("email", "error.userDto", "Email đã được sử dụng bởi tài khoản khác!");
        }
        if (!user.getPhone().equals(userDto.getPhone()) && userRepository.existsByPhone(userDto.getPhone())) {
            result.rejectValue("phone", "error.userDto", "Số điện thoại đã được sử dụng bởi tài khoản khác!");
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userDto", result);
            redirectAttributes.addFlashAttribute("userDto", userDto);
            return "redirect:/profile";
        }

        // Process File Upload
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String avatarUrl = fileUploadService.storeFile(avatarFile);
                if (user.getUserAvatar() == null) {
                    UserAvatar newAvatar = new UserAvatar();
                    newAvatar.setImageUrl(avatarUrl);
                    user.setUserAvatar(newAvatar);
                } else {
                    user.getUserAvatar().setImageUrl(avatarUrl);
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Lỗi tải ảnh: " + e.getMessage());
                return "redirect:/profile";
            }
        }

        user.setFullName(userDto.getFullName().trim());
        user.setEmail(userDto.getEmail().trim());
        user.setPhone(userDto.getPhone().trim());
        user.setAddress(userDto.getAddress().trim());

        userRepository.save(user);

        // Cập nhật lại Spring Security Context để Navbar update ngay lập tức
        CustomUserDetails updatedUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(user.getUsername());
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUserDetails, currentAuth.getCredentials(), updatedUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        return "redirect:/profile";
    }
}
