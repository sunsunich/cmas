package org.cmas.presentation.model.user;

import org.cmas.Globals;
import org.cmas.presentation.controller.admin.AdminController;
import org.cmas.presentation.model.ColumnName;
import org.cmas.presentation.model.SortPaginatorImpl;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.springframework.validation.Errors;

/**
 * Created on Apr 06, 2020
 *
 * @author Alexander Petukhov
 */
public class AddingToFederationFormObject extends SortPaginatorImpl<AddingToFederationFormObject.UserReportColumnNames> implements Validatable {

    @SuppressWarnings("EnumeratedConstantNamingConvention")
    public enum UserReportColumnNames implements ColumnName {
        firstName("firstName"), lastName("lastName"), dob("dob");

        private final String name;

        UserReportColumnNames(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public AddingToFederationFormObject() {
        super(AdminController.MAX_PAGE_ITEMS, 0, UserReportColumnNames.firstName);
    }

    private String firstName;
    private String lastName;
    private String dob;

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateEmpty(errors, firstName, "firstName", "validation.emptyField");
        ValidatorUtils.validateLength(errors, firstName, "firstName", "validation.maxLength", Globals.MAX_LENGTH);
        ValidatorUtils.validateEmpty(errors, lastName, "lastName", "validation.emptyField");
        ValidatorUtils.validateLength(errors, lastName, "lastName", "validation.maxLength", Globals.MAX_LENGTH);
        ValidatorUtils.validateEmpty(errors, dob, "dob", "validation.emptyField");
        ValidatorUtils.validateDate(errors, dob, "dob", "validation.incorrectField", Globals.getDTF());
    }

    @Override
    protected Class<UserReportColumnNames> getEnumClass() {
        return UserReportColumnNames.class;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
