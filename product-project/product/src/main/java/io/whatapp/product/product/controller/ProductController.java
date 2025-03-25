package io.whatapp.product.product.controller;

import io.whatapp.product.product.controller.req.CreateProductCommand;
import io.whatapp.product.product.controller.req.UpdateProductCommand;
import io.whatapp.product.product.controller.res.ProductDetailDto;
import io.whatapp.product.product.controller.res.ProductSummaryDto;
import io.whatapp.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    //GET  getProduct
    //GET  getProductsByPagination
    // POST  addProduct
    //PUT  updateProduct
    //DELETE  deleteProduct
    @GetMapping("/{id}")
    public ProductDetailDto getProduct(@PathVariable Long id) {
        return productService.findById(id);
    }

    @GetMapping
    public Slice<ProductSummaryDto> getProductsByPagination(@RequestParam(required = false) Long id, Pageable pageable) {
        return productService.findByPageable(id, pageable);
    }

    @PostMapping
    public Long addProduct(@RequestBody CreateProductCommand command) {
        command.validate();
        return productService.createProduct(command);
    }

    @PutMapping("/{id}")
    public Long updateProduct(@PathVariable Long id, @RequestBody UpdateProductCommand command) {
        command.validate();
        return productService.updateProduct(id, command);
    }

    @DeleteMapping("/{id}")
    public Long deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

}
