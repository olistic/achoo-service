package uy.achoo.Wrappers;


import uy.achoo.model.tables.pojos.Order;
import uy.achoo.model.tables.pojos.OrderLine;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alfredo on 04/10/15.
 */
public class OrderAndOrderLinesWrapper implements Serializable{
    private Order order;
    private List<OrderLine> orderLines;

    public OrderAndOrderLinesWrapper(){}

    public OrderAndOrderLinesWrapper(Order order, List<OrderLine> orderLines) {
        this.order = order;
        this.orderLines = orderLines;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }
}
