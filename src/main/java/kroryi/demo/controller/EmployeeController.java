package kroryi.demo.controller;

import kroryi.demo.Service.EmployeeService;
import kroryi.demo.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// json으로 응답하는 Controller
// API통신이나 비동기 통신 시 사용
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // id=2
    @GetMapping("/employee/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/employee/department")
    public List<Employee> getEmployeeByDepartment(@RequestParam String department) {
        return employeeService.getEmployeesByDepartment(department);
    }
}
