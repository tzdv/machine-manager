package com.mycopmany.myproject.machineapi.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserToLogin {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
