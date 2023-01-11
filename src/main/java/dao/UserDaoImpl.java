package dao;

import generator.Generator;
import module.Role;
import module.User;

import java.sql.*;
import java.util.*;


public class UserDaoImpl implements DAO<User>{
    private final Connection connection;


    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public User find(long id) {
        String query = "SELECT * FROM user AS u join role AS r ON user.role = role.id WHERE id = ?";
        User user = null;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            user = new User();
            Set<Role> roles = new HashSet<Role>();

            ResultSet resultSet = ps.executeQuery();
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("name"));

            while (resultSet.next()){
                roles.add((Role)resultSet.getObject("r"));
            }
            user.setRoles(roles);

            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void save(User user) {
        String query = "INSERT INTO user VALUES(?, ?)";
        long userId = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            userId = Generator.generateUserId();
            ps.setLong(1, userId);
            ps.setString(2, user.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(user.getRoles() != null){
            String roleQuery = "INSERT INTO role VALUES(?, ?, ?)";
            Set<Role> roles = user.getRoles();
            Iterator<Role> iterator = roles.iterator();
            try {
                PreparedStatement ps = connection.prepareStatement(roleQuery);
                while (iterator.hasNext()) {
                    ps.setLong(1, Generator.generateRoleId());
                    ps.setString(2, iterator.next().getRole());
                    ps.setLong(3, userId);

                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(User user) {
        String query = "UPDATE user SET name = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, user.getName());
            ps.setLong(2, user.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(long id) {
        String query = "DElETE FROM user WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Role> getRolesByUser(long id){
        String query = "SELECT * FROM role WHERE user_id = ?";
        List<Role> roles = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("id"));
                role.setRole(rs.getString("role"));
                role.setUserId(rs.getLong("user_id"));
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }
}
