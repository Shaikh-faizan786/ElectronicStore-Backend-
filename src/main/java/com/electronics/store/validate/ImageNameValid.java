package com.electronics.store.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.lang.reflect.Field;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface  ImageNameValid {

    // Yeh line specify karti hai ki agar constraint (@NotBlank) violate hoti hai, toh default error message kya hoga. "message" ek property hai, aur {jakarta.validation.constraints.NotBlank.message} ek placeholder hai jise localization ya custom messages ke liye replace kiya ja sakta hai.
    String message() default "{jakarta.validation.constraints.NotBlank.message}";

    // Yeh line define karti hai ki validation constraint kis validation group ke andar aati hai. Default mein yeh empty hoti hai, lekin aap custom groups define kar ke specific scenarios mein constraints ko apply kar sakte ho.
    Class<?>[] groups() default {};

    // Yeh line allow karti hai ki aap additional metadata constraints ke saath associate karein. Custom payload classes banane ke liye use hoti hai, jo validation ke context ke saath specific data pass kar sakti hai.
    Class<? extends Payload>[] payload() default {};

}
