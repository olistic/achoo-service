package uy.achoo.controller;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import uy.achoo.database.DBConnector;
import uy.achoo.model.tables.daos.DrugstoreDao;
import uy.achoo.model.tables.pojos.Drugstore;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static uy.achoo.model.Tables.DRUGSTORE;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public class DrugstoresController {

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

    public static List<Drugstore> searchDrugstoresByName(String name) throws SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            DSLContext context = DSL.using(configuration);
            List<Drugstore> drugstores = new ArrayList<>();
            if (name != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("%").append(name).append("%");
                drugstores = context.selectFrom(DRUGSTORE)
                        .where(DRUGSTORE.NAME.like(stringBuilder.toString())).fetch().into(Drugstore.class);
            }
            return drugstores;
        } finally {
            connection.close();
        }
    }


}

