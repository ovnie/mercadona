package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {

    // Déclaration d'une liste pour stocker les produits
    private List<Product> products = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RestController
    public class PromotionController {

        @GetMapping("/api/promotions")
        public List<Promotion> getPromotions() {
            // Fetch promotions from the database
            List<Promotion> promotions = new ArrayList<>();
            for (Product product : products) {
                promotions.add(new Promotion(product.getName(), calculatePromotionPrice(product.getPrice(), 10.0), 10.0));
            }
            return promotions;
        }

        @PostMapping("/api/products")
        public Product createProduct(@RequestBody ProductRequest productRequest) {
            Product product = new Product(productRequest.getName(), productRequest.getPrice());
            products.add(product);
            return product;
        }

        @PostMapping("/api/promotions/{productId}")
        public Promotion createPromotion(@PathVariable int productId, @RequestParam double percentage) {
            Product product = getProductById(productId);
            if (product != null) {
                double promotionPrice = calculatePromotionPrice(product.getPrice(), percentage);
                Promotion promotion = new Promotion(product.getName(), promotionPrice, percentage);
                return promotion;
            }
            return null;
        }
    }

    private double calculatePromotionPrice(double price, double percentage) {
        return price - (price * (percentage / 100));
    }

    private Product getProductById(int productId) {
        for (Product product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    public static class Product {
        private static int nextId = 1;

        private int id;
        private String name;
        private double price;

        public Product(String name, double price) {
            this.id = nextId++;
            this.name = name;
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }

    public static class ProductRequest {
        private String name;
        private double price;

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }

    public static class Promotion {
        private String productName;
        private double promotionPrice;
        private double percentage;

        public Promotion(String productName, double promotionPrice, double percentage) {
            this.productName = productName;
            this.promotionPrice = promotionPrice;
            this.percentage = percentage;
        }

        public String getProductName() {
            return productName;
        }

        public double getPromotionPrice() {
            return promotionPrice;
        }

        public double getPercentage() {
            return percentage;
        }
    }
}