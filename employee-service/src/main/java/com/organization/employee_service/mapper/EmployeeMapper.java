package com.organization.employee_service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.organization.employee_service.dto.EmployeeRequestDTO;
import com.organization.employee_service.dto.EmployeeResponseDTO;
import com.organization.employee_service.entity.Employee;

public class EmployeeMapper {

    public static Employee toEntity(EmployeeRequestDTO dto) {

        Employee employee = new Employee();

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setSalary(dto.getSalary());
        employee.setDepartmentId(dto.getDepartmentId());

        return employee;
    }

    public static EmployeeResponseDTO toResponseDTO(Employee employee) {

        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getSalary(),
                employee.getDepartmentId(),
                null
        );
    }

    public static List<EmployeeResponseDTO> toResponseDTOList(List<Employee> employees) {

        List<EmployeeResponseDTO> response = new ArrayList<>();

        for (Employee employee : employees) {
            response.add(toResponseDTO(employee));
        }

        return response;
    }
}