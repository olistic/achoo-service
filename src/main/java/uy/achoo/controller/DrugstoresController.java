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
     * Find all drugstores that offer a product that has a name that contains namePart or a name that contains namePart
     *
     * @param namePart
     * @return The list of drugstores with that product.
     * @throws SQLException
     */
    public static List<DrugstoreJPA> searchDrugstoresByNameOrProductName(String namePart, Double latitude, Double longitude) throws Exception {
        DBConnector connector = DBConnector.getInstance();
        try {
            List<DrugstoreJPA> drugstores = new ArrayList<>();
            if (namePart != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("%").append(namePart).append("%");
                drugstores = connector.getContext().selectDistinct(DRUGSTORE.ID, DRUGSTORE.NAME, DRUGSTORE.PHONE_NUMBER,
                        DRUGSTORE.ADDRESS, DRUGSTORE.IMAGE_URL).from(PRODUCT).join(DRUGSTORE).on(PRODUCT.DRUGSTORE_ID.equal(DRUGSTORE.ID))
                        .where(PRODUCT.PRODUCT_NAME.like(stringBuilder.toString())
                                .or(DRUGSTORE.NAME.like(stringBuilder.toString()))).fetchInto(DrugstoreJPA.class);
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

    /**
     * Read a drugstore from de database
     * @param drugstoreId
     * @return That drugstore
     */
    public static Drugstore readDrugstore(Integer drugstoreId){
        DBConnector connector = DBConnector.getInstance();
        try{
            return new DrugstoreDao(connector.getConfiguration()).fetchOneById(drugstoreId);
        }finally {
            connector.closeConnection();
        }
    }


}

