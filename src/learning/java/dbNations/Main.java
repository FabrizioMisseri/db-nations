package learning.java.dbNations;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String url = "jdbc:mysql://localhost:3306/db-nations";
    private static final String user = "root";
    private static final String password = "root";

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("inserisci una nazione:");
        String filteredString = scan.nextLine();
        scan.close();


        try (Connection con = DriverManager.getConnection(getUrl(), getUser(), getPassword())){
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
                ps.setString(1, "%" + filteredString + "%");

                try(ResultSet rs = ps.executeQuery()){
                    String outputString = "";
                    String nameCountry = "";
                    Integer countryId = 0;
                    String nameRegion = "";
                    String nameContinent = "";

                    if (rs.next()) {
                        do {
                            nameCountry = rs.getString(1);
                            countryId = rs.getInt(2);
                            nameRegion = rs.getString(3);
                            nameContinent = rs.getString(4);

                            outputString += nameCountry +" "+ countryId +" "+ nameRegion +" "+ nameContinent +" "+ "\n";
                        } while (rs.next());
                    } else {
                        System.out.println("niente");
                    }

                    System.out.println(outputString);

                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
