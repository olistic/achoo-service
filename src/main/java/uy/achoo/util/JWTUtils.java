package uy.achoo.util;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.auth0.jwt.internal.org.apache.commons.codec.binary.Base64;
import com.sun.jersey.spi.container.ContainerRequest;

import javax.servlet.ServletException;
import javax.ws.rs.core.HttpHeaders;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 *
 * Auxiliary class to manage JSON Web tokens
 */
public class JWTUtils {
    // Random base64-encoded encoded String to be used as client secret
    private static final String CLIENT_SECRET = "mkpj8eOMtsiJlnBG2vu1zOcbZi1pWGUnjFwsRJO1z2WGqRRTC3nWUQYCLI0kbQ8x";

    public static  Map<String,Object> decodeToken(String token){
        byte[] decodedSecret = new Base64(true).decodeBase64(CLIENT_SECRET);
        try {
            return new JWTVerifier(decodedSecret).verify(token);
        } catch (NoSuchAlgorithmException|InvalidKeyException|IOException|SignatureException|JWTVerifyException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodePayload(Map<String,Object> decodedData){
        byte[] decodedSecret = new Base64(true).decodeBase64(CLIENT_SECRET);
        return new JWTSigner(decodedSecret).sign(decodedData);
    }

    public static String getTokenFromRequest(ContainerRequest containerRequest) throws ServletException {
        final String authorizationHeader = containerRequest.getHeaderValue("authorization");
        return extractTokenFromAuthorization(authorizationHeader);
    }

    public static String getTokenFromHeaders(HttpHeaders headers) throws ServletException {
        final String authorizationHeader = headers.getRequestHeaders().getFirst("authorization");
        return extractTokenFromAuthorization(authorizationHeader);
    }

    private static String extractTokenFromAuthorization(String authorizationHeader) throws ServletException {
        String token = null;
        if (authorizationHeader == null) {
            throw new ServletException("Unauthorized: No Authorization header was found");
        }

        String[] parts = authorizationHeader.split(" ");
        if (parts.length != 2) {
            throw new ServletException("Unauthorized: Format is Authorization: Bearer [token]");
        }

        String scheme = parts[0];
        String credentials = parts[1];

        Pattern pattern = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(scheme).matches()) {
            token = credentials;
        }
        return token;
    }


    public static Map<String, Object> introspect(Object obj) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        BeanInfo info = Introspector.getBeanInfo(obj.getClass());
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            Method reader = pd.getReadMethod();
            if (reader != null)
                result.put(pd.getName(), reader.invoke(obj));
        }
        return result;
    }
}
