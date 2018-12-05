package br.com.teclibrary.DAO;

import br.com.teclibrary.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class RoleDAO extends BasicDAO<Role, Integer> {

    private static List<Role> roleList = new ArrayList<>();

    public RoleDAO() {
        super(Role.class);
        setRoleList(this.findAll());
    }

    public static final List<GrantedAuthority> retrieveUserRoles(Role role) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("USER"));
        if (role == null)
            return authorityList;
        if (role.getDescricao().equals("ADMIN")) {
            authorityList.add(new SimpleGrantedAuthority("ADMIN"));
            authorityList.add(new SimpleGrantedAuthority("MANAGER"));
        }
        if (role.getDescricao().equals("MANAGER")) {
            authorityList.add(new SimpleGrantedAuthority("MANAGER"));
        }
        return authorityList;
    }

    public static List<Role> getRoleList() {
        if (roleList == null || roleList.isEmpty())
            setRoleList(new RoleDAO().findAll());
        return roleList;
    }

    private static void setRoleList(List<Role> roleList) {
        RoleDAO.roleList = roleList;
    }
}
