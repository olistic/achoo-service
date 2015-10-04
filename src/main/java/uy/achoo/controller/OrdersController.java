package uy.achoo.controller;

import org.jooq.BatchBindStep;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import uy.achoo.Wrappers.OrderAndOrderLinesWrapper;
import uy.achoo.database.DBConnector;
import uy.achoo.model.tables.daos.OrderDao;
import uy.achoo.model.tables.daos.OrderLineDao;
import uy.achoo.model.tables.pojos.Order;
import uy.achoo.model.tables.pojos.OrderLine;
import uy.achoo.model.tables.records.OrderRecord;

import java.sql.Connection;
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

    public static Order createOrder(Order order, List<OrderLine> orderLines) throws SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            DSLContext context = DSL.using(configuration);
            OrderRecord insertedOrder = context.insertInto(ORDER, ORDER.DRUGSTORE_ID, ORDER.USER_ID, ORDER.DATE, ORDER.SCORE)
                    .values(order.getDrugstoreId(), order.getUserId(), order.getDate(), order.getScore())
                    .returning(ORDER.ID).fetchOne();
            order.setId(insertedOrder.getId());
            BatchBindStep batchInsert = context.batch(context
                    .insertInto(ORDER_LINE, ORDER_LINE.ORDER_ID, ORDER_LINE.PRODUCT_ID, ORDER_LINE.AMOUNT)
                    .values((Integer) null, null, null));
            for (OrderLine line : orderLines) {
                line.setOrderId(insertedOrder.getId());
                batchInsert.bind(line.getOrderId(), line.getProductId(), line.getAmount());
            }
            batchInsert.execute();
            return order;
        } finally {
            connection.close();
        }
    }

    public static Integer rateOrder(Integer orderId, Integer score) throws SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            DSLContext context = DSL.using(configuration);
            context.update(ORDER).set(ORDER.SCORE, score).execute();
            return score;
        } finally {
            connection.close();
        }
    }

    public static OrderAndOrderLinesWrapper readOrder(Integer orderId) throws SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            Order order = new OrderDao(configuration).fetchOneById(orderId);
            List<OrderLine> orderLines = new OrderLineDao(configuration).fetchByOrderId(orderId);
            return new OrderAndOrderLinesWrapper(order, orderLines);
        } finally {
            // Close the database connection
            connection.close();
        }
    }

    public static List<OrderAndOrderLinesWrapper> findAllOrdersOfUser(Integer userId) throws SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            List<OrderAndOrderLinesWrapper> ordersAndOrderLines = new ArrayList<>();
            List<Order> orders = new OrderDao(configuration).fetchByUserId(userId);
            OrderAndOrderLinesWrapper orderAndLines = null;
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
            connection.close();
        }
    }

    public static List<OrderAndOrderLinesWrapper> findAllOrdersOfDrugStore(Integer drugstoreId) throws SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            List<OrderAndOrderLinesWrapper> ordersAndOrderLines = new ArrayList<>();
            List<Order> orders = new OrderDao(configuration).fetchByDrugstoreId(drugstoreId);
            OrderAndOrderLinesWrapper orderAndLines = null;
            List<OrderLine> orderLines;
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
            connection.close();
        }
    }
}
