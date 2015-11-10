package uy.achoo.controller;

import org.jooq.BatchBindStep;
import org.jooq.Configuration;
import org.jooq.DSLContext;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static uy.achoo.model.Tables.ORDER;
import static uy.achoo.model.Tables.ORDER_LINE;

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
        try {
            DSLContext context = connector.getContext();
            User orderUser = new UserDao(connector.getConfiguration()).fetchOneById(order.getUserId());
            if (orderUser != null) {
                OrderRecord insertedOrder = insertOrder(order, context);
                order.setId(insertedOrder.getId());
                insertOrderLines(orderLines, context, insertedOrder);
                EmailService.sendSuccessfulOrderMail(orderUser.getEmail());
                return order;
            }
            return null;
        } finally {
            connector.closeConnection();
        }
    }

    private static OrderRecord insertOrder(Order order, DSLContext context) {
        return context.insertInto(ORDER, ORDER.PHARMACY_ID, ORDER.USER_ID, ORDER.DATE, ORDER.SCORE)
                .values(order.getPharmacyId(), order.getUserId(), order.getDate(), order.getScore())
                .returning(ORDER.ID).fetchOne();
    }

    private static void insertOrderLines(List<OrderLine> orderLines, DSLContext context, OrderRecord insertedOrder) {
        BatchBindStep batchInsert = context.batch(context
                .insertInto(ORDER_LINE, ORDER_LINE.ORDER_ID, ORDER_LINE.PRODUCT_ID, ORDER_LINE.AMOUNT)
                .values((Integer) null, null, null));
        for (OrderLine line : orderLines) {
            line.setOrderId(insertedOrder.getId());
            batchInsert.bind(line.getOrderId(), line.getProductId(), line.getAmount());
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
        try {
            connector.getContext().update(ORDER).set(ORDER.SCORE, score).where(ORDER.ID.equal(orderId)).execute();
            return score;
        } finally {
            connector.closeConnection();
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
        try {
            Configuration configuration = connector.getConfiguration();
            Order order = new OrderDao(configuration).fetchOneById(orderId);
            List<OrderLine> orderLines = new OrderLineDao(configuration).fetchByOrderId(orderId);
            return new OrderAndOrderLinesWrapper(order, orderLines);
        } finally {
            // Close the database connection
            connector.closeConnection();
        }
    }

    /**
     * Find all orders made by a user
     *
     * @param userId
     * @return The orders made by the user
     * @throws SQLException
     */
    public static List<OrderAndOrderLinesWrapper> findAllOrdersOfUser(Integer userId) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        try {
            Configuration configuration = connector.getConfiguration();
            List<OrderAndOrderLinesWrapper> ordersAndOrderLines = new ArrayList<>();
            List<Order> orders = new OrderDao(configuration).fetchByUserId(userId);
            OrderAndOrderLinesWrapper orderAndLines;
            OrderLineDao orderLineDao = new OrderLineDao(configuration);
            // TODO : find a way to do it in a single query due to performance issues.
            for (Order order : orders) {
                orderAndLines = new OrderAndOrderLinesWrapper();
                orderAndLines.setOrder(order);
                orderAndLines.setOrderLines(orderLineDao.fetchByOrderId(order.getId()));
                ordersAndOrderLines.add(orderAndLines);
            }
            return ordersAndOrderLines;
        } finally {
            // Close the database connection
            connector.closeConnection();
        }
    }

    /**
     * Find all orders made to a pharmacy
     *
     * @param pharmacyId
     * @return The orders made to the pharmacy
     * @throws SQLException
     */
    public static List<OrderAndOrderLinesWrapper> findAllOrdersOfDrugStore(Integer pharmacyId) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        try {
            Configuration configuration = connector.getConfiguration();
            List<OrderAndOrderLinesWrapper> ordersAndOrderLines = new ArrayList<>();
            List<Order> orders = new OrderDao(configuration).fetchByPharmacyId(pharmacyId);
            OrderAndOrderLinesWrapper orderAndLines;
            OrderLineDao orderLineDao = new OrderLineDao(configuration);
            // TODO : find a way to do it in a single query due to performance issues.
            for (Order order : orders) {
                orderAndLines = new OrderAndOrderLinesWrapper();
                orderAndLines.setOrder(order);
                orderAndLines.setOrderLines(orderLineDao.fetchByOrderId(order.getId()));
                ordersAndOrderLines.add(orderAndLines);
            }
            return ordersAndOrderLines;
        } finally {
            // Close the database connection
            connector.closeConnection();
        }
    }
}
