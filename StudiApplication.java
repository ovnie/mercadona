package vps9d5e18f7.vps.ovh.net.studi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import java.time.LocalDate;
import java.sql.Date;

import org.springframework.dao.EmptyResultDataAccessException;

@SpringBootApplication
public class StudiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(StudiApplication.class, args);
    }
}

@RestController
@RequestMapping("/api/products")
class ProductController {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
    	String sql = "INSERT INTO products (label, description, price, image, category, promotion_start_date, promotion_end_date, promotion_discount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection con) -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getLabel());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setString(4, product.getImage());
            ps.setString(5, product.getCategory());
            ps.setDate(6, Date.valueOf(product.getPromotionStartDate()));
            ps.setDate(7, Date.valueOf(product.getPromotionEndDate()));
            ps.setDouble(8, product.getPromotionDiscount());
            return ps;
        }, keyHolder);

        List<Map<String, Object>> keyList = keyHolder.getKeyList();
        if (keyList != null && !keyList.isEmpty()) {
            Map<String, Object> keyMap = keyList.get(0);
            Integer generatedId = (Integer) keyMap.get("id");
            product.setId(generatedId.longValue());
        }
        return product;
    }
    
    @GetMapping
    public List<Product> getProducts() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Product product = new Product();
            product.setId(rs.getLong("id"));
            product.setLabel(rs.getString("label"));
            product.setDescription(rs.getString("description"));
            product.setPrice(rs.getDouble("price"));
            product.setImage(rs.getString("image"));
            product.setCategory(rs.getString("category"));
            product.setPromotionStartDate(rs.getDate("promotion_start_date").toLocalDate());
            product.setPromotionEndDate(rs.getDate("promotion_end_date").toLocalDate());
            product.setPromotionDiscount(rs.getDouble("promotion_discount"));
            return product;
        });
    }
    
    @PostMapping("/promotion")
    public Product addPromotion(@RequestBody PromotionData promotionData) {
        // Récupérer le produit à partir de la base de données en utilisant l'ID du produit
        Product product = getProductById(promotionData.getProductId());

        if (product != null) {
            // Mettre à jour les champs de promotion du produit
            product.setPromotionStartDate(promotionData.getPromotionStartDate());
            product.setPromotionEndDate(promotionData.getPromotionEndDate());
            product.setPromotionDiscount(promotionData.getPromotionDiscount());

            // Calculer le nouveau prix avec la remise
            double initialPrice = product.getPrice();
            double discount = promotionData.getPromotionDiscount();
            double discountedPrice = initialPrice - (initialPrice * discount);
            product.setPrice(discountedPrice);

            // Mettre à jour le produit dans la base de données
            updateProduct(product);

            return product;
        } else {
            // Gérer le cas où le produit avec l'ID spécifié n'est pas trouvé
            return null;
        }
    }

    // Méthode pour récupérer un produit par ID depuis la base de données
    private Product getProductById(Long productId) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{productId}, (rs, rowNum) -> {
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setLabel(rs.getString("label"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setImage(rs.getString("image"));
                product.setCategory(rs.getString("category"));
                product.setPromotionStartDate(rs.getDate("promotion_start_date").toLocalDate());
                product.setPromotionEndDate(rs.getDate("promotion_end_date").toLocalDate());
                product.setPromotionDiscount(rs.getDouble("promotion_discount"));
                return product;
            });
        } catch (EmptyResultDataAccessException e) {
            // Le produit avec l'ID spécifié n'a pas été trouvé
            return null;
        }
    }

    // Méthode pour mettre à jour un produit dans la base de données
    private void updateProduct(Product product) {
        String sql = "UPDATE products SET promotion_start_date = ?, promotion_end_date = ?, " +
                     "promotion_discount = ?, price = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                Date.valueOf(product.getPromotionStartDate()),
                Date.valueOf(product.getPromotionEndDate()),
                product.getPromotionDiscount(),
                product.getPrice(),
                product.getId()
        );
    }
}

class Product {
    private Long id;
    private String label;
    private String description;
    private double price;
    private String image;
    private String category;
    private LocalDate promotionStartDate;
    private LocalDate promotionEndDate;
    private double promotionDiscount;

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    public LocalDate getPromotionStartDate() {
        return promotionStartDate;
    }

    public void setPromotionStartDate(LocalDate promotionStartDate) {
        this.promotionStartDate = promotionStartDate;
    }

    public LocalDate getPromotionEndDate() {
        return promotionEndDate;
    }

    public void setPromotionEndDate(LocalDate promotionEndDate) {
        this.promotionEndDate = promotionEndDate;
    }

    public double getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(double promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }
}

class PromotionData {
    private Long productId;
    private LocalDate promotionStartDate;
    private LocalDate promotionEndDate;
    private double promotionDiscount;

    // Ajoutez les getters et les setters pour les champs de la classe

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public LocalDate getPromotionStartDate() {
        return promotionStartDate;
    }

    public void setPromotionStartDate(LocalDate promotionStartDate) {
        this.promotionStartDate = promotionStartDate;
    }

    public LocalDate getPromotionEndDate() {
        return promotionEndDate;
    }

    public void setPromotionEndDate(LocalDate promotionEndDate) {
        this.promotionEndDate = promotionEndDate;
    }

    public double getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(double promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }
}