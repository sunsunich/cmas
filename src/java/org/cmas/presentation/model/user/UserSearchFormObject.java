package org.cmas.presentation.model.user;

import org.cmas.presentation.model.ColumnName;
import org.cmas.presentation.model.SortPaginatorImpl;


@SuppressWarnings("InnerClassFieldHidesOuterClassField")
public class UserSearchFormObject extends SortPaginatorImpl<UserSearchFormObject.UserReportColumnNames> {

    @SuppressWarnings("EnumeratedConstantNamingConvention")
    public enum UserReportColumnNames implements ColumnName {
          email("email")
        , dateReg("dateReg")
        , lastAction("lastAction")
        , firstName("firstName")
        , lastName("lastName")
        ;

        private final String name;

        UserReportColumnNames(String name){
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    // условие отбора
    private String email;
    private String firstName;
    private String lastName;
    private String countryCode;
    private String userRole;

    public UserSearchFormObject() {
        super(UserReportColumnNames.email);       
    }

    @Override
    protected Class<UserReportColumnNames> getEnumClass() {
        return UserReportColumnNames.class;
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
}
