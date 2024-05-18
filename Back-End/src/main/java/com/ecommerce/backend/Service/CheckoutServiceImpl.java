package com.ecommerce.backend.Service;

import com.ecommerce.backend.dao.CustomerRepository;
import com.ecommerce.backend.dto.Purchase;
import com.ecommerce.backend.dto.PurchaseResponse;
import com.ecommerce.backend.entity.Customer;
import com.ecommerce.backend.entity.Order;
import com.ecommerce.backend.entity.OrderItem;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private CustomerRepository customerRepository;



    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        //retrieve the order info from dto
        Order order = purchase.getOrder();
        //generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);
        //populate order with orderItems
        Set<OrderItem> orderItem = purchase.getOrderItems();
        orderItem.forEach(item -> order.add(item));
        //populate order with billingAddress and shippingAddress
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        //populate customer with order
        Customer customer = purchase.getCustomer();
        customer.add(order);

        //save to the database
        customerRepository.save(customer);
        //return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {
        // generate a random UUID number (UUID version-4) universally unique identifier
        return UUID.randomUUID().toString();
    }
}
