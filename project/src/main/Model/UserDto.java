package com.library.project.Model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    @Size(min=2,max=20,message="min 2 and max 20 characters are allowed!!")
    @NotBlank(message="Name is required")
    private String fullName;
    @Column(unique = true)
    @Email(regexp="^[a-zA-Z0-9+_.-]+@[a-zsA-Z0-9.-]+$",message="Invalid Email!!")
    private String email;
    @NotBlank(message="address is required")
    private String address;
    @NotBlank(message="contact is required")
    @Size(max=10,message=" max 10 digits  are allowed!!")
    private String contact;
    @NotBlank(message="password is required")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})",
            message = "Enter valid password")
    private String password;
    private Boolean joinBatch;
}
