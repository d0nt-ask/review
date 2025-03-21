package io.whatapp.product.product.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ImageInfo {
    private String thumbnailUrl;
    private String originUrl;
}
