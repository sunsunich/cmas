package org.cmas.presentation.model.user;

import org.cmas.entities.Role;
import org.cmas.entities.diver.DiverType;
import org.cmas.presentation.model.ColumnName;
import org.cmas.presentation.model.SortPaginatorImpl;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.springframework.validation.Errors;


@SuppressWarnings("InnerClassFieldHidesOuterClassField")
public class UserSearchFormObject extends SortPaginatorImpl<UserSearchFormObject.UserReportColumnNames> implements Validatable {

    @SuppressWarnings("EnumeratedConstantNamingConvention")
    public enum UserReportColumnNames implements ColumnName {
        email("email"), dateReg("dateReg"), lastAction("lastAction"), firstName("firstName"), lastName("lastName");

        private final String name;

        UserReportColumnNames(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private String email;
    private String firstName;
    private String lastName;
    private String countryCode;
    private String userRole;
    private String diverType;
    private String federationId;

    public UserSearchFormObject() {
        super(UserReportColumnNames.email);
    }

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateEnum(errors, userRole, Role.class, "userRole", "validation.incorrectField");
        ValidatorUtils.validateEnum(errors, diverType, DiverType.class, "diverType", "validation.incorrectField");
    }

    @Override
    protected Class<UserReportColumnNames> getEnumClass() {
        return UserReportColumnNames.class;
    }

    public String getDiverType() {
        return diverType;
    }

    public void setDiverType(String diverType) {
        this.diverType = diverType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFederationId() {
        return federationId;
    }

    public void setFederationId(String federationId) {
        this.federationId = federationId;
    }
}
