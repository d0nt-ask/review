package io.whatap.order.order.controller;

import io.whatap.order.order.controller.req.CreateOrderCommand;
import io.whatap.order.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public Long createOrder(@RequestBody CreateOrderCommand command){
        return orderService.createOrder(command);
    }
}
