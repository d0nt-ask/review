package io.whatapp.product.product.controller.req;

import io.whatap.library.shared.web.Command;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CreateProductCommand extends Command {
    private String name;
    private String description;
    private Long price;
    private Long quantity;
    private List<CreateProductImageCommand> images;

    @Override
    public void validate() {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("상품명은 필수 입력값입니다.");
        }
        if (price == null) {
            throw new IllegalArgumentException("가격은 필수 입력값입니다.");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("재고는 필수 입력값입니다.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("가격은 0 이상이여야 합니다");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("재고는 0 이상이여야 합니다");
        }
    }
}
