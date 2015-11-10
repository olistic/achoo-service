package uy.achoo.controller;

import uy.achoo.customModel.CustomPharmacy;
import uy.achoo.database.DBConnector;
import uy.achoo.model.tables.daos.PharmacyDao;
import uy.achoo.model.tables.pojos.Pharmacy;
import uy.achoo.util.GoogleService;

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
public class PharmaciesController {
    /**
     * Find all pharmacies in the database
     *
     * @return Pharmacies found
     * @throws SQLException
     */
    public static List<Pharmacy> findAllPharmacies() throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        try {
            List<Pharmacy> pharmacies = new PharmacyDao(connector.getConfiguration()).findAll();
            return pharmacies;
        } finally {
            connector.closeConnection();
        }
    }

    /**
     * Find all pharmacies that offer a product that has a name that contains namePart or a name that contains namePart
     *
     * @param namePart
     * @return The list of pharmacies with that product.
     * @throws SQLException
     */
    public static List<CustomPharmacy> searchPharmaciesByNameOrProductName(String namePart, Double latitude, Double longitude) throws Exception {
        DBConnector connector = DBConnector.getInstance();
        try {
            List<CustomPharmacy> pharmacies = new ArrayList<>();
            if (namePart != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("%").append(namePart).append("%");
                pharmacies = connector.getContext().selectDistinct(PHARMACY.ID, PHARMACY.NAME, PHARMACY.PHONE_NUMBER,
                        PHARMACY.ADDRESS, PHARMACY.IMAGE_URL, ORDER.SCORE.avg().as("average_score")).
                        from(PRODUCT).join(PHARMACY).on(PRODUCT.PHARMACY_ID.equal(PHARMACY.ID))
                        .leftOuterJoin(ORDER).on(ORDER.PHARMACY_ID.equal(PHARMACY.ID))
                        .where(PRODUCT.NAME.like(stringBuilder.toString())
                                .or(PHARMACY.NAME.like(stringBuilder.toString())))
                        .groupBy(PHARMACY.ID).fetchInto(CustomPharmacy.class);
                if (latitude != null && longitude != null) {
                    pharmacies = GoogleService.orderPharmaciesByLocation(latitude, longitude, pharmacies);
                }
            }
            return pharmacies;
        } finally {
            connector.closeConnection();
        }
    }


    /**
     * Read a pharmacy from de database
     *
     * @param pharmacyId
     * @return That pharmacy
     */
    public static CustomPharmacy readPharmacy(Integer pharmacyId) {
        DBConnector connector = DBConnector.getInstance();
        try {
            return connector.getContext().select(PHARMACY.ID, PHARMACY.NAME, PHARMACY.PHONE_NUMBER,
                    PHARMACY.ADDRESS, PHARMACY.IMAGE_URL, ORDER.SCORE.avg().as("average_score")).
                    from(PRODUCT).join(PHARMACY).on(PRODUCT.PHARMACY_ID.equal(PHARMACY.ID))
                    .leftOuterJoin(ORDER).on(ORDER.PHARMACY_ID.equal(PHARMACY.ID))
                    .where(PHARMACY.ID.equal(pharmacyId))
                    .groupBy(PHARMACY.ID).fetchOneInto(CustomPharmacy.class);
        } finally {
            connector.closeConnection();
        }
    }


}

