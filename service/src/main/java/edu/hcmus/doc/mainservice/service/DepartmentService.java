package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.DepartmentDto;
import edu.hcmus.doc.mainservice.model.dto.DepartmentSearchCriteria;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.entity.Department;
import java.util.List;

public interface DepartmentService extends SearchService<DepartmentDto, DepartmentSearchCriteria> {

  List<Department> findAll();

  Department getDepartmentById(Long id);

  Long saveDepartment(Department entity);
}
