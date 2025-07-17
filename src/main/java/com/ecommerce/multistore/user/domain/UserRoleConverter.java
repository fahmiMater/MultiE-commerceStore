package com.ecommerce.multistore.user.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * محول دور المستخدم لقاعدة البيانات
 * User role converter for database
 */
@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, String> {

    @Override
    public String convertToDatabaseColumn(UserRole attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public UserRole convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return UserRole.fromValue(dbData);
    }
}
