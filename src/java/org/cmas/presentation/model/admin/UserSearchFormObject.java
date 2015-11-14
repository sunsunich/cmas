package org.cmas.presentation.model.admin;

import org.cmas.presentation.model.ColumnName;
import org.cmas.presentation.model.SortPaginatorImpl;


@SuppressWarnings({"InnerClassFieldHidesOuterClassField"})
public class UserSearchFormObject extends SortPaginatorImpl<UserSearchFormObject.UserReportColumnNames> {

    @SuppressWarnings({"EnumeratedConstantNamingConvention"})
    public enum UserReportColumnNames implements ColumnName {
          email("contactInfo.email")
        , dateReg("dateReg")
        , shopName("shopName");
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
    private String shopName;


    public UserSearchFormObject() {
        super(UserReportColumnNames.email);
        //по умолчанию сортируем по логину        
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

}
