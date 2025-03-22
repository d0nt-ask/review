package io.whatapp.product.inventory.controller;

import io.whatapp.product.inventory.controller.req.DecreaseInventoryCommand;
import io.whatapp.product.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/decrease")
    public List<Long> decreaseInventoryQuantities(@RequestBody List<DecreaseInventoryCommand> commands){
        return inventoryService.decreaseInventoryQuantities(commands);
    }

}
