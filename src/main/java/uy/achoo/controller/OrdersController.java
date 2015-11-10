package uy.achoo.controller;

import org.jooq.BatchBindStep;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import uy.achoo.customModel.CustomOrder;
import uy.achoo.customModel.CustomOrderAndOrderLines;
import uy.achoo.customModel.CustomOrderLine;
import uy.achoo.database.DBConnector;
import uy.achoo.model.tables.daos.OrderDao;
import uy.achoo.model.tables.daos.OrderLineDao;
import uy.achoo.model.tables.daos.UserDao;
import uy.achoo.model.tables.pojos.Order;
import uy.achoo.model.tables.pojos.OrderLine;
import uy.achoo.model.tables.pojos.User;
import uy.achoo.model.tables.records.OrderRecord;
import uy.achoo.util.EmailService;
import uy.achoo.wrappers.OrderAndOrderLinesWrapper;

import javax.mail.MessagingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static uy.achoo.model.Tables.*;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public class OrdersController {
    /**
     * Create a new order in the database
     *
     * @param order
     * @param orderLines
     * @return The inserted order
     * @throws SQLException
     */
    public static Order createOrder(Order order, List<OrderLine> orderLines) throws SQLException, MessagingException {
        DBConnector connector = DBConnector.getInstance();
        Connection connection = connector.createConnection();
        try {
            DSLContext context = connector.getContext(connection);
            User orderUser = new UserDao(connector.getConfiguration(connection)).fetchOneById(order.getUserId());
            if (orderUser != null) {
                OrderRecord insertedOrder = insertOrder(order, context);
                order.setId(insertedOrder.getId());
                insertOrderLines(orderLines, context, insertedOrder);
                EmailService.sendSuccessfulOrderMail(orderUser.getEmail());
                return order;
            }
            return null;
        } finally {
            connector.closeConnection(connection);
        }
    }

    private static OrderRecord insertOrder(Order order, DSLContext context) {
        return context.insertInto(ORDER, ORDER.PHARMACY_ID, ORDER.USER_ID, ORDER.DATE, ORDER.SCORE)
                .values(order.getPharmacyId(), order.getUserId(), order.getDate(), order.getScore())
                .returning(ORDER.ID).fetchOne();
    }

    private static void insertOrderLines(List<OrderLine> orderLines, DSLContext context, OrderRecord insertedOrder) {
        BatchBindStep batchInsert = context.batch(context
                .insertInto(ORDER_LINE, ORDER_LINE.ORDER_ID, ORDER_LINE.PRODUCT_ID, ORDER_LINE.QUANTITY)
                .values((Integer) null, null, null));
        for (OrderLine orderLine : orderLines) {
            orderLine.setOrderId(insertedOrder.getId());
            batchInsert.bind(orderLine.getOrderId(), orderLine.getProductId(), orderLine.getQuantity());
        }
        batchInsert.execute();
    }

    /**
     * Change the score of a Order
     *
     * @param orderId
     * @param score
     * @return The orders new score
     * @throws SQLException
     */
    public static Integer rateOrder(Integer orderId, Integer score) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        Connection connection = connector.createConnection();
        try {
            connector.getContext(connection).update(ORDER).set(ORDER.SCORE, score).where(ORDER.ID.equal(orderId)).execute();
            return score;
        } finally {
            connector.closeConnection(connection);
        }
    }

    /**
     * Read an order from the database
     *
     * @param orderId
     * @return The read order
     * @throws SQLException
     */
    public static OrderAndOrderLinesWrapper readOrder(Integer orderId) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        Connection connection = connector.createConnection();
        try {
            Configuration configuration = connector.getConfiguration(connection);
            Order order = new OrderDao(configuration).fetchOneById(orderId);
            List<OrderLine> orderLines = new OrderLineDao(configuration).fetchByOrderId(orderId);
            return new OrderAndOrderLinesWrapper(order, orderLines);
        } finally {
            // Close the database connection
            connector.closeConnection(connection);
        }
    }

    /**
     * Find all orders made by a user
     *
     * @param userId
     * @return The orders made by the user
     * @throws SQLException
     */
    public static List<CustomOrderAndOrderLines> findAllOrdersOfUser(Integer userId) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        Connection connection = connector.createConnection();
        try {
            List<CustomOrderAndOrderLines> ordersAndOrderLines = new ArrayList<>();
            List<CustomOrder> orders = connector.getContext(connection).selectDistinct(ORDER.ID, ORDER.PHARMACY_ID,
                    ORDER.USER_ID, ORDER.DATE, ORDER.SCORE, PHARMACY.NAME).
                    from(ORDER).join(PHARMACY).on(ORDER.PHARMACY_ID.equal(PHARMACY.ID)).where(ORDER.USER_ID.equal(userId))
                    .fetchInto(CustomOrder.class);
            CustomOrderAndOrderLines orderAndLines;
            List<CustomOrderLine> customOrderLines;
            // TODO : find a way to do it in a single query due to performance issues.
            for (CustomOrder order : orders) {
                customOrderLines = connector.getContext(connection).selectDistinct(ORDER_LINE.ORDER_ID, ORDER_LINE.PRODUCT_ID,
                        ORDER_LINE.QUANTITY, PRODUCT.UNITARY_PRICE, PRODUCT.NAME).
                        from(ORDER_LINE).join(PRODUCT).on(ORDER_LINE.PRODUCT_ID.equal(PRODUCT.ID))
                        .where(ORDER_LINE.ORDER_ID.equal(order.getId()))
                        .fetchInto(CustomOrderLine.class);
                orderAndLines = new CustomOrderAndOrderLines();
                orderAndLines.setOrder(order);
                orderAndLines.setOrderLines(customOrderLines);
                ordersAndOrderLines.add(orderAndLines);
            }
            return ordersAndOrderLines;
        } finally {
            // Close the database connection
            connector.closeConnection(connection);
        }
    }

    /**
     * Find all orders made to a pharmacy
     *
     * @param pharmacyId
     * @return The orders made to the pharmacy
     * @throws SQLException
     */
    public static List<CustomOrderAndOrderLines> findAllOrdersOfPharmacy(Integer pharmacyId) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        Connection connection = connector.createConnection();
        try {
            List<CustomOrderAndOrderLines> ordersAndOrderLines = new ArrayList<>();
            List<CustomOrder> orders = connector.getContext(connection).selectDistinct(ORDER.ID, ORDER.PHARMACY_ID,
                    ORDER.USER_ID, ORDER.DATE, ORDER.SCORE, PHARMACY.NAME).
                    from(ORDER).join(PHARMACY).on(ORDER.PHARMACY_ID.equal(PHARMACY.ID)).where(ORDER.PHARMACY_ID.equal(pharmacyId))
                    .fetchInto(CustomOrder.class);
            CustomOrderAndOrderLines orderAndLines;
            List<CustomOrderLine> customOrderLines;
            // TODO : find a way to do it in a single query due to performance issues.
            for (CustomOrder order : orders) {
                customOrderLines = connector.getContext(connection).selectDistinct(ORDER_LINE.ORDER_ID, ORDER_LINE.PRODUCT_ID,
                        ORDER_LINE.QUANTITY, PRODUCT.UNITARY_PRICE, PRODUCT.NAME).
                        from(ORDER_LINE).join(PRODUCT).on(ORDER_LINE.PRODUCT_ID.equal(PRODUCT.ID))
                        .where(ORDER_LINE.ORDER_ID.equal(order.getId()))
                        .fetchInto(CustomOrderLine.class);
                orderAndLines = new CustomOrderAndOrderLines();
                orderAndLines.setOrder(order);
                orderAndLines.setOrderLines(customOrderLines);
                ordersAndOrderLines.add(orderAndLines);
            }
            return ordersAndOrderLines;
        } finally {
            // Close the database connection
            connector.closeConnection(connection);
        }
    }
}
