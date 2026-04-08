package com.ticket4u.core.entity.converters;

import com.ticket4u.core.entity.BannerUrl;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tools.jackson.databind.ObjectMapper;

@Converter
public class BannerUrlConverter implements AttributeConverter<BannerUrl, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(BannerUrl attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not serialize BannerUrl", e);
        }
    }

    @Override
    public BannerUrl convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, BannerUrl.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not deserialize BannerUrl", e);
        }
    }
}
