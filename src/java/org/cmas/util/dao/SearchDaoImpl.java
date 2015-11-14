package org.cmas.util.dao;

import org.cmas.presentation.model.ColumnName;
import org.cmas.presentation.model.Paginator;
import org.cmas.presentation.model.SortPaginator;
import org.cmas.util.text.StringUtil;
import org.hibernate.Criteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class SearchDaoImpl<T> extends IdGeneratingDaoImpl<T>{

    @Transactional
    protected List<T> search(Criteria crit, SortPaginator form) {
        boolean dir = form.isDir();
        String sort = form.getSortColumnName();
        Order order = getOrder(sort, dir);
        //noinspection unchecked
        return crit.addOrder(order).setFirstResult(form.getOffset())
                .setMaxResults(form.getLimit()).setCacheable(true).list();
    }

    @Transactional
    protected List<T> search(Criteria crit, Paginator form) {
        //noinspection unchecked
        return crit.setFirstResult(form.getOffset())
                .setMaxResults(form.getLimit()).setCacheable(true).list();
    }

    @Transactional
    protected int getMaxCountSearch(Criteria crit) {
        crit.setProjection(Projections.rowCount()).setCacheable(true);
        return (Integer) crit.uniqueResult();
    }

    protected Order getOrder(String name, boolean dir) {
        if (dir) {
            return Order.desc(name);
        }
        return Order.asc(name);
    }

    protected void addStringValueToSearchCriteria(Criteria crit, @Nullable String value, ColumnName column) {
        addStringValueToSearchCriteria(crit, value, column.getName());
    }

    protected void addStringValueToSearchCriteria(Criteria crit, @Nullable String value, String columnName) {
        if (!StringUtil.isTrimmedEmpty(value)) {
            //noinspection ConstantConditions
            crit.add(Restrictions.like(columnName, value.trim(), MatchMode.ANYWHERE));
        }
    }

     protected void addDateRangeToSearchCriteria(Criteria crit
                                                , @Nullable String fromValue, @Nullable String toValue
                                                , String columnName
                                                , SimpleDateFormat simpleDateFormat
     ) {
         try {
             if (!StringUtil.isTrimmedEmpty(fromValue)) {
                 Date value = simpleDateFormat.parse(fromValue);
                 crit.add(Restrictions.ge(columnName, value));
             }
             if (!StringUtil.isTrimmedEmpty(toValue)) {
                 Date value = simpleDateFormat.parse(toValue);
                 crit.add(Restrictions.le(columnName, value));
             }
         } catch (ParseException ignored) {
         }
    }

    protected void addBooleanToHql(StringBuilder hql, boolean value, String property) {
        if(value){
            hql.append(" or ")
               .append(property)
               .append("=true");
        }
    }

    protected void addBooleanToJunction(Junction junction, boolean value, String property) {        
        if(value){
            junction.add(Restrictions.eq(property,true));
        }
    }
}
