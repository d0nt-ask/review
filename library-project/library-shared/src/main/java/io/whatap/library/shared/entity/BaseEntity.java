package io.whatap.library.shared.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
@Setter
@NoArgsConstructor
@Getter
public class BaseEntity {
    private String createdId = "anonymous";
    @Column(updatable = false, insertable = false)
    private Instant createdDate;
    private String modifiedId = "anonymous";
    @Column(updatable = false, insertable = false)
    private Instant modifiedDate;
}
