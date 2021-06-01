package com.yangzhuo.exception;

import com.yangzhuo.constant.ShopCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CastException {
    public static void cast(ShopCode shopCode) {
        log.error(shopCode.toString());
        throw new CustomerException(shopCode);
    }
}
