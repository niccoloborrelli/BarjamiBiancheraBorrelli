package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DepartmentRepository extends CrudRepository<Department, Integer> {
    List<Department> findByDepId_Storeid(int depId_storeid);
}
