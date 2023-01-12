package table;

import java.sql.*;

public class TableCreator {
    private final Connection connection;

    public TableCreator(Connection connection) {
        this.connection = connection;
    }

    public void createTable() throws SQLException {
        String createPerson = """              
                                CREATE TABLE person(
                                id serial PRIMARY KEY,
                                name varchar(50) NOT NULL
                                ); 
                             """;
        String createRole = """              
                                CREATE TABLE role(
                                id serial PRIMARY KEY,
                                status varchar(50) NOT NULL UNIQUE
                                );
                            """;
        String createPersonRole = """              
                                    CREATE TABLE person_role(
                                    id serial PRIMARY KEY,
                                    person_id bigint NOT null references person(id),
                                    role_id bigint NOT null references role(id)
                                    );
                                 """;
        String person = null;
        String role = null;
        String personRole = null;


        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs =
                    metaData.getTables("test_db", null, null, new String[]{"TABLES"});

            while (rs.next()) {
                 person = rs.getString("person");
                 role = rs.getString("role");
                 personRole = rs.getString("person_role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement ps;

        if(person == null) {
            ps = connection.prepareStatement(createPerson);
            ps.executeUpdate();
        }
        if(role == null) {
            ps = connection.prepareStatement(createRole);
            ps.executeUpdate();
        }
        if(personRole == null) {
            ps = connection.prepareStatement(createPersonRole);
            ps.executeUpdate();
        }

    }
}
