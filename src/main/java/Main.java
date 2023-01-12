import connection.DatabaseConnection;
import dao.PersonRepositoryImpl;
import dao.RoleRepositoryImpl;
import module.Person;
import module.Role;
import table.TableCreator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private static long testPersonId;
    private static long testRoleId;
    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getConnection();
        TableCreator creator = new TableCreator(connection);
        try {
            creator.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("CRUD operation with Person");
        System.out.println("=========================================================");
        PersonRepositoryImpl personRepository = new PersonRepositoryImpl(connection);
//        //Save person
//        Person person = new Person();
//        person.setName("Tom");
//        Role role = new Role();
//        role.setRole("user");
//        Role role2 = new Role();
//        role2.setRole("admin");
//        person.addRole(role);
//        person.addRole(role2);
//        personRepository.save(person);

//        //Get person list
//        List<Person> personList = personRepository.getPersonList();
//        personList.forEach(p -> System.out.println("Name: " + p.getName() + " -- id: " + p.getId()));
//
//        testPersonId = personList.get(0).getId();
//
//        //Find person
//        Person fromDb = personRepository.find(testPersonId);
//        System.out.println(fromDb.getName());

//        //Update person
//        Person updatePerson = new Person();
//        updatePerson.setId(testPersonId);
//        updatePerson.setName("Stan");
//        personRepository.update(updatePerson);
//
//        //Delete person
//        personRepository.delete(testPersonId);

//       //Get person's roles
//        List<String> roles = personRepository.getRolesByPerson(testPersonId);
//        System.out.println(roles);

        System.out.println("\nCRUD operation with Role");
        System.out.println("=========================================================");

        RoleRepositoryImpl roleRepository = new RoleRepositoryImpl(connection);

//        //Save role
//        Role role = new Role();
//        role.setRole("child");
//        roleRepository.save(role);

//        List<Role> roles = roleRepository.roleList();
//        roles.forEach(role -> System.out.println(role.getRole()));
//
//        testRoleId = roles.get(0).getId();

//        //Get role
//        Role fromDb = roleRepository.find(20);
//        System.out.println("Status: " + fromDb.getRole() + " -- id: " + fromDb.getId());

//        //Update role
//        Role updatedRole = new Role();
//        updatedRole.setRole("adult");
//        updatedRole.setId(testRoleId);
//        roleRepository.update(updatedRole);

//        //Delete role
//        roleRepository.delete(testRoleId);

//        //Get persons by status
//        List<Person> personList = roleRepository.getPersonsByRole("admin");
//        personList.forEach(person -> System.out.println(person.getName()));






    }
}
