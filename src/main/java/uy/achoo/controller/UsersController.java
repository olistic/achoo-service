package uy.achoo.controller;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import uy.achoo.database.DBConnector;
import uy.achoo.model.tables.daos.UserDao;
import uy.achoo.model.tables.pojos.User;
import uy.achoo.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by alfredo on 04/10/15.
 */
public class UsersController {

    public static User createUser(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException {
        // Open the database connection
        Connection connection = DBConnector.getInstance().connection();

        // Initialize a Configuration
        Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);

        // Initialize the DAO with the Configuration
        UserDao userDao = new UserDao(configuration);
        byte[] salt = DigestUtils.getSalt();

        user.setPassword(DigestUtils.digestPassword(user.getPassword(), salt));
        user.setSalt(DigestUtils.byteToBase64(salt));
        // Insert the POJO
        userDao.insert(user);

        // Close the database connection
        connection.close();
        return user;
    }

    public static List<User> findAllUsers() throws SQLException {
        Connection connection = DBConnector.getInstance().connection();
        // Initialize a Configuration
        Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
        List<User> users = new UserDao(configuration).findAll();
        connection.close();
        return users;
    }
}
