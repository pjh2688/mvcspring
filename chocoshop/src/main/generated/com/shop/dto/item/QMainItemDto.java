package com.shop.dto.item;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.Generated;

/**
 * com.shop.dto.item.QMainItemDto is a Querydsl Projection type for MainItemDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMainItemDto extends ConstructorExpression<MainItemDto> {

    private static final long serialVersionUID = -2006909614L;

    public QMainItemDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> itemNm, com.querydsl.core.types.Expression<String> itemDetail, com.querydsl.core.types.Expression<String> imgUrl, com.querydsl.core.types.Expression<Integer> price) {
        super(MainItemDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, int.class}, id, itemNm, itemDetail, imgUrl, price);
    }

}

