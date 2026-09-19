package vn.huytan.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.huytan.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductName(String name);

    List<Product> findAllByOrderByUnitPriceAsc();
    List<Product> findByCategory_CategoryId(Long categoryId);
    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findByCategory_CategoryIdAndProductNameContainingIgnoreCase(Long categoryId, String keyword, Pageable pageable);
}