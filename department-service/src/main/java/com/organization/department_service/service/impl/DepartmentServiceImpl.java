package com.organization.department_service.service.impl;

import com.organization.department_service.entity.Department;
import com.organization.department_service.exception.ResourceNotFoundException;
import com.organization.department_service.repository.DepartmentRepository;
import com.organization.department_service.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    @Override
    public Department updateDepartment(Long id, Department department) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        existingDepartment.setDepartmentName(department.getDepartmentName());
        existingDepartment.setDepartmentCode(department.getDepartmentCode());
        existingDepartment.setDepartmentDescription(department.getDepartmentDescription());

        return departmentRepository.save(existingDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        departmentRepository.delete(existingDepartment);
    }
}