package com.organization.employee_service.controller;

import com.organization.employee_service.dto.EmployeeRequestDTO;
import com.organization.employee_service.dto.EmployeeResponseDTO;
import com.organization.employee_service.entity.Employee;
import com.organization.employee_service.mapper.EmployeeMapper;
import com.organization.employee_service.service.EmployeeService;
import com.organization.employee_service.dto.EmployeeRequestDTO;
import com.organization.employee_service.dto.EmployeeResponseDTO;
import com.organization.employee_service.mapper.EmployeeMapper;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public EmployeeResponseDTO saveEmployee(
        @Valid @RequestBody EmployeeRequestDTO request) {

    Employee employee = EmployeeMapper.toEntity(request);

    Employee savedEmployee = employeeService.saveEmployee(employee);

    return EmployeeMapper.toResponseDTO(savedEmployee);
    }

    @GetMapping
    public List<EmployeeResponseDTO> getAllEmployees() {

        List<Employee> employees = employeeService.getAllEmployees();

        return EmployeeMapper.toResponseDTOList(employees);
    }

    @GetMapping("/{id}")
    public EmployeeResponseDTO getEmployeeById(@PathVariable Long id) {

        Employee employee = employeeService.getEmployeeById(id);

        return EmployeeMapper.toResponseDTO(employee);
    }

    @PutMapping("/{id}")
    public EmployeeResponseDTO updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDTO request) {

        Employee employee = EmployeeMapper.toEntity(request);

        Employee updatedEmployee = employeeService.updateEmployee(id, employee);

        return EmployeeMapper.toResponseDTO(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return "Employee deleted successfully";
}
}