package com.organization.employee_service.service;

import com.organization.employee_service.dto.EmployeeRequestDTO;
import com.organization.employee_service.dto.EmployeeResponseDTO;

import java.util.List;

public interface EmployeeService {

    EmployeeResponseDTO saveEmployee(EmployeeRequestDTO dto);

    List<EmployeeResponseDTO> getAllEmployees();

    EmployeeResponseDTO getEmployeeById(Long id);

    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto);

    void deleteEmployee(Long id);
}