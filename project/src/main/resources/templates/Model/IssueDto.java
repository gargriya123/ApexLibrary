package com.library.project.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
public class IssueDto {
    private int userId;
    private int bookId;
    @NotNull(message = "Issue Date can't be null")
    @Temporal(TemporalType.DATE)
    private LocalDate issueDate;
    @NotNull(message = "Return Date can't be null")
    @Temporal(TemporalType.DATE)
    private LocalDate returnDate;
}
