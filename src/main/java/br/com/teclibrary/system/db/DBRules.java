package br.com.teclibrary.system.db;

import br.com.teclibrary.entity.Role;
import br.com.teclibrary.entity.User;
import br.com.teclibrary.service.RoleService;
import br.com.teclibrary.service.UserService;
import br.com.teclibrary.system.impls.ModelPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.NoResultException;

public class DBRules {

    private static final Logger logger = LoggerFactory.getLogger(DBRules.class);
    private static final BasicJPA JPA = new BasicJPA();

    public DBRules() {
        createDefaultExtensions();
        createDefaultRoles();
        createDefaultUsers();
    }

    public static void init() {
        new DBRules();
    }

    private void createExtension(String extension) {
        try {
            if(ConnectionFactory.DEFAULT_PERSISTENCE_UNIT == ConnectionFactory.DefaultPersistenceUnit.Web)
                JPA.executeStatement("CREATE EXTENSION IF NOT EXISTS ".concat(extension), QueryType.Native_Query);
        } catch (Exception ex) {
            logger.error(ex.getMessage().concat(", criando extensão: ".concat(extension)), ex);
            System.exit(1);
        }
    }

    private void createDefaultExtensions() {
        createExtension("UNACCENT");
    }

    private void createDefaultRoles() {
        String[] roles = new String[]{"ADMIN", "MANAGER", "USER"};
        RoleService service = new RoleService();
        for (String descrRole : roles) {
            try {
                Boolean exists = service.checkIfExists("SELECT COUNT(1) FROM TROLE WHERE DESCRROLE = :P_DESCRROLE", new ModelPair("P_DESCRROLE", descrRole));
                if (!exists) {
                    Role role = new Role();
                    role.setDescricao(descrRole);
                    service.insert(role);
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage().concat(" tentando autenticar Role: " + descrRole), ex);
                System.exit(1);
            }
        }
    }

    private void createDefaultUsers() {
        UserService service = new UserService();
        try {
            User user = service.findByLogin("admin");
            user.setSenha(new BCryptPasswordEncoder().encode("admin"));
            service.update(user);
        } catch (NoResultException ex) {
            User user = User.builder()
                    .login("admin")
                    .senha(new BCryptPasswordEncoder().encode("admin"))
                    .role(new Role(1, "ADMIN"))
                    .profissao("Administrador")
                    .email("teclibraryapp@gmail.com")
                    .telefone("0000000000")
                    .cpf("00000000000")
                    .build();
            service.insert(user);
        } catch (Exception ex) {
            logger.error(ex.getMessage().concat(", tentando autenticar usuário"), ex);
            System.exit(1);
        }
    }
}
