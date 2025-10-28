package com.example.onlinestore;

import com.example.onlinestore.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectMapperTest {
    ObjectMapper om = new ObjectMapper();

    @Test
    void serializeAndDeserialize_Product() throws Exception {
        Product p = new Product(1L,"Mouse","optical",25.5,7);

        String json = om.writeValueAsString(p);
        Product back = om.readValue(json, Product.class);

        assertThat(back.getProductId()).isEqualTo(1L);
        assertThat(back.getName()).isEqualTo("Mouse");
        assertThat(json).contains("\"price\":25.5");
    }
}
