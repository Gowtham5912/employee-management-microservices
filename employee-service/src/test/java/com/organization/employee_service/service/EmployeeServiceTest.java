package com.organization.employee_service.service;

import com.organization.employee_service.dto.DepartmentDTO;
import com.organization.employee_service.dto.EmployeeRequestDTO;
import com.organization.employee_service.dto.EmployeeResponseDTO;
import com.organization.employee_service.entity.Employee;
import com.organization.employee_service.exception.EmployeeNotFoundException;
import com.organization.employee_service.repository.EmployeeRepository;
import com.organization.employee_service.service.impl.EmployeeServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void shouldSaveEmployee() {

        EmployeeRequestDTO request = new EmployeeRequestDTO();
        request.setFirstName("Gowtham");
        request.setLastName("DV");
        request.setEmail("gowtham@example.com");
        request.setSalary(50000.0);
        request.setDepartmentId(1L);

        Employee savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setFirstName("Gowtham");
        savedEmployee.setLastName("DV");
        savedEmployee.setEmail("gowtham@example.com");
        savedEmployee.setSalary(50000.0);
        savedEmployee.setDepartmentId(1L);

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(1L);
        departmentDTO.setDepartmentName("Engineering");
        departmentDTO.setDepartmentCode("ENG001");
        departmentDTO.setDepartmentDescription("Handles software development");

        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);
        when(restTemplate.getForObject(
                "http://localhost:8083/departments/1",
                DepartmentDTO.class))
                .thenReturn(departmentDTO);

        EmployeeResponseDTO response = employeeService.saveEmployee(request);

        assertNotNull(response);
        assertEquals("Gowtham", response.getFirstName());
        assertEquals(1L, response.getDepartmentId());
        assertNotNull(response.getDepartment());
        assertEquals("Engineering", response.getDepartment().getDepartmentName());

        verify(employeeRepository).save(any(Employee.class));
        verify(restTemplate).getForObject("http://localhost:8083/departments/1", DepartmentDTO.class);
    }

    @Test
    void shouldReturnEmployeeById() {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Gowtham");
        employee.setLastName("DV");
        employee.setEmail("gowtham@example.com");
        employee.setSalary(50000.0);
        employee.setDepartmentId(1L);

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(1L);
        departmentDTO.setDepartmentName("Engineering");
        departmentDTO.setDepartmentCode("ENG001");
        departmentDTO.setDepartmentDescription("Handles software development");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(restTemplate.getForObject(
                "http://localhost:8083/departments/1",
                DepartmentDTO.class))
                .thenReturn(departmentDTO);

        EmployeeResponseDTO result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Gowtham", result.getFirstName());
        assertEquals(1L, result.getDepartmentId());
        assertNotNull(result.getDepartment());
        assertEquals("Engineering", result.getDepartment().getDepartmentName());

        verify(employeeRepository).findById(1L);
        verify(restTemplate).getForObject("http://localhost:8083/departments/1", DepartmentDTO.class);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception =
                assertThrows(EmployeeNotFoundException.class,
                        () -> employeeService.getEmployeeById(1L));

        assertEquals("Employee not found with id: 1", exception.getMessage());

        verify(employeeRepository).findById(1L);
        verifyNoInteractions(restTemplate);
    }

    @Test
    void shouldUpdateEmployee() {

        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        existingEmployee.setFirstName("Gowtham");
        existingEmployee.setLastName("DV");
        existingEmployee.setEmail("gowtham@example.com");
        existingEmployee.setSalary(50000.0);
        existingEmployee.setDepartmentId(1L);

        EmployeeRequestDTO request = new EmployeeRequestDTO();
        request.setFirstName("Gowtham");
        request.setLastName("D V");
        request.setEmail("gowtham.dvgowda@example.com");
        request.setSalary(80000.0);
        request.setDepartmentId(2L);

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(2L);
        departmentDTO.setDepartmentName("HR");
        departmentDTO.setDepartmentCode("HR001");
        departmentDTO.setDepartmentDescription("Handles human resources");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);
        when(restTemplate.getForObject(
                "http://localhost:8083/departments/2",
                DepartmentDTO.class))
                .thenReturn(departmentDTO);

        EmployeeResponseDTO result = employeeService.updateEmployee(1L, request);

        assertEquals("D V", result.getLastName());
        assertEquals("gowtham.dvgowda@example.com", result.getEmail());
        assertEquals(80000.0, result.getSalary());
        assertEquals(2L, result.getDepartmentId());
        assertNotNull(result.getDepartment());
        assertEquals("HR", result.getDepartment().getDepartmentName());

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(existingEmployee);
        verify(restTemplate).getForObject("http://localhost:8083/departments/2", DepartmentDTO.class);
    }

    @Test
    void shouldDeleteEmployee() {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Gowtham");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).delete(employee);
        verifyNoInteractions(restTemplate);
    }
}