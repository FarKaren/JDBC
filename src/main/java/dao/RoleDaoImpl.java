package dao;

import generator.Generator;
import module.Role;
import module.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements DAO<Role> {
    private final Connection connection;

    public RoleDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Role find(long id) {
        String query = "SELECT * FROM role WHERE id = ?";
        Role role = null;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            role = new Role();
            role.setId(rs.getLong("id"));
            role.setRole(rs.getString("role"));
            role.setUserId(rs.getLong("user_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    @Override
    public void save(Role role) {
        String query = "INSERT INTO role VALUES(?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, Generator.generateRoleId());
            ps.setString(2, role.getRole());
            ps.setLong(3, role.getUserId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Role role) {
        String query = "UPDATE role SET role = ?, user_id = ? WHERE id = ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, role.getRole());
            ps.setLong(2, role.getUserId());
            ps.setLong(3, role.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM role WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<User> getUsersByRole(String role) {
        String query = "SELECT * FROM role WHERE role = ?";
        List<Long> usersId = new ArrayList<>();
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, role);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usersId.add(rs.getLong("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!usersId.isEmpty()) {
            String userQuery = "SELECT * FROM user WHERE id IN (?)";
            Array idsArr = null;
            try {
                 idsArr = connection.createArrayOf("bigint", usersId.toArray(new Long[0]));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                PreparedStatement ps = connection.prepareStatement(userQuery);
                ps.setArray(1, idsArr);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("user_id"));
                    user.setName(rs.getString("name"));

                    users.add(user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

}
