package com.gifthub.admin.exception.validator;

import com.gifthub.admin.dto.GifticonAppovalRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class GifticonAppovalValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return GifticonAppovalRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GifticonAppovalRequest request = (GifticonAppovalRequest) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "productName", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "brandName", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "due", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "barcode", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "storageId", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotNull");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "category", "NotSelect");

    }
}
