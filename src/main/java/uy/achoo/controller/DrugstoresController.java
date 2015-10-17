package uy.achoo.controller;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import uy.achoo.customModel.DrugstoreJPA;
import uy.achoo.database.DBConnector;
import uy.achoo.model.tables.daos.DrugstoreDao;
import uy.achoo.model.tables.pojos.Drugstore;
import uy.achoo.util.GoogleService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static uy.achoo.model.Tables.DRUGSTORE;
import static uy.achoo.model.Tables.PRODUCT;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public class DrugstoresController {
    /**
     * Find all drugstores in the database
     * @return Drugstores found
     * @throws SQLException
     */
    public static List<Drugstore> findAllDrugstores() throws SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            List<Drugstore> drugstores = new DrugstoreDao(configuration).findAll();
            return drugstores;
        } finally {
            connection.close();
        }
    }

    /**
     * Find all drugstores by name
     * @param namePart
     * @return Drugstores with names that contain namePart
     * @throws SQLException
     */
    public static List<DrugstoreJPA> searchDrugstoresByName(String namePart, String clientAdress) throws Exception {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            DSLContext context = DSL.using(configuration);
            List<DrugstoreJPA> drugstores = new ArrayList<>();
            if (namePart != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("%").append(namePart).append("%");
                drugstores = context.selectFrom(DRUGSTORE)
                        .where(DRUGSTORE.NAME.like(stringBuilder.toString())).fetch().into(DrugstoreJPA.class);
                if(clientAdress != null){
                    drugstores = GoogleService.orderDrugstoresByLocation(clientAdress, drugstores);
                }
            }
            return drugstores;
        } finally {
            connection.close();
        }
    }

    /**
     * Find all drugstores that offer a product that has a name that contains productNamePart
     * @param productNamePart
     * @return The list of drugstores with that product.
     * @throws SQLException
     */
    public static List<DrugstoreJPA> searchDrugstoresByProductName(String productNamePart, String clientAddress) throws Exception {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            DSLContext context = DSL.using(configuration);
            List<DrugstoreJPA> drugstores = new ArrayList<>();
            if (productNamePart != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("%").append(productNamePart).append("%");
                drugstores = context.selectDistinct(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_DESCRIPTION, PRODUCT.PRODUCT_UNITARY_PRICE,
                        DRUGSTORE.ID,DRUGSTORE.NAME, DRUGSTORE.PHONE_NUMBER, DRUGSTORE.ADDRESS).
                        from(PRODUCT).join(DRUGSTORE).on(PRODUCT.DRUGSTORE_ID.equal(DRUGSTORE.ID))
                        .where(PRODUCT.PRODUCT_NAME.like(stringBuilder.toString())).fetchInto(DrugstoreJPA.class);
                if(clientAddress != null){
                    drugstores = GoogleService.orderDrugstoresByLocation(clientAddress, drugstores);
                }
            }
            return drugstores;
        } finally {
            connection.close();
        }
    }


    /**
     * Find all drugstores that offer the product with id equal to productId
     * @param productId
     * @return The list of dugstores that offer that product.
     * @throws SQLException
     */
    public static List<Drugstore> searchDrugstoresByProductId(Integer productId) throws SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            DSLContext context = DSL.using(configuration);
            List<Drugstore> drugstores = new ArrayList<>();
            if (productId != null) {
                drugstores = context.selectDistinct(DRUGSTORE.ID,DRUGSTORE.NAME, DRUGSTORE.PHONE_NUMBER, DRUGSTORE.ADDRESS).
                        from(PRODUCT).join(DRUGSTORE).on(PRODUCT.DRUGSTORE_ID.equal(DRUGSTORE.ID))
                        .where(PRODUCT.ID.equal(productId)).fetchInto(Drugstore.class);
            }
            return drugstores;
        } finally {
            connection.close();
        }
    }


}

