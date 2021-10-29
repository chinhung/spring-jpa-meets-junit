package net.chinhung.jpa.junit.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        ProductEntity iPhone = new ProductEntity();
        iPhone.setId("1");
        iPhone.setName("iPhone");
        iPhone.setPrice(30000);

        ProductEntity macbook = new ProductEntity();
        macbook.setId("2");
        macbook.setName("macbook");
        macbook.setPrice(70000);

        ProductEntity iMac = new ProductEntity();
        iMac.setId("3");
        iMac.setName("iMac");
        iMac.setPrice(50000);

        productRepository.save(iPhone);
        productRepository.save(macbook);
        productRepository.save(iMac);
    }

    @Test
    public void injectedComponentsAreNotNull() {
        assertNotNull(productRepository);
    }

    @Test
    public void findById() {
        assertTrue(productRepository.findById("1").isPresent());
    }

    @Test
    public void save() {
        assertFalse(productRepository.findById("4").isPresent());

        ProductEntity iPod = new ProductEntity();
        iPod.setId("4");
        iPod.setName("iPod");
        iPod.setPrice(7000);
        productRepository.save(iPod);

        assertTrue(productRepository.findById("4").isPresent());
    }

    @Test
    public void deleteById() {
        assertTrue(productRepository.findById("3").isPresent());

        productRepository.deleteById("3");

        assertFalse(productRepository.findById("3").isPresent());
    }

    @Test
    public void findAll() {
        assertEquals(3, productRepository.findAll().size());
    }
}
