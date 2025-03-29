package io.whatap.order.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.whatap.order.event.FailedOrderCreationEvent;
import io.whatap.order.order.controller.req.ChangeOrderCommand;
import io.whatap.order.order.controller.req.OrderProductCommand;
import io.whatap.order.order.controller.res.OrderDetailDto;
import io.whatap.order.order.controller.res.OrderSummaryDto;
import io.whatap.order.order.entity.Order;
import io.whatap.order.order.entity.enumeration.OrderStatus;
import io.whatap.order.order.event.CreatedOrderEvent;
import io.whatap.order.order.proxy.ProductProxy;
import io.whatap.order.order.proxy.req.DecreaseInventoryQuantityRequest;
import io.whatap.order.order.proxy.res.ProductDto;
import io.whatap.order.order.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
@RecordApplicationEvents
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @MockBean
    private ProductProxy productProxy;

    @SpyBean
    private OrderRepository orderRepository;

    @Autowired
    private ApplicationEvents applicationEvents;

    @PersistenceContext
    private EntityManager em;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        Mockito.when(productProxy.decreaseInventoryQuantities(Arrays.asList(new DecreaseInventoryQuantityRequest(10000L, 10L)))).thenReturn(Arrays.asList(10000L));
        ProductDto productDto = new ProductDto();
        productDto.setId(10000L);
        productDto.setName("나랑드사이다 제로, 500ml, 48개");
        productDto.setDescription("원산지: 상품 상세설명 참조");
        productDto.setPrice(32680L);
        Mockito.when(productProxy.retrieveProduct(10001L)).thenReturn(productDto);
    }

    @AfterEach
    void afterEach() {
        Mockito.reset(orderRepository);
    }


    @Test
    @DisplayName("정상: 단건 조회")
    void getOrder() {
        OrderProductCommand orderProductCommand = createOrderProductCommand();
        Long id = orderService.orderProduct(orderProductCommand);

        OrderDetailDto order = orderService.getOrder(id);

        assertThat(order).isNotNull();
    }

    @Test
    @DisplayName("예외: 미존재 조회")
    void getOrderException() {
        assertThrows(EntityNotFoundException.class, () -> orderService.getOrder(Long.MAX_VALUE));

    }


    @Test
    @DisplayName("정상: 주문")
    void orderProduct() {
        OrderProductCommand orderProductCommand = createOrderProductCommand();
        Long id = orderService.orderProduct(orderProductCommand);

        List<CreatedOrderEvent> list = applicationEvents.stream(CreatedOrderEvent.class).toList();
        OrderDetailDto order = orderService.getOrder(id);
        assertThat(order).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.DRAFT);
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getOrder().getId()).isEqualTo(id);
    }


    @Test
    @DisplayName("예외: 재고 감소 이후 예외 이벤트 발행")
    void orderProductException() {
        Mockito.doThrow(new RuntimeException()).when(orderRepository).save(Mockito.any());
        OrderProductCommand orderProductCommand = createOrderProductCommand();

        assertThatThrownBy(() -> orderService.orderProduct(orderProductCommand));

        Optional<FailedOrderCreationEvent> failedOrderCreationEvent = applicationEvents.stream(FailedOrderCreationEvent.class).findFirst();
        assertThat(failedOrderCreationEvent).isPresent();
    }


    @Test
    @DisplayName("정상: 목록 조회")
    void getOrders() {
        OrderProductCommand orderProductCommand = createOrderProductCommand();

        Long id1 = orderService.orderProduct(orderProductCommand);
        Long id2 = orderService.orderProduct(orderProductCommand);
        Long id3 = orderService.orderProduct(orderProductCommand);
        Long id4 = orderService.orderProduct(orderProductCommand);
        em.createQuery("update Order o set o.orderInfo.status = io.whatap.order.order.entity.enumeration.OrderStatus.PAID where o.id in :ids")
                .setParameter("ids", List.of(id1, id2, id3, id4))
                .executeUpdate();
        em.flush();
        em.clear();

        Page<OrderSummaryDto> pages = (Page<OrderSummaryDto>) orderService.getOrders(null, PageRequest.of(0, 2));
        Slice<OrderSummaryDto> slices = orderService.getOrders(id1, PageRequest.ofSize(2));


        assertThat(pages.getTotalElements()).isGreaterThan(4);
        assertThat(pages.getNumberOfElements()).isEqualTo(2);
        assertThat(slices.getNumberOfElements()).isEqualTo(2);
        assertThat(slices.getContent().get(0).getOrderId()).isEqualTo(id2);
    }

    @Test
    @DisplayName("정상: 주문 수정")
    void changeOrder() {
        OrderProductCommand orderProductCommand = createOrderProductCommand();
        ChangeOrderCommand changeOrderCommand = changeOrderCommand();
        Long id = orderService.orderProduct(orderProductCommand);


        orderService.changeOrder(id, changeOrderCommand);

        Order order = orderRepository.findById(id).get();
        assertThat(order).isNotNull();
        assertThat(order.getAddress().getDetailAddr()).isEqualTo(changeOrderCommand.getDetailAddr());
        assertThat(order.getAddress().getJibunAddr()).isEqualTo(changeOrderCommand.getJibunAddr());
        assertThat(order.getAddress().getRoadAddr()).isEqualTo(changeOrderCommand.getRoadAddr());

    }

    @Test
    @DisplayName("예외: 수정 불가 상태 주문 변경")
    void changeOrderException() {
        OrderProductCommand orderProductCommand = createOrderProductCommand();
        ChangeOrderCommand changeOrderCommand = changeOrderCommand();
        Long id1 = orderService.orderProduct(orderProductCommand);
        em.createQuery("update Order o set o.orderInfo.status = io.whatap.order.order.entity.enumeration.OrderStatus.DELIVERED where o.id in :ids")
                .setParameter("ids", List.of(id1))
                .executeUpdate();
        em.flush();
        em.clear();

        assertThrows(IllegalStateException.class, () -> orderService.changeOrder(id1, changeOrderCommand));
    }

    @Test
    @DisplayName("정상: 주문 삭제")
    void deleteOrder() {
        OrderProductCommand orderProductCommand = createOrderProductCommand();
        Long id = orderService.orderProduct(orderProductCommand);

        orderService.deleteOrder(id);

        assertThrows(EntityNotFoundException.class, () -> orderService.getOrder(id));
    }

    @Test
    @DisplayName("예외: 삭제 불가 상태 주문 변경")
    void deleteOrderException() {
        OrderProductCommand orderProductCommand = createOrderProductCommand();
        Long id = orderService.orderProduct(orderProductCommand);
        em.createQuery("update Order o set o.orderInfo.status = io.whatap.order.order.entity.enumeration.OrderStatus.DELIVERED where o.id in :ids")
                .setParameter("ids", List.of(id))
                .executeUpdate();
        em.flush();
        em.clear();

        assertThrows(IllegalStateException.class, () -> orderService.deleteOrder(id));
    }

    @Test
    void deleteExpiredDraftedOrders() {
        OrderProductCommand orderProductCommand = createOrderProductCommand();
        Long id = orderService.orderProduct(orderProductCommand);
        em.createQuery("update Order o set o.orderInfo.orderCreatedDateTime = :dateTime where o.id in :ids")
                .setParameter("ids", List.of(id))
                .setParameter("dateTime", LocalDateTime.now().minusMinutes(11))
                .executeUpdate();
        em.flush();
        em.clear();

        orderService.deleteExpiredDraftedOrders(LocalDateTime.now());

        assertThat(orderRepository.findById(id)).isNotPresent();
    }


    public OrderProductCommand createOrderProductCommand() {
        try {
            return objectMapper.readValue(
                    """
                              {
                              "products" : [ {
                                "productId" : 10001,
                                "quantity" : 10
                              } ]
                            }
                            """, OrderProductCommand.class
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ChangeOrderCommand changeOrderCommand() {
        try {
            return objectMapper.readValue(
                    """
                              {
                                "roadAddr": "서울특별시 마포구 성암로 301 (상암동)",
                                "jibunAddr": "서울특별시 마포구 상암동 1595 한국지역정보개발원(KLID Tower)",
                                "detailAddr": "1층"
                              } 
                            
                            """, ChangeOrderCommand.class
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}