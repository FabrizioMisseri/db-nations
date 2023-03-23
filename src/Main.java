import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        SELECT countries.name, countries.country_id, regions.name, continents.name FROM `countries`
//        INNER JOIN regions on countries.region_id = regions.region_id
//        INNER JOIN continents on regions.continent_id = continents.continent_id
//        ORDER BY countries.name;
        String url = "jdbc:mysql://localhost:3306/db-nations";
        String user = "root";
        String password = "root";


        Scanner scan = new Scanner(System.in);
        System.out.println("inserisci una nazione:");
        String filteredString = scan.nextLine();
        filteredString = '%' + filteredString + '%';
        scan.close();


        try (Connection con = DriverManager.getConnection(url, user, password)){
            //System.out.println("ciao");
            String query = """
                    SELECT countries.name, countries.country_id, regions.name, continents.name FROM `countries`
                    INNER JOIN regions on countries.region_id = regions.region_id
                    INNER JOIN continents on regions.continent_id = continents.continent_id
                    WHERE countries.name LIKE ?
                    ORDER BY countries.name;
                    """;

            try(PreparedStatement ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)){
                ps.setString(1, filteredString);

                try(ResultSet rs = ps.executeQuery()){
                    String outputString = "";

                    while (rs.next()){
                        String nameCountry = rs.getString(1);
                        Integer countryId = rs.getInt(2);
                        String nameRegion = rs.getString(3);
                        String nameContinent = rs.getString(4);

                        outputString += nameCountry +" "+ countryId +" "+ nameRegion +" "+ nameContinent +" "+ "\n";
                    }

                    System.out.println(outputString);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
