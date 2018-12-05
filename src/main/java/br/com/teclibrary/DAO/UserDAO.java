package br.com.teclibrary.DAO;

import br.com.teclibrary.entity.User;
import br.com.teclibrary.system.db.QueryType;
import br.com.teclibrary.system.impls.ModelPair;

public class UserDAO extends BasicDAO<User, Integer> {

    public UserDAO() {
        super(User.class);
    }

    public User findByLogin(String username) throws Exception {
        return this.getEntidadeByQuery("User.findByLogin",
                QueryType.Named_Query,
                new ModelPair("P_LOGIN", username.toUpperCase()));
    }

    public Boolean checkIfUserExists(String username) throws Exception {
        return this.checkIfExists("SELECT COUNT(1) FROM TUSER WHERE UPPER(LOGIN) = :P_LOGIN",
                new ModelPair("P_LOGIN", username.toUpperCase()));
    }

}
