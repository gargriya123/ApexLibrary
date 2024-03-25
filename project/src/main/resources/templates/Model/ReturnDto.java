package com.library.project.Model;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ReturnDto {
    private int userId;
    private String name;
    private int bookId;
    private String title;
    private int issueId;
    @Temporal(TemporalType.DATE)
    private LocalDate issueDate;
    @Temporal(TemporalType.DATE)
    private LocalDate returnDate;
    @NotNull(message = "Actual Date can't be null")
    @Temporal(TemporalType.DATE)
    private LocalDate actualReturnDate = LocalDate.now();
   private int fineValue;
}
