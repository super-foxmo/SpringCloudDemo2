package com.newbee.cloud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewBeeGoodsInfo {
    //商品ID
    private int goodsId;
    //商品名称
    private String goodsName;
    //库存
    private int stock;
}
