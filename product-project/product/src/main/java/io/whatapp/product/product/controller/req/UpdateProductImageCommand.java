package io.whatapp.product.product.controller.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class UpdateProductImageCommand {
    private UUID productImageId;
    private int sequence;
}
