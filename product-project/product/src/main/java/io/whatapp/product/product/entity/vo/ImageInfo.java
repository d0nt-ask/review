package io.whatapp.product.product.entity.vo;

import javax.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageInfo {
    private final String thumbnailUrl;
    private final String originUrl;

    protected ImageInfo() {
        thumbnailUrl = null;
        originUrl = null;
    }
}
