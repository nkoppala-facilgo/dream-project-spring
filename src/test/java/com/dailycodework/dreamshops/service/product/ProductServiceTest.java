package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        Product product1 = new Product("Product1", "Brand1", new BigDecimal("100.0"), 10,
                "Description1", null);
        Product product2 = new Product("Product2", "Brand2", new BigDecimal("200.0"), 5, "Description2",
                null);
        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Product1", result.get(0).getName());
        assertEquals("Product2", result.get(1).getName());
    }

    @Test
    void testGetProductById_Success() {
        // Arrange
        Long productId = 1L;
        Product product = new Product("Product1", "Brand1", new BigDecimal("100.0"), 10, "Description1", null);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals("Product1", result.getName());
        assertEquals("Brand1", result.getBrand());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetProductById_NotFound() {
        // Arrange
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(productId));

        assertEquals("Product not found!", exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
    }
}
