package io.whatapp.product.product.controller.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProductCommand {
    private String name;
    private String description;
    private Long price;
}
