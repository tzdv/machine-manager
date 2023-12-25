package com.mycopmany.myproject.machineapi.maintenance;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MaintenanceToCreate {
    @NotBlank
    private String title;
    private String description;
    private Long machineSerialNumber;

}
