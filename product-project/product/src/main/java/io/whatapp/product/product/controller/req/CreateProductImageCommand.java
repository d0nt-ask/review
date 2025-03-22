package io.whatapp.product.product.controller.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class CreateProductImageCommand {
    private UUID fileId;
    private String fileName;
    private int sequence;
    private String thumbnailUrl;
    private String originUrl;
}
