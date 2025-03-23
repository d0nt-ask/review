package io.whatap.order.order.controller;

import io.whatap.order.order.controller.req.ChangeOrderCommand;
import io.whatap.order.order.controller.req.OrderProductCommand;
import io.whatap.order.order.controller.res.OrderDetailDto;
import io.whatap.order.order.controller.res.OrderSummaryDto;
import io.whatap.order.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    @GetMapping
    public Slice<OrderSummaryDto> getOrders(@RequestParam(required = false) Long id, Pageable pageable) {
        return orderService.getOrders(id, pageable);
    }

    @PostMapping
    public Long orderProduct(@RequestBody OrderProductCommand command) {
        return orderService.orderProduct(command);
    }

    @PutMapping("/{id}")
    public Long changeOrder(@PathVariable Long id, @RequestBody ChangeOrderCommand command) {
        return orderService.changeOrder(id, command);
    }

    @DeleteMapping("/{id}")
    public Long deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }
}
