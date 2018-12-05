package br.com.teclibrary.system.converter;

import br.com.teclibrary.DAO.RoleDAO;
import br.com.teclibrary.entity.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter implements Converter<String, Role> {
    @Override
    public Role convert(String roleStr) {
        return RoleDAO.getRoleList().stream()
                .filter(role -> role.getCodigo() == Integer.valueOf(roleStr)).findFirst().get();
    }
}
