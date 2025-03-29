package io.whatap.order.order.schedule;

import io.whatap.library.shared.lock.annotation.ScheduleLock;
import io.whatap.order.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class OrderSchedule {

    private final OrderService orderService;

    @Scheduled(cron = "0,30 * * * * ?")
    @ScheduleLock(lockName = "deleteExpiredDraftedOrders", leaseTime = 60)
    public void deleteExpiredDraftedOrders() {
        orderService.deleteExpiredDraftedOrders(LocalDateTime.now());
    }
}
