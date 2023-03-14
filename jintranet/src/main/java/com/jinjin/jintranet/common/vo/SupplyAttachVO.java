package com.jinjin.jintranet.common.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SupplyAttachVO extends AttachVO {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String tableName = "SUPPLY_ATTACH";
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String mainTableName = "SUPPLY";

    private Integer supplyId;

    public SupplyAttachVO(Integer id) {
        super(id);
    }
}
