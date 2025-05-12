package com.levelup.levelup_academy.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime bookDate;

    @NotEmpty(message = "Status cannot be empty")
    @Pattern(regexp = "^(PENDING|ACTIVE|CANCELLED)$", message = "Booking must be PENDING, ACTIVE or CANCELED only")
    @Column(columnDefinition = "varchar(20) not null")
    private String status = "PENDING";


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "subscription_id", referencedColumnName = "id")
    @JsonIgnore
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    @JsonIgnore
    private Session session;

}
