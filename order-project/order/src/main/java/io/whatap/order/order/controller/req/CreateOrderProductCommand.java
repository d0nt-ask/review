package io.whatap.order.order.controller.req;

import io.whatap.library.shared.web.Command;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
@NoArgsConstructor
public class CreateOrderProductCommand extends Command {
    private Long productId;
    private Long quantity;

    @Override
    public void validate() {
        if (productId == null) {
            throw new IllegalArgumentException("상품ID는 필수 입력값입니다.");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("수량은 필수 입력값입니다.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1개 이상이여야 합니다.");
        }
    }
}
