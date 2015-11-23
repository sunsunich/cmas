package org.cmas.presentation.model.user;

import org.cmas.presentation.model.ColumnName;
import org.cmas.presentation.model.SortPaginatorImpl;


@SuppressWarnings({"InnerClassFieldHidesOuterClassField"})
public class UserSearchFormObject extends SortPaginatorImpl<UserSearchFormObject.UserReportColumnNames> {

    @SuppressWarnings({"EnumeratedConstantNamingConvention"})
    public enum UserReportColumnNames implements ColumnName {
          email("contactInfo.email")
        , dateReg("dateReg");

        private String name;

        UserReportColumnNames(String name){
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    // условие отбора
    private String email;
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
}
