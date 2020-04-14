package org.cmas.presentation.model.admin;

import org.cmas.Globals;
import org.cmas.entities.User;
import org.cmas.presentation.model.Editable;
import org.cmas.presentation.model.user.UserFormObject;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.cmas.util.StringUtil;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.Pattern;
import org.jetbrains.annotations.Nullable;
import org.springframework.validation.Errors;

public class AdminUserFormObject extends UserFormObject implements Editable<User>, Validatable {

	@Nullable
    private Long id;

//    @NotEmpty(message = "validation.passwordEmpty")
//    private String password;

    @Pattern( regex = Globals.SIMPLE_EMAIL_REGEXP
            , message = "validation.emailValid"
            , flags = java.util.regex.Pattern.CASE_INSENSITIVE
    )
    @NotEmpty(message = "validation.emailEmpty")
    private String email;

    @NotEmpty(message = "validation.emailEmpty")
    private String discountPercent;

	@Override
    public void transferToEntity(User entity) {
		super.transferToEntity(entity);
//        if (password !=null){
//			entity.setPassword(password);
//		}
        entity.setEmail(StringUtil.lowerCaseEmail(email));
//        entity.getUserBalance().setDiscountPercent(
//                new BigDecimal(discountPercent)
//        );

    }

    @Override
    public void transferFromEntity(User entity) {
		super.transferFromEntity(entity);
   //     password = entity.getPassword();
        email = entity.getEmail();
//        discountPercent = String.valueOf(
//                entity.getUserBalance().getDiscountPercent()
//                );
        id = entity.getId();
    }

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateBigDecimal(
                errors, discountPercent, "discountPercent", "validation.incorrectNumber"
        );
    }

    @Override
    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    //    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
}
