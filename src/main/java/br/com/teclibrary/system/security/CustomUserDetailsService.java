package br.com.teclibrary.system.security;

import br.com.teclibrary.entity.User;
import br.com.teclibrary.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.NoResultException;

@Configuration
public class CustomUserDetailsService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = service.findByLogin(username);
            if (user == null)
                throw new NoResultException();
            return user;
        } catch (UsernameNotFoundException ex) {
            String msg = "Usuário/Senha inválidos.";
            logger.error(msg);
            throw new UsernameNotFoundException(msg);
        } catch (NoResultException ex) {
            String msg = "Usuário não encontrado para login digitado.";
            logger.error(msg);
            throw new UsernameNotFoundException(msg);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new UsernameNotFoundException(ex.getMessage());
        }
    }
}
