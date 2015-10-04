package uy.achoo.Wrappers;


import uy.achoo.model.tables.pojos.Order;
import uy.achoo.model.tables.pojos.OrderLine;

import java.io.Serializable;
import java.util.List;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 *
 * Auxiliary class to wrap an order with its order lines
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
