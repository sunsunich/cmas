package org.cmas.presentation.model.user.fin;

import org.cmas.presentation.entities.billing.OperationType;
import org.cmas.presentation.model.ColumnName;
import org.cmas.presentation.model.TemporalPaginatorImpl;
import org.cmas.presentation.validator.ValidatorUtils;
import org.cmas.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

public class FinStatsFormObject extends TemporalPaginatorImpl<FinStatsFormObject.FinStatsColumnNames> {

    private static final int MAX_PAGE_ITEMS = 50;

    @SuppressWarnings({"EnumeratedConstantNamingConvention"})
    public enum FinStatsColumnNames implements ColumnName {
          recordDate("recordDate")
        ;
        private String name;

        FinStatsColumnNames(String name){
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

    private String operationType;

    private List<OperationType> operationTypes;

    public FinStatsFormObject(){
        super(FinStatsColumnNames.recordDate);
        setLimit(MAX_PAGE_ITEMS);
        setDir(true);
    }

    @Override
    public void validate(Errors errors) {
        super.validate(errors);
        if (!StringUtil.isTrimmedEmpty(operationType)) {
            for (String operationTypeStr : operationType.split(",")) {
                ValidatorUtils.validateEnum(errors, operationTypeStr, OperationType.class, "operationType", "validation.incorrectField");
                if (errors.hasErrors()) {
                    break;
                }
            }
        }
    }

    @NotNull
    public List<OperationType> getOperationTypes() {
        if(operationTypes == null){
            if (StringUtil.isTrimmedEmpty(operationType)) {
                operationTypes = new ArrayList<OperationType>();
            } else {
                String[] operationTypeStrings = operationType.split(",");
                operationTypes = new ArrayList<OperationType>(operationTypeStrings.length);
                for (String operationTypeStr : operationTypeStrings) {
                    try{
                        OperationType operationTypeEnum = OperationType.valueOf(operationTypeStr);
                        operationTypes.add(operationTypeEnum);
                    }
                    catch (Exception ignored){
                        break;
                    }
                }
            }
        }
        return operationTypes;
    }

    @Override
    @NotNull
    protected Class<FinStatsColumnNames> getEnumClass() {
        return FinStatsColumnNames.class;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
