package com.newbee.cloud.param;

import com.newbee.cloud.entity.NewBeeCartItem;
import com.newbee.cloud.entity.NewBeeGoodsInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ComplexObject {
    private int requestNum;

    private List<Integer> cartIds;

    private List<NewBeeGoodsInfo> newBeeGoodsInfos;

    private NewBeeCartItem newBeeCartItem;
}
