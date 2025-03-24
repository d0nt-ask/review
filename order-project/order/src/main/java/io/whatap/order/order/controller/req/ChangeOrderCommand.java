package io.whatap.order.order.controller.req;

import io.whatap.library.shared.web.Command;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
@NoArgsConstructor
public class ChangeOrderCommand extends Command {
    private String roadAddr;
    private String jibunAddr;
    private String detailAddr;

    @Override
    public void validate() {
        if (StringUtils.isAnyBlank(roadAddr, jibunAddr)) {
            throw new IllegalArgumentException("도로명 또는 지번 주소는 필수 입력값입니다.");
        }

        if (StringUtils.length(detailAddr) > 300) {
            throw new IllegalArgumentException("상세 주소는 300자 이내로 입력해주세요.");
        }
    }
}
