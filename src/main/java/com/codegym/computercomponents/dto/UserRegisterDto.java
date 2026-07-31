package com.codegym.computercomponents.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "Tên đăng nhập phải từ 4 đến 12 ký tự và không chứa ký tự đặc biệt")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 30, message = "Mật khẩu phải từ 6 đến 30 ký tự")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$", message = "Mật khẩu nên chứa ít nhất 1 chữ hoa và 1 ký tự đặc biệt")
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(086|096|097|098|032|033|034|035|036|037|038|039|089|090|093|070|079|077|076|078|088|091|094|083|084|085|081|082)[0-9]{7}$", 
             message = "Số điện thoại không hợp lệ (Phải là 10 số thuộc mạng Viettel, Mobi hoặc Vina)")
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;
}
