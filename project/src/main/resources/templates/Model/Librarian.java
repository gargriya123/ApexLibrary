package com.library.project.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
@Entity
public class Librarian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message="Name field is required!!")
    @Size(min=2,max=20,message="min 2 and max 20 characters are allowed!!")
    private String fullName;
    @Email(regexp="^[a-zA-Z0-9+_.-]+@[a-zsA-Z0-9.-]+$",message="Invalid Email!!")
    @Column(unique=true)
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

}
