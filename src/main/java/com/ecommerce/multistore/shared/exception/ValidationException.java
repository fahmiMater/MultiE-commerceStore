
/* ---- File: src/main/java/com/ecommerce/multistore/shared/exception/ValidationException.java ---- */

package com.ecommerce.multistore.shared.exception;

import java.util.List;

/**
 * استثناء التحقق من الصحة
 * Validation Exception
 */
public class ValidationException extends RuntimeException {

    private final List<String> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = List.of(message);
    }

    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public ValidationException(List<String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
