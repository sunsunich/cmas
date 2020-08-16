package org.cmas.presentation.model.cards;

import org.cmas.Globals;
import org.cmas.entities.cards.CardApprovalRequestStatus;
import org.cmas.presentation.model.ColumnName;
import org.cmas.presentation.model.SortPaginatorImpl;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.hibernate.validator.Length;
import org.springframework.validation.Errors;

/**
 * Created on Sep 30, 2019
 *
 * @author Alexander Petukhov
 */
public class CardApprovalRequestSearchFormObject extends SortPaginatorImpl<CardApprovalRequestSearchFormObject.ColumnNames> implements Validatable {

    @SuppressWarnings("EnumeratedConstantNamingConvention")
    public enum ColumnNames implements ColumnName {
        email("diver.email"), createDate("createDate"), firstName("diver.firstName"), lastName("diver.lastName");

        private final String name;

        ColumnNames(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    @Length(message = "validation.maxLength", max = Globals.MAX_LENGTH)
    private String email;
    @Length(message = "validation.maxLength", max = Globals.MAX_LENGTH)
    private String firstName;
    @Length(message = "validation.maxLength", max = Globals.MAX_LENGTH)
    private String lastName;

    private String status;

    public CardApprovalRequestSearchFormObject() {
        super(ColumnNames.createDate);
        setDir(true);
    }

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateEnum(errors,
                                    status,
                                    CardApprovalRequestStatus.class,
                                    "status",
                                    "validation.incorrectField");
    }

    @Override
    protected Class<ColumnNames> getEnumClass() {
        return ColumnNames.class;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
