package io.whatapp.product.product.controller.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class CreateProductCommand {
    private String name;
    private String description;
    private Long price;
    private Long quantity;
    private List<CreateProductImageCommand> images;

    @Setter
    @Getter
    @NoArgsConstructor
    public static class CreateProductImageCommand {
        private UUID fileId;
        private String fileName;
        private String thumbnailUrl;
        private String originUrl;
    }
}
