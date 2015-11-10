package uy.achoo.controller;

import uy.achoo.database.DBConnector;
import uy.achoo.model.tables.daos.ProductDao;
import uy.achoo.model.tables.pojos.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static uy.achoo.model.Tables.PRODUCT;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public class ProductsController {
    /**
     * Read a product from the database
     *
     * @param productId
     * @return The read product
     * @throws SQLException
     */
    public static Product readProduct(int productId) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        Connection connection = connector.createConnection();
        try {
            return new ProductDao(connector.getConfiguration(connection)).findById(productId);
        } finally {
            connector.closeConnection(connection);
        }
    }

    /**
     * Find all the products of a pharmacy
     *
     * @param pharmacyId
     * @return The products of the pharmacy
     * @throws SQLException
     */
    public static List<Product> searchAllProductsOfDrugstor(int pharmacyId) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        Connection connection = connector.createConnection();
        try {
            return new ProductDao(connector.getConfiguration(connection)).fetchByPharmacyId(pharmacyId);
        } finally {
            connector.closeConnection(connection);
        }
    }

    /**
     * Lists all the products
     *
     * @return The products of the pharmacy
     * @throws SQLException
     */
    public static List<Product> listAllProducts() throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        Connection connection = connector.createConnection();
        try {
            return new ProductDao(connector.getConfiguration(connection)).findAll();
        } finally {
            connector.closeConnection(connection);
        }
    }

    /**
     * Find all products that have a name that contains namePart
     *
     * @param namePart
     * @return The list of products whith namePart in their name
     * @throws SQLException
     */
    public static List<Product> searchProductsByName(String namePart) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        Connection connection = connector.createConnection();
        try {
            List<Product> products = new ArrayList<>();
            if (namePart != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("%").append(namePart).append("%");
                products = connector.getContext(connection).selectFrom(PRODUCT)
                        .where(PRODUCT.NAME.like(sb.toString())).fetchInto(Product.class);
            }
            return products;
        } finally {
            connector.closeConnection(connection);
        }
    }
}
