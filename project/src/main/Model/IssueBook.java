package com.library.project.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
@Data
@Entity
public class IssueBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="book_id",nullable=false)
    private Book book;

    @NotNull(message = "Issue Date can't be null")
    @PastOrPresent(message = "Issue date must be in the past or present")
    @Temporal(TemporalType.DATE)
    private LocalDate issueDate;

    @NotNull(message = "Return Date can't be null")
    @FutureOrPresent(message = "Return date must be in the future or present")
    @Temporal(TemporalType.DATE)
    private LocalDate returnDate;
}
