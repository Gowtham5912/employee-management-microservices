package com.organization.employee_service.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.organization.employee_service.entity.Employee;
import com.organization.employee_service.exception.EmployeeNotFoundException;
import com.organization.employee_service.repository.EmployeeRepository;
import com.organization.employee_service.service.impl.EmployeeServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;
    
    @Test
    void shouldSaveEmployee() {

        Employee employee = new Employee();
        employee.setFirstName("Gowtham");
        employee.setLastName("DV");
        employee.setEmail("gowtham@example.com");
        employee.setSalary(50000.0);

        when(employeeRepository.save(employee))
                .thenReturn(employee);

        Employee savedEmployee = employeeService.saveEmployee(employee);

        assertEquals("Gowtham", savedEmployee.getFirstName());

        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldReturnEmployeeById() {

        // Arrange
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Gowtham");
        employee.setLastName("DV");
        employee.setEmail("gowtham@example.com");
        employee.setSalary(50000.0);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        // Act
        Employee result = employeeService.getEmployeeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Gowtham", result.getFirstName());

        verify(employeeRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {

        // Arrange
        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        EmployeeNotFoundException exception =
                assertThrows(EmployeeNotFoundException.class,
                        () -> employeeService.getEmployeeById(1L));

        assertEquals("Employee not found with id: 1",
                exception.getMessage());

        verify(employeeRepository).findById(1L);
    }

    @Test
    void shouldUpdateEmployee() {

        // Arrange
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        existingEmployee.setFirstName("Gowtham");
        existingEmployee.setLastName("DV");
        existingEmployee.setEmail("gowtham@example.com");
        existingEmployee.setSalary(50000.0);

        Employee updatedEmployee = new Employee();
        updatedEmployee.setFirstName("Gowtham");
        updatedEmployee.setLastName("D V");
        updatedEmployee.setEmail("gowtham.dvgowda@example.com");
        updatedEmployee.setSalary(80000.0);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(existingEmployee));

        when(employeeRepository.save(existingEmployee))
                .thenReturn(existingEmployee);

        // Act
        Employee result = employeeService.updateEmployee(1L, updatedEmployee);

        // Assert
        assertEquals("D V", result.getLastName());
        assertEquals("gowtham.dvgowda@example.com", result.getEmail());
        assertEquals(80000.0, result.getSalary());

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(existingEmployee);
    }

    @Test
    void shouldDeleteEmployee() {

        // Arrange
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Gowtham");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        doNothing().when(employeeRepository).delete(employee);

        // Act
        employeeService.deleteEmployee(1L);

        // Assert
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).delete(employee);
    }

}