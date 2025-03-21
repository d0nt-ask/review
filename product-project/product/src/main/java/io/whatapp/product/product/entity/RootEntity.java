package io.whatapp.product.product.entity;

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
public class RootEntity {
    private String createdId;
    @Column(updatable = false, insertable = false)
    private Instant createdDate;
    private String modifiedId;
    @Column(updatable = false, insertable = false)
    private Instant modifiedDate;
}
