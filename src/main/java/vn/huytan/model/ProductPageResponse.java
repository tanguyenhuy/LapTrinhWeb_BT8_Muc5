package vn.huytan.model;

import java.util.List;
import vn.huytan.entity.Product;

public record ProductPageResponse(List<Product> content, int totalPages, long totalElements, int pageNumber) {}