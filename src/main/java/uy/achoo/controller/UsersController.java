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
        Connection connection = DBConnector.getInstance().connection();
        try {
            // Initialize a Configuration
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);

            // Initialize the DAO with the Configuration
            UserDao userDao = new UserDao(configuration);
            byte[] salt = DigestUtils.getSalt();

            user.setPassword(DigestUtils.digestPassword(user.getPassword(), salt));
            user.setSalt(DigestUtils.byteToBase64(salt));
            // Insert the POJO
            userDao.insert(user);
            return user;
        } finally {
            connection.close();
        }
    }

    public static List<User> findAllUsers() throws SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            // Initialize a Configuration
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            List<User> users = new UserDao(configuration).findAll();
            return users;
        } finally {
            connection.close();
        }
    }

    public static boolean checkUsersPassword(String email, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            boolean result = false;
            // Initialize a Configuration
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            User user = new UserDao(configuration).fetchOneByEmail(email);
            if (user != null) {
                String hashedPassword = DigestUtils.digestPassword(password, DigestUtils.base64toBytes(user.getSalt()));
                result = hashedPassword.equals(user.getPassword());
            }
            return result;
        } finally {
            connection.close();
        }
    }
}
