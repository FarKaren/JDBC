package dao;

import module.Role;
import module.Person;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;


public class PersonRepositoryImpl implements Repository<Person> {
    private final Connection connection;


    public PersonRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    public Person find(long id) {
        String query = "SELECT * FROM person WHERE id = ?";
        Person person = null;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);

            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            person = new Person();
            person.setId(resultSet.getLong("id"));
            person.setName(resultSet.getString("name"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    public void save(Person person) {
        String queryPerson = "INSERT INTO person (name) VALUES(?)";
        String queryRole = "INSERT INTO role (status) VALUES(?)";
        String queryPersonRole = "INSERT INTO person_role (person_id, role_id) VALUES(?, ?)";
        Map<String, Long> roles = handleExistedRole(person.getRoles());
        List<Long> roleIds = new ArrayList<>();

        long personAutoId = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(queryPerson, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, person.getName());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            personAutoId = rs.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement ps = connection.prepareStatement(queryRole, Statement.RETURN_GENERATED_KEYS);
            ResultSet rsRole;
            for (Map.Entry<String, Long> role : roles.entrySet()) {
                if (role.getValue() == null) {
                    ps.setString(1, role.getKey());
                    ps.executeUpdate();

                    rsRole = ps.getGeneratedKeys();
                    rsRole.next();
                    roleIds.add(rsRole.getLong(1));
                } else
                    roleIds.add(role.getValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement ps = connection.prepareStatement(queryPersonRole);
            for (Long roleId : roleIds) {
                ps.setLong(1, personAutoId);
                ps.setLong(2, roleId);
                ps.executeUpdate();
            }
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
        String queryPersonRole = "DELETE FROM person_role WHERE person_id = ?";
        String queryPerson = "DElETE FROM person WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(queryPersonRole);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement ps = connection.prepareStatement(queryPerson);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Person> getPersonList() {
        String query = "SELECT * FROM Person";
        List<Person> persons = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
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
        return persons;
    }


    public List<String> getRolesByPerson(long id) {
        String queryPersonRole = "SELECT * FROM person_role WHERE person_id = ?";
        String queryGetRoles = "SELECT * FROM role WHERE id IN (?)";
        List<Long> ids = new ArrayList<>();
        List<String> roles = new ArrayList<>();

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
            String queryIn = ids.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",", "(", ")"));
            queryGetRoles = queryGetRoles.replace("(?)", queryIn);

            PreparedStatement ps = connection.prepareStatement(queryGetRoles);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                roles.add(rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public Map<String, Long> handleExistedRole(Set<Role> roles) {
        String query = "SELECT * FROM role";
        Map<String, Long> existedRoles = new HashMap<>();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                existedRoles.put(rs.getString("status"), rs.getLong("id"));

            for (Role role : roles) {
                String status = role.getRole();
                if (!existedRoles.containsKey(status))
                    existedRoles.put(status, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existedRoles;
    }
}
