package io.whatapp.product.product.controller.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CreateProductCommand {
    private String name;
    private String description;
    private Long price;
    private Long quantity;
    private List<CreateProductImageCommand> images;

}
