package org.cmas.presentation.model.user;

public class NavigationStatisticsFormObject {

    private int chosenItemsCnt;

    private int cartSize;

    private int orderCnt;

    public NavigationStatisticsFormObject(int chosenItemsCnt, int cartSize, int orderCnt) {
        this.chosenItemsCnt = chosenItemsCnt;
        this.cartSize = cartSize;
        this.orderCnt = orderCnt;
    }

    public int getChosenItemsCnt() {
        return chosenItemsCnt;
    }

    public void setChosenItemsCnt(int chosenItemsCnt) {
        this.chosenItemsCnt = chosenItemsCnt;
    }

    public int getCartSize() {
        return cartSize;
    }

    public void setCartSize(int cartSize) {
        this.cartSize = cartSize;
    }

    public int getOrderCnt() {
        return orderCnt;
    }

    public void setOrderCnt(int orderCnt) {
        this.orderCnt = orderCnt;
    }
}
