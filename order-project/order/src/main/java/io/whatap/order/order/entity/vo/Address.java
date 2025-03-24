package io.whatap.order.order.entity.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private final String roadAddr; // 도로명주소 전체
    private final String jibunAddr; // 지번 주소
    private final String detailAddr;

    protected Address() {
        this.roadAddr = null;
        this.jibunAddr = null;
        this.detailAddr = null;
    }

    @Builder
    private Address(String roadAddr, String jibunAddr, String detailAddr) {
        if (StringUtils.isAnyBlank(roadAddr, jibunAddr)) {
            throw new IllegalArgumentException("도로명 또는 지번 주소는 필수 입력값입니다.");
        }

        if (StringUtils.length(detailAddr) > 300) {
            throw new IllegalArgumentException("상세 주소는 300자 이내로 입력해주세요.");
        }
        this.roadAddr = roadAddr;
        this.jibunAddr = jibunAddr;
        this.detailAddr = detailAddr;
    }
}
