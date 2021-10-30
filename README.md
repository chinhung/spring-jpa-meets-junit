# Spring JPA meets JUnit

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/chinhung/pointwave/blob/master/LICENSE)

This is a demo repository for validating the Spring JPA configurations via JUnit.

## Run Tests
```
./gradlew test
```

## Example

### Product Entity
```java
@Entity
@Table(name="products")
public class ProductEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "price")
    @NotNull
    private Integer price;

    // ...
}
```

### Product Repository
```java
@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, String> {

    List<ProductEntity> findAll();
}
```

## Testing Setup 

```java
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

    // ...
}
```

## Test Cases

```java
@DataJpaTest
public class ProductRepositoryTest {

    // ...

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
```