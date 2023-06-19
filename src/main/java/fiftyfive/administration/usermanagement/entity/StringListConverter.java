package fiftyfive.administration.usermanagement.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String[]> {

    @Override
    public String[] convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toArray(new String[0]);
    }

    @Override
    public List<String> convertToEntityAttribute(String[] dbData) {
        if (dbData == null) {
            return null;
        }
        return Arrays.stream(dbData).collect(Collectors.toList());
    }
}
