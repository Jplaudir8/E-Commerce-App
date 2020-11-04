package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);


    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
    }

    @Test
    public void verifyAddToCart() throws Exception {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(1);
        cartRequest.setQuantity(2);
        cartRequest.setUsername("Joan");

        
    }
}
