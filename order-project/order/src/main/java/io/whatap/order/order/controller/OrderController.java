package io.whatap.order.order.controller;

import io.whatap.order.order.controller.req.OrderProductCommand;
import io.whatap.order.order.controller.res.OrderDetailDto;
import io.whatap.order.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    //GET  getOrders
    //GET  getOrder
    //POST  orderProduct
    // PUT  changeOrder
    // DELETE  deleteOrder

    @GetMapping("/{id}")
    public OrderDetailDto getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @PostMapping
    public Long orderProduct(@RequestBody OrderProductCommand command) {
        return orderService.orderProduct(command);
    }
}
