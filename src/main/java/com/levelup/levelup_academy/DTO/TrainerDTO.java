package com.levelup.levelup_academy.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
@AllArgsConstructor
public class TrainerDTO {
    @NotEmpty(message = "Username can not be empty")
    @Size(min = 3,max = 200,message = "Username length must be between 3 and 30 characters")

    private String username;
    @NotEmpty(message = "Password can not be empty")
    @Size(min = 8,max = 200,message = "Password length must be more than 8 character")
    @Column(columnDefinition = "varchar(220) not null")
    private String password;
    @Email
    @Column(columnDefinition = "varchar(200) not null unique")
    private String email;
    @NotEmpty(message = "firstName can not be empty")
    @Column(columnDefinition = "varchar(40) not null")
    private String firstName;
    @NotEmpty(message = "lastName can not be empty")
    @Column(columnDefinition = "varchar(40) not null")
    private String lastName;
    @Column(columnDefinition = "varchar(40) not null")
    @Pattern(regexp = "^(ADMIN|MODERATOR|PLAYER|PRO|PARENTS)$", message = "Role must be ADMIN, MODERATOR, PLAYER, PRO or PARENTS only")
    private String role;

    private MultipartFile cv;
    @NotEmpty(message = "Game can not empty")
    @Column(columnDefinition = "varchar(40) not null")
    @Pattern(regexp = "^(FC25|CALLOFDUTY|OVERWATCH|FATALFURY|FORTNITE|ROCKETLEAUGE)$", message = "Game must be FC25, CALLOFDUTY, OVERWATCH, FATALFURY, FORTNITE or ROCKETLEAUGE only")
    private String game;

    private Boolean isAvailable = false;
}
