package io.whatapp.product.product.controller.req;

import io.whatap.library.shared.web.Command;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class UpdateProductCommand extends Command {
    private String name;
    private String description;
    private Long price;
    private List<CreateProductImageCommand> createdProductImages;
    private List<UpdateProductImageCommand> updatedProductImages;
    private List<UUID> deletedProductImages;
}
