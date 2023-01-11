package dao;

import module.Role;
import module.Person;

import java.sql.*;
import java.util.*;


public class UserDaoImpl implements DAO<Person>{
    private final Connection connection;


    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public Person find(long id) {
        String query = "SELECT * FROM person WHERE id = ?";
        Person person = null;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            person = new Person();

            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            person.setId(resultSet.getLong("id"));
            person.setName(resultSet.getString("name"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    public void save(Person person) {
        String query = "INSERT INTO Person (name) VALUES(?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, person.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Person person) {
        String query = "UPDATE Person SET name = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, person.getName());
            ps.setLong(2, person.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(long id) {
        String query = "DElETE FROM person WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Role> getRolesByUser(long id){
        String queryPersonRole = "SELECT * FROM person_role WHERE person_id = ?";
        String queryGetRoles = "SELECT * FROM role WHERE id = ?";
        List<Long> ids = new ArrayList<>();
        List<Role> roles = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(queryPersonRole);
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next())
                ids.add(rs.getLong("role_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Array idsArr = connection.createArrayOf("bigint", ids.toArray(new Long[0]));
            PreparedStatement ps = connection.prepareStatement(queryGetRoles);
            ps.setArray(1, idsArr);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("id"));
                role.setRole(rs.getString("role"));

                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }
}
