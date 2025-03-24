package io.whatap.order.order.service;

import io.whatap.order.event.DeletedOrderEvent;
import io.whatap.order.event.FailedOrderCreationEvent;
import io.whatap.order.event.dto.OrderProductDto;
import io.whatap.order.order.controller.req.ChangeOrderCommand;
import io.whatap.order.order.controller.req.CreateOrderProductCommand;
import io.whatap.order.order.controller.req.OrderProductCommand;
import io.whatap.order.order.controller.res.OrderDetailDto;
import io.whatap.order.order.controller.res.OrderSummaryDto;
import io.whatap.order.order.entity.Order;
import io.whatap.order.order.entity.OrderProduct;
import io.whatap.order.order.entity.enumeration.OrderStatus;
import io.whatap.order.order.entity.vo.Address;
import io.whatap.order.order.event.CreatedOrderEvent;
import io.whatap.order.order.proxy.ProductProxy;
import io.whatap.order.order.proxy.req.DecreaseInventoryQuantityRequest;
import io.whatap.order.order.proxy.res.ProductDto;
import io.whatap.order.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductProxy productProxy;
    private final ApplicationEventPublisher eventPublisher;

    public Long orderProduct(OrderProductCommand command) {
        List<DecreaseInventoryQuantityRequest> requests = command.getProducts().stream().map(DecreaseInventoryQuantityRequest::from).toList();
        productProxy.decreaseInventoryQuantities(requests);
        boolean published = false;
        try {
            Order order = Order.from(command);
            addOrderProducts(order, command.getProducts());
            orderRepository.save(order);
            eventPublisher.publishEvent(CreatedOrderEvent.from(order));
            published = true;
            return order.getId();
        } catch (Exception e) {
            if (!published) {
                eventPublisher.publishEvent(FailedOrderCreationEvent.builder()
                        .orderProducts(command.getProducts().stream()
                                .map(value -> new OrderProductDto(value.getProductId(), value.getQuantity())).toList()).build());
            }
            throw e;
        }
    }

    private void addOrderProducts(Order order, List<CreateOrderProductCommand> commands) {
        if (!CollectionUtils.isEmpty(commands)) {
            for (CreateOrderProductCommand command : commands) {
                ProductDto productDto = productProxy.retrieveProduct(command.getProductId());
                OrderProduct orderProduct = OrderProduct.fromCreateCommand(order, productDto, command);
                order.addOrderProduct(orderProduct);
            }

        }
    }

    public OrderDetailDto getOrder(Long id) {
        return orderRepository.findById(id).map(OrderDetailDto::from).orElseThrow(() -> new EntityNotFoundException("Order not found"));

    }

    public Slice<OrderSummaryDto> getOrders(Long id, Pageable pageable) {
        if (id == null) {
            Page<Order> products = orderRepository.findByUserIdAndOrderInfoStatusNot("anonymous", OrderStatus.CREATED, pageable);
            return new PageImpl<>(products.getContent().stream().map(OrderSummaryDto::from).toList(), products.getPageable(), products.getTotalElements());
        } else {
            Slice<Order> products = orderRepository.findByIdGreaterThanAndUserIdAndOrderInfoStatusNot(id, "anonymous", OrderStatus.CREATED, pageable);
            return new SliceImpl<>(products.getContent().stream().map(OrderSummaryDto::from).toList(), products.getPageable(), products.hasNext());
        }
    }

    public Long changeOrder(Long id, ChangeOrderCommand command) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            throw new EntityNotFoundException("Order not found");

        } else {
            Order order = optionalOrder.get();
            Address address = Address.builder()
                    .roadAddr(command.getRoadAddr())
                    .jibunAddr(command.getJibunAddr())
                    .detailAddr(command.getDetailAddr())
                    .build();

            order.modifyOrder(address);
            orderRepository.save(order);
            return order.getId();
        }
    }

    public Long deleteOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            throw new EntityNotFoundException("Order not found");

        } else {
            Order order = optionalOrder.get();
            List<OrderProduct> deleteOrderProucts = new ArrayList<>(order.getOrderProducts());
            order.remove();
            orderRepository.delete(order);
            eventPublisher.publishEvent(DeletedOrderEvent.builder().orderProducts(deleteOrderProucts.stream().map(orderProduct -> new OrderProductDto(orderProduct.getProductId(), orderProduct.getOrderProductInfo().getQuantity())).toList()).build());
            return order.getId();
        }
    }
}
