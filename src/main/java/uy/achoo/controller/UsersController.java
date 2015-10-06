package uy.achoo.controller;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import uy.achoo.database.DBConnector;
import uy.achoo.model.tables.daos.UserDao;
import uy.achoo.model.tables.pojos.User;
import uy.achoo.util.DigestUtils;
import uy.achoo.util.PasswordGenerator;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public class UsersController {

    /**
     * Create a new user in the database
     * @param user
     * @return The created user
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws SQLException
     */
    public static User createUser(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            UserDao userDao = new UserDao(configuration);
            byte[] salt = DigestUtils.getSalt();
            user.setPassword(DigestUtils.digestPassword(user.getPassword(), salt));
            user.setSalt(DigestUtils.byteToBase64(salt));
            userDao.insert(user);
            return user;
        } finally {
            connection.close();
        }
    }

    /**
     * Find all users in the database
     * @return Users found
     * @throws SQLException
     */
    public static List<User> findAllUsers() throws SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            List<User> users = new UserDao(configuration).findAll();
            return users;
        } finally {
            connection.close();
        }
    }

    /**
     * Check if the user password is correct.
     * @param email
     * @param password
     * @return Whether the user´s password is correct or not
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws SQLException
     */
    public static boolean checkUsersPassword(String email, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            boolean result = false;
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

    /**
     * Fetch user by email and password
     * @param email
     * @param password
     * @return Whether the user´s password is correct or not
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws SQLException
     */
    public static User fetchUserByEmailAndPassword(String email, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            User user = new UserDao(configuration).fetchOneByEmail(email);
            if (user != null) {
                String hashedPassword = DigestUtils.digestPassword(password, DigestUtils.base64toBytes(user.getSalt()));
                if(hashedPassword.equals(user.getPassword())){
                    return user;
                }
            }
            return null;
        } finally {
            connection.close();
        }
    }

    /**
     * Reset the password of a user
     * @param email
     * @return The new password
     */
    public static String resetPassword(String email) throws UnsupportedEncodingException, NoSuchAlgorithmException, SQLException {
        Connection connection = DBConnector.getInstance().connection();
        try {
            String result = null;
            Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
            UserDao userDao = new UserDao(configuration);
            User user = userDao.fetchOneByEmail(email);
            if (user != null) {
                String newPassword = PasswordGenerator.generatePassword(8);
                String hashedPassword = DigestUtils.digestPassword(newPassword, DigestUtils.base64toBytes(user.getSalt()));
                user.setPassword(hashedPassword);
                userDao.update(user);
                result = newPassword;
            }
            return result;
        } finally {
            connection.close();
        }
    }
}
