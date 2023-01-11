public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/test_db";
        String user = "user";
        String password = "user";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
