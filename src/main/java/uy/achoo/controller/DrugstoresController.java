package uy.achoo.controller;

import uy.achoo.customModel.DrugstoreJPA;
import uy.achoo.database.DBConnector;
import uy.achoo.model.tables.daos.DrugstoreDao;
import uy.achoo.model.tables.pojos.Drugstore;
import uy.achoo.util.GoogleService;

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
     *
     * @return Drugstores found
     * @throws SQLException
     */
    public static List<Drugstore> findAllDrugstores() throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        try {
            List<Drugstore> drugstores = new DrugstoreDao(connector.getConfiguration()).findAll();
            return drugstores;
        } finally {
            connector.closeConnection();
        }
    }

    /**
     * Find all drugstores by name
     *
     * @param namePart
     * @return Drugstores with names that contain namePart
     * @throws SQLException
     */
    public static List<DrugstoreJPA> searchDrugstoresByName(String namePart, Double latitude, Double longitude) throws Exception {
        DBConnector connector = DBConnector.getInstance();
        try {
            List<DrugstoreJPA> drugstores = new ArrayList<>();
            if (namePart != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("%").append(namePart).append("%");
                drugstores = connector.getContext().selectFrom(DRUGSTORE)
                        .where(DRUGSTORE.NAME.like(stringBuilder.toString())).fetch().into(DrugstoreJPA.class);
                if (latitude != null && longitude != null) {
                    drugstores = GoogleService.orderDrugstoresByLocation(latitude, longitude, drugstores);
                }
            }
            return drugstores;
        } finally {
            connector.closeConnection();
        }
    }

    /**
     * Find all drugstores that offer a product that has a name that contains productNamePart
     *
     * @param productNamePart
     * @return The list of drugstores with that product.
     * @throws SQLException
     */
    public static List<DrugstoreJPA> searchDrugstoresByProductName(String productNamePart, Double latitude, Double longitude) throws Exception {
        DBConnector connector = DBConnector.getInstance();
        try {
            List<DrugstoreJPA> drugstores = new ArrayList<>();
            if (productNamePart != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("%").append(productNamePart).append("%");
                drugstores = connector.getContext().selectDistinct(PRODUCT.PRODUCT_ID,
                        PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_DESCRIPTION, PRODUCT.PRODUCT_UNITARY_PRICE,
                        PRODUCT.PRODUCT_IMAGE_URL, DRUGSTORE.ID, DRUGSTORE.NAME, DRUGSTORE.PHONE_NUMBER,
                        DRUGSTORE.ADDRESS).from(PRODUCT).join(DRUGSTORE).on(PRODUCT.DRUGSTORE_ID.equal(DRUGSTORE.ID))
                        .where(PRODUCT.PRODUCT_NAME.like(stringBuilder.toString())).fetchInto(DrugstoreJPA.class);
                if (latitude != null && longitude!= null) {
                    drugstores = GoogleService.orderDrugstoresByLocation(latitude, longitude, drugstores);
                }
            }
            return drugstores;
        } finally {
            connector.closeConnection();
        }
    }


    /**
     * Find all drugstores that offer the product with id equal to productId
     *
     * @param productId
     * @return The list of dugstores that offer that product.
     * @throws SQLException
     */
    public static List<Drugstore> searchDrugstoresByProductId(Integer productId) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        try {
            List<Drugstore> drugstores = new ArrayList<>();
            if (productId != null) {
                drugstores = connector.getContext().selectDistinct(DRUGSTORE.ID, DRUGSTORE.NAME, DRUGSTORE.PHONE_NUMBER, DRUGSTORE.ADDRESS).
                        from(PRODUCT).join(DRUGSTORE).on(PRODUCT.DRUGSTORE_ID.equal(DRUGSTORE.ID))
                        .where(PRODUCT.PRODUCT_ID.equal(productId)).fetchInto(Drugstore.class);
            }
            return drugstores;
        } finally {
            connector.closeConnection();
        }
    }


}

