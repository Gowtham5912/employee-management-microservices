package com.organization.employee_service.service.impl;

import com.organization.employee_service.entity.Employee;
import com.organization.employee_service.exception.EmployeeNotFoundException;
import com.organization.employee_service.repository.EmployeeRepository;
import com.organization.employee_service.service.EmployeeService;
import org.springframework.stereotype.Service;
import com.organization.employee_service.exception.EmployeeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger =
        LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        logger.info("Saving employee: {}", employee.getFirstName());

        Employee savedEmployee = employeeRepository.save(employee);

        logger.info("Employee saved successfully with ID: {}", savedEmployee.getId());

        return savedEmployee;
    }

    @Override
    public List<Employee> getAllEmployees() {

        logger.info("Fetching all employees");

        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {

        logger.info("Fetching employee with ID: {}", id);

        return employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {

        logger.info("Updating employee with ID: {}", id);
        
        Employee existingEmployee = employeeRepository.findById(id).orElse(null);

        if (existingEmployee != null) {

            existingEmployee.setFirstName(employee.getFirstName());
            existingEmployee.setLastName(employee.getLastName());
            existingEmployee.setEmail(employee.getEmail());
            existingEmployee.setSalary(employee.getSalary());  

            logger.info("Employee updated successfully");

            return employeeRepository.save(existingEmployee);
        }

        return null;
    }

    @Override
    public void deleteEmployee(Long id) {

        logger.info("Deleting employee with ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        employeeRepository.delete(employee);

        logger.info("Employee deleted successfully");

}
}