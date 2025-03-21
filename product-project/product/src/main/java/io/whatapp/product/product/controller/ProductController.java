package io.whatapp.product.product.controller;

import io.whatapp.product.product.controller.req.CreateProductCommand;
import io.whatapp.product.product.controller.req.UpdateProductCommand;
import io.whatapp.product.product.controller.res.ProductDto;
import io.whatapp.product.product.service.ProductService;
import io.whatapp.product.product.repository.ProductRepository;
import io.whatapp.product.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductService productService;

    //GET  getProduct
    //GET  getProductsByPagination
    // POST  addProduct
    //PUT  updateProduct
    //DELETE  deleteProduct
    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable Long id) {
        return productService.findById(id);
    }

    @GetMapping
    public Slice<ProductDto> getProducts(@RequestParam(required = false) Long id, Pageable pageable) {
        return productService.findByPageable(id, pageable);
    }

    @PostMapping
    public Long createProduct(@RequestBody CreateProductCommand command) {
        return productService.createProduct(command);
    }

    @PutMapping("/{id}")
    public Long updateProduct(@PathVariable Long id, @RequestBody UpdateProductCommand command) {
        return productService.updateProduct(id, command);
    }
}
