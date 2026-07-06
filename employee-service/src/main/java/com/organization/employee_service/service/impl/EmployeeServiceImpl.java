package com.organization.employee_service.service.impl;

import com.organization.employee_service.dto.DepartmentDTO;
import com.organization.employee_service.dto.EmployeeRequestDTO;
import com.organization.employee_service.dto.EmployeeResponseDTO;
import com.organization.employee_service.entity.Employee;
import com.organization.employee_service.exception.EmployeeNotFoundException;
import com.organization.employee_service.mapper.EmployeeMapper;
import com.organization.employee_service.repository.EmployeeRepository;
import com.organization.employee_service.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.organization.employee_service.exception.DepartmentNotFoundException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final RestTemplate restTemplate;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               RestTemplate restTemplate) {
        this.employeeRepository = employeeRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public EmployeeResponseDTO saveEmployee(EmployeeRequestDTO dto) {

        logger.info("Saving employee: {}", dto.getFirstName());

        // 1. Validate department first
        DepartmentDTO departmentDTO = getDepartmentById(dto.getDepartmentId());

        // 2. Only if department exists, convert and save employee
        Employee employee = EmployeeMapper.toEntity(dto);
        Employee savedEmployee = employeeRepository.save(employee);

        logger.info("Employee saved successfully with ID: {}", savedEmployee.getId());

        // 3. Build response
        EmployeeResponseDTO responseDTO = EmployeeMapper.toResponseDTO(savedEmployee);
        responseDTO.setDepartment(departmentDTO);

        return responseDTO;
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {

        logger.info("Fetching all employees");

        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResponseDTO> responseList = new ArrayList<>();

        for (Employee employee : employees) {
            EmployeeResponseDTO responseDTO = EmployeeMapper.toResponseDTO(employee);

            if (employee.getDepartmentId() != null) {
                try {
                    DepartmentDTO departmentDTO = getDepartmentById(employee.getDepartmentId());
                    responseDTO.setDepartment(departmentDTO);
                } catch (DepartmentNotFoundException ex) {
                    logger.warn("Department not found for employee id {} with department id {}",
                            employee.getId(), employee.getDepartmentId());

                    responseDTO.setDepartment(null);
                }
            }

            responseList.add(responseDTO);
        }

        return responseList;
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {

        logger.info("Fetching employee with ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        DepartmentDTO departmentDTO = getDepartmentById(employee.getDepartmentId());

        EmployeeResponseDTO responseDTO = EmployeeMapper.toResponseDTO(employee);
        responseDTO.setDepartment(departmentDTO);

        return responseDTO;
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {

        logger.info("Updating employee with ID: {}", id);

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        // 1. Validate department first
        DepartmentDTO departmentDTO = getDepartmentById(dto.getDepartmentId());

        // 2. Only after department is valid, update employee fields
        existingEmployee.setFirstName(dto.getFirstName());
        existingEmployee.setLastName(dto.getLastName());
        existingEmployee.setEmail(dto.getEmail());
        existingEmployee.setSalary(dto.getSalary());
        existingEmployee.setDepartmentId(dto.getDepartmentId());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        logger.info("Employee updated successfully");

        // 3. Build response
        EmployeeResponseDTO responseDTO = EmployeeMapper.toResponseDTO(updatedEmployee);
        responseDTO.setDepartment(departmentDTO);

        return responseDTO;
    }
    @Override
    public void deleteEmployee(Long id) {

        logger.info("Deleting employee with ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        employeeRepository.delete(employee);

        logger.info("Employee deleted successfully");
    }

    private DepartmentDTO getDepartmentById(Long departmentId) {
        String url = "http://localhost:8083/departments/" + departmentId;

        try {
            return restTemplate.getForObject(url, DepartmentDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new DepartmentNotFoundException(departmentId);
        }
    }
}