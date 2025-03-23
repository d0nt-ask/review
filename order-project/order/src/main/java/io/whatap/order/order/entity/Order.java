package io.whatap.order.order.entity;

import io.whatap.order.order.controller.req.OrderProductCommand;
import io.whatap.order.order.entity.vo.Address;
import io.whatap.order.order.entity.vo.OrderInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "`order`")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    private String userId = "anonymous";
    @Embedded
    private OrderInfo orderInfo;
    @Embedded
    private Address address;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    protected Order(OrderInfo orderInfo, Address address) {
        this.orderInfo = orderInfo;
        this.address = address;
    }

    public static Order from(OrderProductCommand command) {
        return Order.builder()
                .orderInfo(OrderInfo.init())
                .address(Address.builder()
                        .roadAddr(command.getRoadAddr())
                        .jibunAddr(command.getJibunAddr())
                        .detailAddr(command.getDetailAddr())
                        .build())
                .build();
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        this.orderInfo = orderInfo.modifyTotalPrice(this.orderProducts.stream().mapToLong(value -> value.getOrderProductInfo().getTotalPrice()).sum());
    }

    public void modifyOrder(Address address) {
        if (this.orderInfo.getStatus().isEditable()) {
            this.address = address;
        } else {
            throw new IllegalStateException("수정 불가능한 주문 상태 입니다.");
        }
    }

    public void remove() {
        if (this.orderInfo.getStatus().isDeletable()) {
            this.orderProducts.clear();
        } else {
            throw new IllegalStateException("삭제 불가능한 주문 상태 입니다.");
        }
    }
}
