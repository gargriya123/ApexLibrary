package com.library.project.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
@Data
@Entity
public class ReturnBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name="issue_id")
    private IssueBook issue;
    @NotNull(message = "Actual Date can't be null")
    @Temporal(TemporalType.DATE)
    private LocalDate actualReturnDate = LocalDate.now();
    private boolean fine;
    @NotNull(message = "fine cannot be null")
    @Min(value = 0, message = "fine must be greater than or equal to 0")
    private int fineValue;
    private boolean finePaid;
}
