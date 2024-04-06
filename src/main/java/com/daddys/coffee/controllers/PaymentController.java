package com.daddys.coffee.controllers;

import com.daddys.coffee.entities.Order;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class PaymentController {

    String STRIPE_API_KEY = System.getenv().get("STRIPE_API_KEY");

    @PostMapping("/checkout/hosted")
    String hostedCheckout(@RequestBody Order order) throws StripeException {
        return "Hello World!";
    }

}