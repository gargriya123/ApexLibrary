package com.library.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message="Name field is required!!")
    @Size(min=2,max=20,message="min 2 and max 20 characters are allowed!!")
    private String fullName;
    @Column(unique = true)
    @Email(regexp="^[a-zA-Z0-9+_.-]+@[a-zsA-Z0-9.-]+$",message="Invalid Email!!")
    private String email;
    @NotBlank(message="Address field is required!!")
    private String address;
    @NotBlank(message="Contact field is required!!")
    @Size(max=10,message=" max 10 digits  are allowed!!")
    private String contact;
    @NotBlank(message="Password field is required!!")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})",
            message = "Enter valid password")
    private String password;
    private Boolean joinBatch;

}
