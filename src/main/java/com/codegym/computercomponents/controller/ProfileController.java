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
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final IFileUploadService fileUploadService;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        String[] parts = email.split("@");
        String name = parts[0];
        if (name.length() <= 3) {
            name = "***";
        } else {
            name = name.substring(0, 3) + "***";
        }
        return name + "@" + parts[1];
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) return phone;
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 3);
    }

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
            dto.setEmail(maskEmail(user.getEmail()));
            dto.setPhone(maskPhone(user.getPhone()));
            String currentAvatar = user.getUserAvatar() != null ? user.getUserAvatar().getImageUrl() : null;
            dto.setAvatar(currentAvatar);
            model.addAttribute("userDto", dto);
        }
        
        String currentAvatar = user.getUserAvatar() != null ? user.getUserAvatar().getImageUrl() : null;
        model.addAttribute("currentAvatar", currentAvatar);
        model.addAttribute("addresses", user.getAddresses());

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

        // Restore original values if they are masked
        String newEmail = userDto.getEmail();
        if (newEmail != null && newEmail.contains("***")) {
            newEmail = user.getEmail();
            userDto.setEmail(newEmail);
        }
        String newPhone = userDto.getPhone();
        if (newPhone != null && newPhone.contains("****")) {
            newPhone = user.getPhone();
            userDto.setPhone(newPhone);
        }

        // Custom duplicate check
        if (!user.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)) {
            result.rejectValue("email", "error.userDto", "Email đã được sử dụng bởi tài khoản khác!");
        }
        if (!user.getPhone().equals(newPhone) && userRepository.existsByPhone(newPhone)) {
            result.rejectValue("phone", "error.userDto", "Số điện thoại đã được sử dụng bởi tài khoản khác!");
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userDto", result);
            redirectAttributes.addFlashAttribute("userDto", userDto);
            return "redirect:/profile";
        }

        // Process File Upload
        if (avatarFile != null && !avatarFile.isEmpty()) {
            if (avatarFile.getSize() > 10 * 1024 * 1024) {
                redirectAttributes.addFlashAttribute("error", "Ảnh đại diện có dung lượng không được quá 10MB");
                return "redirect:/profile";
            }
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
        user.setEmail(newEmail.trim());
        user.setPhone(newPhone.trim());

        userRepository.save(user);

        // Cập nhật lại Spring Security Context để Navbar update ngay lập tức
        CustomUserDetails updatedUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(user.getUsername());
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUserDetails, currentAuth.getCredentials(), updatedUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        return "redirect:/profile";
    }

    @PostMapping("/address/add")
    public String addAddress(@RequestParam("receiverName") String receiverName,
                             @RequestParam("phone") String phone,
                             @RequestParam("streetAddress") String streetAddress,
                             @RequestParam(value = "isDefault", required = false) boolean isDefault,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        AppUser user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        com.codegym.computercomponents.model.UserAddress newAddress = new com.codegym.computercomponents.model.UserAddress();
        newAddress.setUser(user);
        newAddress.setReceiverName(receiverName);
        newAddress.setPhone(phone);
        newAddress.setStreetAddress(streetAddress);
        
        if (isDefault || user.getAddresses().isEmpty()) {
            for (com.codegym.computercomponents.model.UserAddress addr : user.getAddresses()) {
                addr.setDefault(false);
            }
            newAddress.setDefault(true);
        }
        
        user.getAddresses().add(newAddress);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Thêm địa chỉ thành công!");
        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        AppUser user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không chính xác!");
            return "redirect:/profile";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/profile";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Thay đổi mật khẩu thành công!");
        return "redirect:/profile";
    }
}
