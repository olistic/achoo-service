package uy.achoo.customModel;


import java.io.Serializable;
import java.util.List;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 *         <p/>
 *         Auxiliary class to wrap an order with its order lines
 */
public class CustomOrderAndOrderLines implements Serializable {
    private CustomOrder order;
    private List<CustomOrderLine> orderLines;

    public CustomOrderAndOrderLines() {
    }

    public CustomOrderAndOrderLines(CustomOrder order, List<CustomOrderLine> orderLines) {
        this.order = order;
        this.orderLines = orderLines;
    }

    public CustomOrder getOrder() {
        return order;
    }

    public void setOrder(CustomOrder order) {
        this.order = order;
    }

    public List<CustomOrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<CustomOrderLine> orderLines) {
        this.orderLines = orderLines;
    }
}
