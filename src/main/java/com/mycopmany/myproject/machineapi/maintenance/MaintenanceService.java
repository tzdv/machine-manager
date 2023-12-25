package com.mycopmany.myproject.machineapi.maintenance;

import com.mycopmany.myproject.machineapi.exception.ResourceNotFoundException;
import com.mycopmany.myproject.machineapi.machine.Machine;
import com.mycopmany.myproject.machineapi.machine.MachineRepository;
import com.mycopmany.myproject.machineapi.user.AuthenticatedUser;
import com.mycopmany.myproject.machineapi.user.User;
import com.mycopmany.myproject.machineapi.user.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;
    private final UserRepository userRepository;
    private final MachineRepository machineRepository;


    public List<MaintenanceToGet> getAllMaintenance() {
        return maintenanceRepository.findAll()
                .stream()
                .map(MaintenanceToGet::fromModel)
                .collect(Collectors.toList());
    }

    public List<MaintenanceToGet> getMaintenanceByMachine(Long serialNumber) {
        boolean machineExists = machineRepository.existsBySerialNumber(serialNumber);
        if (!machineExists)
            throw new ResourceNotFoundException("Machine does not exist");
        return maintenanceRepository.findByMachineSerialNumber(serialNumber)
                .stream()
                .map(MaintenanceToGet::fromModel)
                .collect(Collectors.toList());
    }

    public void createMaintenance(MaintenanceToCreate maintenanceToCreate) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(authenticatedUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        Machine machine = machineRepository.findBySerialNumber(maintenanceToCreate.getMachineSerialNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Machine not found"));

        Maintenance maintenance = new Maintenance(
                maintenanceToCreate.getTitle(),
                maintenanceToCreate.getDescription(),
                user,
                machine);
        maintenanceRepository.save(maintenance);
    }

    @Transactional
    public void editMaintenance(Long recordId, MaintenanceToEdit maintenanceToEdit) {
        Maintenance maintenance = maintenanceRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Maintenance record with id: " + recordId + " does not exist"));
        if (StringUtils.isNotBlank(maintenanceToEdit.getTitle())) {
            maintenance.setTitle(maintenanceToEdit.getTitle());
        }
        maintenance.setDescription(maintenanceToEdit.getDescription());
    }

    public void deleteMaintenance(Long recordId) {
        maintenanceRepository.deleteById(recordId);
    }
}
