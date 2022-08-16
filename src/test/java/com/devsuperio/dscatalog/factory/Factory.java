package com.devsuperio.dscatalog.factory;

import com.devsuperio.dscatalog.dto.ProductDTO;
import com.devsuperio.dscatalog.entities.Category;
import com.devsuperio.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {
    public static Product createProduct() {
        Product product = new Product(1L, "Smart TV",
                "Good Smartphone",
                2190.0,
                "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg",
                Instant.parse("2020-07-14T10:00:00Z"));

        product.getCategories().add(new Category(2L, "Electronics"));

        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
