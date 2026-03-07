package com.ticket4u.feature.homepage.mapper;

import com.ticket4u.core.entity.Category;
import com.ticket4u.feature.homepage.dto.CategoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HomepageMapper {

    CategoryResponse toDTO(Category category);
}
