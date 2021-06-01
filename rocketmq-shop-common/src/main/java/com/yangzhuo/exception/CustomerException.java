package com.yangzhuo.exception;

import com.yangzhuo.constant.ShopCode;

public class CustomerException extends RuntimeException {
    
    private ShopCode shopCode;

    public CustomerException(ShopCode shopCode) {
        this.shopCode = shopCode;
    }
}
