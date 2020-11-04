package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void verifyAddToCart() throws Exception {

        Cart newCart = new Cart();
        User joanUser = createUser(1l, "Joan", "password", newCart);
        Item newItem = createItem(1L, "Fidget Spinner", new BigDecimal("2"), "Toy");
        ModifyCartRequest newCartRequest = createCartRequest(1L, 5, "Joan");
        ArrayList<Item> listOfItems = new ArrayList<Item>();
        listOfItems.add(newItem);
        newCart = createCart(1l, listOfItems, joanUser);

        when(userRepository.findByUsername("Joan")).thenReturn(joanUser);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(newItem));

        final ResponseEntity<Cart> response = cartController.addToCart(newCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
        assertEquals(5, cart.getItems().size());
    }

    @Test
    public void verifyRemoveFromCart() throws Exception {
        Cart newCart = new Cart();
        User joanUser = createUser(1l, "Joan", "password", newCart);
        Item newItem = createItem(1L, "Fidget Spinner", new BigDecimal("2"), "Toy");
        ModifyCartRequest newCartRequest = createCartRequest(1L, 5, "Joan");

        when(userRepository.findByUsername("Joan")).thenReturn(joanUser);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(newItem));

        final ResponseEntity<Cart> response = cartController.removeFromCart(newCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
        assertEquals(0, cart.getItems().size());
    }

    /**
     * Helper method to create a ModifyCartRequest instance.
     * @param itemId
     * @param quantity
     * @param username
     * @return  ModifyCartRequest object
     */
    public ModifyCartRequest createCartRequest(long itemId, int quantity, String username) {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(itemId);
        cartRequest.setQuantity(quantity);
        cartRequest.setUsername(username);
        return cartRequest;
    }

    /**
     * Helper mthod to create a User instance
     * @param userId
     * @param username
     * @param password
     * @param cart
     * @return  User object
     */
    public User createUser(long userId, String username, String password, Cart cart) {
        User newUser = new User();
        newUser.setId(userId);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setCart(cart);
        return newUser;
    }

    /**
     * Helper method to create an Item instance
     * @param id
     * @param name
     * @param price
     * @param description
     * @return  Item object.
     */
    public Item createItem(Long id, String name, BigDecimal price, String description) {
        Item newItem = new Item();
        newItem.setId(id);
        newItem.setName(name);
        newItem.setPrice(price);
        newItem.setDescription(description);
        return newItem;
    }

    /**
     * Helper method to create a Cart instance.
     * @param cartId
     * @param items
     * @param user
     * @return  Cart object
     */
    public Cart createCart(long cartId, ArrayList<Item> items, User user) {
        Cart newCart = new Cart();
        newCart.setId(cartId);
        newCart.setItems(items);
        newCart.setUser(user);
        return newCart;
    }
}
