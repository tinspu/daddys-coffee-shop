package com.daddys.coffee.controllers;


import com.daddys.coffee.repositories.OrderItemRepository;
import com.daddys.coffee.repositories.OrderRepository;
import com.daddys.coffee.repositories.ProductRepository;
import com.daddys.coffee.entities.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerIntegrationTest {

    private static final String INITIAL_PRODUCT_NAME = "InitialProduct";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MockMvc mockMvc;

    private Product initialProductWithPrice;

    @org.junit.Before
    public void setUp() {
        var product = new Product(INITIAL_PRODUCT_NAME);
        product.setPrice(20.2);
        Product initialProduct = productRepository.save(product);
        initialProductWithPrice = productRepository.findById(initialProduct.getId()).orElseThrow(() -> new AssertionError("Initial Product ought to be persisted"));
    }

    @Test
    public void shouldCreateANewProduct() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String newProdName = "New Product";
        String prodJson = ow.writeValueAsString(new Product(newProdName));

        ResultActions resultActions = this.mockMvc.perform(post("/products").contentType(APPLICATION_JSON)
                        .content(prodJson)).andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newProdName));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Product newProduct = mapper.readValue(contentAsString, Product.class);

        Assert.assertTrue("New products Id cannot be zero", newProduct.getId() != 0L);

        try {
            productRepository.findById(newProduct.getId());
        } catch (EntityNotFoundException nfe) {
            throw new AssertionError(newProduct + " has not be persisted");
        }

    }

    @Test
    public void shouldUpdateAnExistingProductWithANewPrice() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String newProdName = "New Product Name";
        String prodJson = ow.writeValueAsString(new Product(newProdName));

        this.mockMvc.perform(put("/products/" + initialProductWithPrice.getId()).contentType(APPLICATION_JSON)
                        .content(prodJson)).andDo(print()).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.name").value(newProdName)).
                andExpect(jsonPath("$.id").value(initialProductWithPrice.getId()));
    }

    private String getProductAsJson(Product productWithNewPrice) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(productWithNewPrice);
    }

    private String getProductAsJSONWithNoPrice(Product product) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(product);
    }
}
