package io.whatapp.product.inventory.controller;

import com.zaxxer.hikari.HikariDataSource;
import io.whatap.library.shared.web.Command;
import io.whatapp.product.inventory.controller.req.DecreaseInventoryCommand;
import io.whatapp.product.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;
    @Autowired
    private HikariDataSource dataSource;

    @PostMapping("/decrease")
    public List<Long> decreaseInventoryQuantities(@RequestBody List<DecreaseInventoryCommand> commands) {
        commands.forEach(Command::validate);
        return inventoryService.decreaseInventoryQuantities(commands);
    }


    @PostMapping("/decrease/v2")
    public List<Long> decreaseInventoryQuantitiesV2(@RequestBody List<DecreaseInventoryCommand> commands) {
        commands.forEach(Command::validate);
        StringBuilder status = new StringBuilder();
        status.append("Pool Name: ").append(dataSource.getPoolName()).append("\n");
        status.append("Active Connections: ").append(dataSource.getHikariPoolMXBean().getActiveConnections()).append("\n");
        status.append("Idle Connections: ").append(dataSource.getHikariPoolMXBean().getIdleConnections()).append("\n");
        status.append("Total Connections: ").append(dataSource.getHikariPoolMXBean().getTotalConnections()).append("\n");
        status.append("Threads Awaiting Connection: ").append(dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection()).append("\n");
        log.info(status.toString());
        return inventoryService.decreaseInventoryQuantitiesV2(commands);
    }

}
