package kozmikoda.passwordspace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserValidator {

    public static void validateUser(PSQLConnection dbConnection, String username, String password) throws SQLException, IncorrectPasswordException{

        dbConnection.connectToDatabase(username);

        Statement s = dbConnection.getCommandExecutor();

        ResultSet rs = s.executeQuery("SELECT password_hash FROM user_credentials WHERE user_name = '" + username + "';");

        rs.next();

        String promptPassword = HashedPassword.calculateDigest(password, "sha-256");
        String hashedPassword = rs.getString("password_hash");

        if (!promptPassword.equals(hashedPassword)) {
            throw new IncorrectPasswordException();
        }

    }


}