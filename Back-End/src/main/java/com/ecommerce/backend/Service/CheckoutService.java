package com.ecommerce.backend.Service;

import com.ecommerce.backend.dto.Purchase;
import com.ecommerce.backend.dto.PurchaseResponse;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);
}
