package uy.achoo.controller;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import uy.achoo.database.DBConnector;
import uy.achoo.model.tables.daos.ProductDao;
import uy.achoo.model.tables.pojos.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public class ProductsController {
    /**
     * Read a product from the database
     * @param productId
     * @return The read product
     * @throws SQLException
     */
    public static Product readProduct(int productId) throws SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            return new ProductDao(configuration).findById(productId);
        } finally {
            connection.close();
        }
    }

    /**
     * Find all the products of a drugstore
     * @param durgstoreId
     * @return The products of the drugstore
     * @throws SQLException
     */
    public static List<Product> findAllProductsOfDrugstor(int durgstoreId) throws SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            return new ProductDao(configuration).fetchByDrugstoreId(durgstoreId);
        } finally {
            connection.close();
        }
    }

    /**
     * Lists all the products
     * @return The products of the drugstore
     * @throws SQLException
     */
    public static List<Product> listAllProducts() throws SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            return new ProductDao(configuration).findAll();
        } finally {
            connection.close();
        }
    }
}
