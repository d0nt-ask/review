package io.whatapp.product.product.controller.req;

import io.whatap.library.shared.web.Command;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class CreateProductImageCommand extends Command {
    private UUID fileId;
    private String fileName;
    private int sequence;
    private String thumbnailUrl;
    private String originUrl;

    @Override
    public void validate() {
        if (fileId == null) {
            throw new IllegalArgumentException("파일ID는 필수 입력값입니다.");
        }
    }
}
