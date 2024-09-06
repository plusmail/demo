package kroryi.demo.controller;

import kroryi.demo.Service.OrderService;
import kroryi.demo.domain.Order;
import kroryi.demo.dto.OrderItemDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestParam Long customerId,
                                             @RequestBody List<OrderItemDTO> orderItems) {


        log.info("Order-> {}", customerId);
        log.info("Order Item-> {}", orderItems.toString());
        Order order = orderService.createOrder(customerId, orderItems);

        //json으로 등록 성공 시 응답
        return ResponseEntity.ok(order);
    }

}
