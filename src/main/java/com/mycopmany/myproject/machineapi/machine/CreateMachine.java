package com.mycopmany.myproject.machineapi.machine;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMachine {
    private Long serialNumber;
    private String model;
    private String category;
    private String location;
}
