package vn.huytan.controller.api;

import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import vn.huytan.entity.*;
import vn.huytan.model.*;
import vn.huytan.repository.*;

@Controller
@RequiredArgsConstructor
public class GraphQLController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @QueryMapping
    public List<Product> productsByPriceAsc() {
        return productRepository.findAllByOrderByUnitPriceAsc();
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId);
    }

    @QueryMapping
    public ProductPageResponse productsPage(@Argument String keyword, @Argument Long categoryId, @Argument Integer page, @Argument Integer size) {
        int pageIndex = (page == null || page < 0) ? 0 : page;
        int pageSize = (size == null || size <= 0) ? 6 : size;
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("unitPrice").ascending());

        Page<Product> result;
        String kw = (keyword == null) ? "" : keyword.trim();

        if (categoryId != null && categoryId > 0) {
            result = productRepository.findByCategory_CategoryIdAndProductNameContainingIgnoreCase(categoryId, kw, pageable);
        } else {
            result = productRepository.findByProductNameContainingIgnoreCase(kw, pageable);
        }
        return new ProductPageResponse(result.getContent(), result.getTotalPages(), result.getTotalElements(), result.getNumber());
    }

    @QueryMapping
    public Product productById(@Argument Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Category> allCategories() {
        return categoryRepository.findAll();
    }

    @QueryMapping
    public CategoryPageResponse categoriesPage(@Argument String keyword, @Argument Integer page, @Argument Integer size) {
        int pageIndex = (page == null || page < 0) ? 0 : page;
        int pageSize = (size == null || size <= 0) ? 5 : size;
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        String kw = (keyword == null) ? "" : keyword.trim();
        Page<Category> result = categoryRepository.findByCategoryNameContaining(kw, pageable);
        return new CategoryPageResponse(result.getContent(), result.getTotalPages(), result.getTotalElements(), result.getNumber());
    }

    @QueryMapping
    public Category categoryById(@Argument Long id) {
        return categoryRepository.findById(id).orElse(null);
    }


    @MutationMapping
    public Category createCategory(@Argument Map<String, Object> input) {
        Category cat = new Category();
        cat.setCategoryName((String) input.get("categoryName"));
        cat.setIcon((String) input.get("icon"));
        return categoryRepository.save(cat);
    }

    @MutationMapping
    public Category updateCategory(@Argument Long id, @Argument Map<String, Object> input) {
        Category cat = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        cat.setCategoryName((String) input.get("categoryName"));
        if (input.get("icon") != null) {
            cat.setIcon((String) input.get("icon"));
        }
        return categoryRepository.save(cat);
    }

    @MutationMapping
    public Boolean deleteCategory(@Argument Long id) {
        if (!categoryRepository.existsById(id)) return false;
        categoryRepository.deleteById(id);
        return true;
    }

    @MutationMapping
    public Product createProduct(@Argument Map<String, Object> input) {
        Long catId = Long.valueOf(input.get("categoryId").toString());
        Category cat = categoryRepository.findById(catId).orElseThrow(() -> new RuntimeException("Category not found"));

        Product p = new Product();
        p.setProductName((String) input.get("productName"));
        p.setUnitPrice(Double.valueOf(input.get("unitPrice").toString()));
        p.setQuantity((Integer) input.get("quantity"));
        p.setDescription((String) input.get("description"));
        p.setImages((String) input.get("images"));
        p.setDiscount(input.get("discount") != null ? Double.valueOf(input.get("discount").toString()) : 0.0);
        p.setStatus(input.get("status") != null ? ((Integer) input.get("status")).shortValue() : (short) 1);
        p.setCreateDate(new Date());
        p.setCategory(cat);
        return productRepository.save(p);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument Map<String, Object> input) {
        Product p = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        p.setProductName((String) input.get("productName"));
        p.setUnitPrice(Double.valueOf(input.get("unitPrice").toString()));
        p.setQuantity((Integer) input.get("quantity"));
        p.setDescription((String) input.get("description"));
        if (input.get("images") != null) p.setImages((String) input.get("images"));
        
        Long catId = Long.valueOf(input.get("categoryId").toString());
        Category cat = categoryRepository.findById(catId).orElseThrow(() -> new RuntimeException("Category not found"));
        p.setCategory(cat);

        return productRepository.save(p);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        if (!productRepository.existsById(id)) return false;
        productRepository.deleteById(id);
        return true;
    }
}