package uy.achoo.controller;

import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import uy.achoo.customModel.CustomOrder;
import uy.achoo.customModel.CustomOrderLine;
import uy.achoo.database.DBConnector;
import uy.achoo.model.tables.daos.UserDao;
import uy.achoo.model.tables.pojos.User;
import uy.achoo.model.tables.records.OrderRecord;
import uy.achoo.util.EmailService;

import javax.mail.MessagingException;
import java.sql.Connection;
import java.sql.SQLException;
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
     * @return The inserted order
     * @throws SQLException
     */
    public static CustomOrder createOrder(CustomOrder order) throws SQLException, MessagingException {
        DBConnector connector = DBConnector.getInstance();
        Connection connection = connector.createConnection();
        OrderRecord insertedOrder = null;
        try {
            DSLContext context = connector.getContext(connection);
            User orderUser = new UserDao(connector.getConfiguration(connection)).fetchOneById(order.getUserId());
            if (orderUser != null) {
                insertedOrder = insertOrder(order, context);
                insertOrderLines(order.getOrderLines(), context, insertedOrder);
                EmailService.sendSuccessfulOrderMail(orderUser.getEmail());
            }
        } finally {
            connector.closeConnection(connection);
        }
        return insertedOrder != null ? readOrder(insertedOrder.getId()) : null;
    }

    private static OrderRecord insertOrder(CustomOrder order, DSLContext context) {
        return context.insertInto(ORDER, ORDER.PHARMACY_ID, ORDER.USER_ID, ORDER.DATE, ORDER.SCORE)
                .values(order.getPharmacyId(), order.getUserId(), order.getDate(), order.getScore())
                .returning(ORDER.ID).fetchOne();
    }

    private static void insertOrderLines(List<CustomOrderLine> orderLines, DSLContext context, OrderRecord insertedOrder) {
        BatchBindStep batchInsert = context.batch(context
                .insertInto(ORDER_LINE, ORDER_LINE.ORDER_ID, ORDER_LINE.PRODUCT_ID, ORDER_LINE.QUANTITY)
                .values((Integer) null, null, null));
        for (CustomOrderLine orderLine : orderLines) {
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
    public static Integer rateOrder(Integer orderId, Integer score, Integer userId) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        Connection connection = connector.createConnection();
        try {
            connector.getContext(connection).update(ORDER).set(ORDER.SCORE, score).where(ORDER.ID.equal(orderId)
                    .and(ORDER.USER_ID.eq(userId))).execute();
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
    public static CustomOrder readOrder(Integer orderId) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        Connection connection = connector.createConnection();
        try {
            CustomOrder order = connector.getContext(connection).selectDistinct(ORDER.ID, ORDER.PHARMACY_ID,
                    ORDER.USER_ID, ORDER.DATE, ORDER.SCORE, PHARMACY.NAME, PHARMACY.IMAGE_URL).
                    from(ORDER).join(PHARMACY).on(ORDER.PHARMACY_ID.equal(PHARMACY.ID)).where(ORDER.ID.equal(orderId))
                    .fetchOneInto(CustomOrder.class);
            // TODO : find a way to do it in a single query due to performance issues.
            List<CustomOrderLine> orderLines = connector.getContext(connection).selectDistinct(ORDER_LINE.ORDER_ID, ORDER_LINE.PRODUCT_ID,
                    ORDER_LINE.QUANTITY, PRODUCT.UNITARY_PRICE, PRODUCT.NAME).
                    from(ORDER_LINE).join(PRODUCT).on(ORDER_LINE.PRODUCT_ID.equal(PRODUCT.ID))
                    .where(ORDER_LINE.ORDER_ID.equal(order.getId()))
                    .fetchInto(CustomOrderLine.class);
            order.setOrderLines(orderLines);
            return order;
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
    public static List<CustomOrder> findAllOrdersOfUser(Integer userId) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        Connection connection = connector.createConnection();
        try {
            List<CustomOrder> orders = connector.getContext(connection).selectDistinct(ORDER.ID, ORDER.PHARMACY_ID,
                    ORDER.USER_ID, ORDER.DATE, ORDER.SCORE, PHARMACY.NAME, PHARMACY.IMAGE_URL).
                    from(ORDER).join(PHARMACY).on(ORDER.PHARMACY_ID.equal(PHARMACY.ID)).where(ORDER.USER_ID.equal(userId))
                    .fetchInto(CustomOrder.class);
            List<CustomOrderLine> orderLines;
            // TODO : find a way to do it in a single query due to performance issues.
            for (CustomOrder order : orders) {
                orderLines = connector.getContext(connection).selectDistinct(ORDER_LINE.ORDER_ID, ORDER_LINE.PRODUCT_ID,
                        ORDER_LINE.QUANTITY, PRODUCT.UNITARY_PRICE, PRODUCT.NAME).
                        from(ORDER_LINE).join(PRODUCT).on(ORDER_LINE.PRODUCT_ID.equal(PRODUCT.ID))
                        .where(ORDER_LINE.ORDER_ID.equal(order.getId()))
                        .fetchInto(CustomOrderLine.class);
                order.setOrderLines(orderLines);
            }
            return orders;
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
    public static List<CustomOrder> findAllOrdersOfPharmacy(Integer pharmacyId) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        Connection connection = connector.createConnection();
        try {
            List<CustomOrder> orders = connector.getContext(connection).selectDistinct(ORDER.ID, ORDER.PHARMACY_ID,
                    ORDER.USER_ID, ORDER.DATE, ORDER.SCORE, PHARMACY.NAME, PHARMACY.IMAGE_URL).
                    from(ORDER).join(PHARMACY).on(ORDER.PHARMACY_ID.equal(PHARMACY.ID)).where(ORDER.PHARMACY_ID.equal(pharmacyId))
                    .fetchInto(CustomOrder.class);
            List<CustomOrderLine> customOrderLines;
            // TODO : find a way to do it in a single query due to performance issues.
            for (CustomOrder order : orders) {
                customOrderLines = connector.getContext(connection).selectDistinct(ORDER_LINE.ORDER_ID, ORDER_LINE.PRODUCT_ID,
                        ORDER_LINE.QUANTITY, PRODUCT.UNITARY_PRICE, PRODUCT.NAME).
                        from(ORDER_LINE).join(PRODUCT).on(ORDER_LINE.PRODUCT_ID.equal(PRODUCT.ID))
                        .where(ORDER_LINE.ORDER_ID.equal(order.getId()))
                        .fetchInto(CustomOrderLine.class);
                order.setOrderLines(customOrderLines);
            }
            return orders;
        } finally {
            // Close the database connection
            connector.closeConnection(connection);
        }
    }
}
