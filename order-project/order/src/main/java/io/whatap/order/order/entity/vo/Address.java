package io.whatap.order.order.entity.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Address {
    private final String roadAddr; // 도로명주소 전체
    private final String jibunAddr; // 지번 주소
//    private final String zipNo; // 우편주소
//    private final String siNm; // 시도명
//    private final String sggNm; // 시군구명
//    private final String emdNm; // 읍면동명
//    private final String rn; // 성암로
    private final String detailAddr;

    protected Address() {
        this.roadAddr = null;
        this.jibunAddr = null;
//        this.zipNo = null;
//        this.siNm = null;
//        this.sggNm = null;
//        this.emdNm = null;
//        this.rn = null;
        this.detailAddr = null;
    }
}
