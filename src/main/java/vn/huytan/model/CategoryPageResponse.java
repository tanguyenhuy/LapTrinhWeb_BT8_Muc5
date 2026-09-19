package vn.huytan.model;

import java.util.List;
import vn.huytan.entity.Category;

public record CategoryPageResponse(List<Category> content, int totalPages, long totalElements, int pageNumber) {}