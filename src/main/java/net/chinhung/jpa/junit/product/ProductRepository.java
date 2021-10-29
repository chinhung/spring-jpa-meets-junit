package net.chinhung.jpa.junit.product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, String> {

    ProductEntity save(ProductEntity productEntity);

    void deleteById(String id);

    List<ProductEntity> findAll();

    Optional<ProductEntity> findById(String id);
}
