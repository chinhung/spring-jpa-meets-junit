# Spring JPA meets JUnit

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/chinhung/pointwave/blob/master/LICENSE)

This is a demo repository for validating the Spring JPA configurations via JUnit.

## Introduction

With the help of `@DataJpaTest` annotation, we could initialize the repository objects in the JUnit test cases. Validating the behavior of repository objects such as the CRUD operations and the database constrains becomes much easier.

## Run Tests
```
./gradlew test
```

## Example

### Product Entity

Use the validation annotation `@NotNull` to validate the field by hibernate validator. Use `@Column(nullable = false)` to generate the NOT NULL constraint in DDL.
```java
@Entity
@Table(name="products")
public class ProductEntity {

    @Id
    @Column(name = "id")
    private String id;

    @NotNull
    @Column(name="name", nullable = false)
    private String name;

    @NotNull
    @Column(name="price", nullable = false)
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

### Enable Automatically Validation Before Database Operations

Add the following three dependencies to `build.gradle`:
```groovy
dependencies {
    implementation 'org.hibernate.validator:hibernate-validator:7.0.1.Final'
    implementation 'org.hibernate.validator:hibernate-validator-cdi:7.0.1.Final'
    implementation 'org.glassfish:jakarta.el:4.0.0'
}
```

### Test Cases Setup
The annotation `@DataJpaTest` is required to inject the ProductRepository by Spring with `@Autowired` annotation. The test data is initialized by the `setUp` method before each test case with the annotation `@BeforeEach`. By default, `@DataJpaTest` annotated test cases are transactional and will roll back at the end of each test case.
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
    public void saveFail_NameNull() {
        assertFalse(productRepository.findById("4").isPresent());

        ProductEntity iPod = new ProductEntity();
        iPod.setId("4");
        iPod.setName(null);
        iPod.setPrice(7000);

        assertThrows(DataIntegrityViolationException.class, () -> {
            productRepository.save(iPod);
        });
    }

    @Test
    public void saveFail_PriceNull() {
        assertFalse(productRepository.findById("4").isPresent());

        ProductEntity iPod = new ProductEntity();
        iPod.setId("4");
        iPod.setName("iPod");
        iPod.setPrice(null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            productRepository.save(iPod);
        });
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