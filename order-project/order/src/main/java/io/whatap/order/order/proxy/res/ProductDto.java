package io.whatap.order.order.proxy.res;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private Long quantity;

}
