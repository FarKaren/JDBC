import connection.DatabaseConnection;
import dao.UserDaoImpl;
import module.Person;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getConnection();
        UserDaoImpl service = new UserDaoImpl(connection);
        //Save person
        Person person = new Person();
        person.setName("Mark");
        service.save(person);

        //Find person
        Person fromDb = service.find(1);
        System.out.println(fromDb.getName());

        //Update person
        Person updatePerson = new Person();
        updatePerson.setId(8);
        updatePerson.setName("Stan");
        service.update(updatePerson);


    }
}
