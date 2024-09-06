package kroryi.demo.controller;


import kroryi.demo.Service.CustomerService;
import kroryi.demo.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomer();
    }

    //이 소스는 html하고 관계가 없다.
    //응답을 Json으로 합니다.
    @PostMapping("")
    public Customer saveCustomer(@RequestBody Customer customer) {

        return customerService.saveCustomer(customer);
    }

}
