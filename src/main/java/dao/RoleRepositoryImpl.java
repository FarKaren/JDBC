package dao;

import module.Person;
import module.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoleRepositoryImpl implements Repository<Role> {
    private final Connection connection;

    public RoleRepositoryImpl(Connection connection) {
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
            rs.next();

            role = new Role();
            role.setId(rs.getLong("id"));
            role.setRole(rs.getString("status"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    @Override
    public void save(Role role) {
        String query = "INSERT INTO role (status) VALUES(?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, role.getRole());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Role role) {
        String query = "UPDATE role SET status = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, role.getRole());
            ps.setLong(2, role.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        String queryPersonRole = "DELETE FROM person_role WHERE role_id = ?";
        String queryRole = "DELETE FROM role WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(queryPersonRole);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement ps = connection.prepareStatement(queryRole);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Role> roleList(){
        String query = "SELECT * FROM role";
        List<Role> roles = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("id"));
                role.setRole(rs.getString("status"));
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public List<Person> getPersonsByRole(String status) {
        String queryGetRole = "SELECT * FROM role WHERE status = ?";
        String queryPersonRole = "SELECT * FROM person_role WHERE role_id = ?";
        String queryGetPersons = "SELECT * FROM person WHERE id IN (?)";
        long roleId = 0;
        List<Long> personsId = new ArrayList<>();
        List<Person> persons = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(queryGetRole);
            ps.setString(1, status);

            ResultSet rs = ps.executeQuery();
            rs.next();
            roleId = rs.getLong("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement ps = connection.prepareStatement(queryPersonRole);
            ps.setLong(1, roleId);

            ResultSet rs = ps.executeQuery();
            while (rs.next())
                personsId.add(rs.getLong("person_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!personsId.isEmpty()) {
            try {
                String queryIn = personsId.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",", "(", ")"));
                queryGetPersons = queryGetPersons.replace("(?)", queryIn);

                PreparedStatement ps = connection.prepareStatement(queryGetPersons);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Person person = new Person();
                    person.setId(rs.getLong("id"));
                    person.setName(rs.getString("name"));

                    persons.add(person);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return persons;
    }

}
