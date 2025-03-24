package io.whatap.order.order.controller.req;

import io.whatap.library.shared.web.Command;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Setter
@Getter
@NoArgsConstructor
public class ChangeOrderCommand extends Command {
    private String roadAddr;
    private String jibunAddr;
    private String detailAddr;
}
