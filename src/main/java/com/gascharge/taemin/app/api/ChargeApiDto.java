package com.gascharge.taemin.app.api;

import com.gascharge.taemin.domain.entity.charge.Charge;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChargeApiDto {
    private String chargePlaceId;
    private String name;
    private Long totalCount;
    private Long currentCount;

    @Builder
    public ChargeApiDto(String chargePlaceId, String name, Long totalCount, Long currentCount) {
        this.chargePlaceId = chargePlaceId;
        this.name = name;
        this.totalCount = totalCount;
        this.currentCount = currentCount;
    }

    public Charge toEntity() {
        return Charge.builder()
                .chargePlaceId(this.chargePlaceId)
                .name(this.name)
                .totalCount(this.totalCount)
                .currentCount(this.currentCount)
                .build();
    }
}
