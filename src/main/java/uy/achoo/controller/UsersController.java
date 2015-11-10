package uy.achoo.controller;

import org.jooq.DSLContext;
import uy.achoo.database.DBConnector;
import uy.achoo.model.tables.daos.UserDao;
import uy.achoo.model.tables.pojos.User;
import uy.achoo.model.tables.records.UserRecord;
import uy.achoo.util.DigestUtils;
import uy.achoo.util.EmailService;
import uy.achoo.util.PasswordGenerator;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import static uy.achoo.model.Tables.USER;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public class UsersController {

    /**
     * Create a new user in the database
     *
     * @param user
     * @return The created user
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws SQLException
     */
    public static User createUser(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException {
        DBConnector connector = DBConnector.getInstance();
        try {
            byte[] salt = DigestUtils.getSalt();
            user.setPassword(DigestUtils.digestPassword(user.getPassword(), salt));
            user.setSalt(DigestUtils.byteToBase64(salt));
            UserRecord insertedUser = insertUser(user, connector.getContext());
            user.setId(insertedUser.getId());
            return user;
        } finally {
            connector.closeConnection();
        }
    }

    private static UserRecord insertUser(User user, DSLContext context) {
        return context.insertInto(USER, USER.EMAIL, USER.FIRST_NAME, USER.LAST_NAME, USER.PASSWORD, USER.SALT)
                .values(user.getEmail(), user.getFirstName(), user.getLastName(), user.getPassword(), user.getSalt())
                .returning(USER.ID).fetchOne();
    }

    /**
     * Find all users in the database
     *
     * @return Users found
     * @throws SQLException
     */
    public static List<User> findAllUsers() throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        try {
            return connector.getContext().select(USER.ID, USER.EMAIL, USER.FIRST_NAME, USER.LAST_NAME)
                    .from(USER).fetchInto(User.class);
        } finally {
            connector.closeConnection();
        }
    }

    /**
     * Check if the user password is correct.
     *
     * @param email
     * @param password
     * @return Whether the user´s password is correct or not
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws SQLException
     */
    public static boolean checkUserPassword(String email, String password, boolean passwordAlreadyHashed) throws UnsupportedEncodingException, NoSuchAlgorithmException, SQLException {
        DBConnector connector = DBConnector.getInstance();
        try {
            boolean result = false;
            User user = new UserDao(connector.getConfiguration()).fetchOneByEmail(email);
            if (user != null) {
                String hashedPassword = passwordAlreadyHashed ? password :
                        DigestUtils.digestPassword(password, DigestUtils.base64toBytes(user.getSalt()));
                result = hashedPassword.equals(user.getPassword());
            }
            return result;
        } finally {
            connector.closeConnection();
        }
    }

    /**
     * Fetch user by email and password
     *
     * @param email
     * @param password
     * @return Whether the user´s password is correct or not
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws SQLException
     */
    public static User fetchUserByEmailAndPassword(String email, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, SQLException {
        DBConnector connector = DBConnector.getInstance();
        try {
            User user = new UserDao(connector.getConfiguration()).fetchOneByEmail(email);
            if (user != null) {
                String hashedPassword = DigestUtils.digestPassword(password, DigestUtils.base64toBytes(user.getSalt()));
                if (hashedPassword.equals(user.getPassword())) {
                    return user;
                }
            }
            return null;
        } finally {
            connector.closeConnection();
        }
    }

    /**
     * Reset the password of a user
     *
     * @param email
     * @return The new password
     */
    public static String resetPassword(String email) throws UnsupportedEncodingException, NoSuchAlgorithmException, SQLException {
        DBConnector connector = DBConnector.getInstance();
        try {
            String result = null;
            UserDao userDao = new UserDao(connector.getConfiguration());
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
            connector.closeConnection();
        }
    }

    /**
     * Check if an email is available for use
     *
     * @param email
     * @return Whether the email is available or not
     */
    public static Boolean isEmailAvailable(String email) throws SQLException {
        DBConnector connector = DBConnector.getInstance();
        try {
            UserDao userDao = new UserDao(connector.getConfiguration());
            return EmailService.validateEmail(email) && userDao.fetchOneByEmail(email) == null;
        } finally {
            connector.closeConnection();
        }
    }
}
