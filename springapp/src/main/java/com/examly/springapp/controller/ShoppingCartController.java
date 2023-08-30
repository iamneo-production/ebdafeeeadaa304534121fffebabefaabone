package com.examly.springapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.model.Product;

// ShoppingCartController.java - Controller Class
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final List<Product> cart = new ArrayList<>();

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestBody Product product) {
        try {
            if (product == null || product.getName() == null || product.getPrice() <= 0 || product.getQuantity() <= 0) {
                return ResponseEntity.badRequest().body("Invalid request payload or missing required fields.");
            }

            cart.add(product);

            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully added the product to the cart");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Invalid request payload or missing required fields.");
        }
    }

    @GetMapping("/total")
    public ResponseEntity<?> getTotalPrice() {
        try {
            if (cart.isEmpty()) {
                throw new NoSuchElementException("The cart is empty.");
            }

            double totalPrice = cart.stream().mapToDouble(product -> product.getPrice() * product.getQuantity()).sum();
            return ResponseEntity.ok("Successfully retrieved the total price of the cart: " + totalPrice);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The cart is empty.");
        }

        // catch (Exception e) {
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Er");
        // }
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout() {
        if (cart.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The cart is empty.");
        }

        try {
            // Implement the payment processing logic here
            // For simplicity, we'll assume the payment is successful

            // Clear the cart after successful checkout
            cart.clear();

            return ResponseEntity.ok("Successfully checked out the cart and processed the payment.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the payment.");
        }
    }
}
