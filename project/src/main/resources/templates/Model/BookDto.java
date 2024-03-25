package com.library.project.Model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookDto {
    @NotBlank(message="isbn is required!!")
    @Column(unique=true)
    private String isbn;
    @NotBlank(message ="Title is required!!")
    private String title;
    @NotBlank(message="Author is required!!")
    private String author;
    @NotNull(message = "Edition cannot be null")
    @Min(value = 1, message = "Edition must be greater than or equal to 1")
    private int edition;
    @NotNull(message = "Price cannot be null")
    @Min(value = 1, message = "Price must be greater than or equal to 1")
    private int price;
    @NotNull(message = "quantity cannot be null")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private int quantity;
    private String feedback;
}
