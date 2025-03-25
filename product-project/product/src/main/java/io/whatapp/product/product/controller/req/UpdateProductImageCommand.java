package io.whatapp.product.product.controller.req;

import io.whatap.library.shared.web.Command;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class UpdateProductImageCommand extends Command {
    private UUID productImageId;
    private int sequence;

    @Override
    public void validate() {
        if (productImageId == null) {
            throw new IllegalArgumentException("사진ID는 필수 입력값입니다.");

        }
    }
}
