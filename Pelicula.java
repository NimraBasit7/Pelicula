import java.sql.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.application.HostServices;

class GridPaneUtils {
    public static int getRowIndexAsInteger(Node node) {
        final var a = GridPane.getRowIndex(node);
        if (a == null) {
            return 0;
        }
        return a;
    }

    public static void removeRow(GridPane grid, Integer targetRowIndexIntegerObject) {
        final int targetRowIndex = targetRowIndexIntegerObject == null ? 0 : targetRowIndexIntegerObject;

        // Remove children from row
        grid.getChildren().removeIf(node -> getRowIndexAsInteger(node) == targetRowIndex);

        // Update indexes for elements in further rows
        grid.getChildren().forEach(node -> {
            final int rowIndex = getRowIndexAsInteger(node);
            if (targetRowIndex < rowIndex) {
                GridPane.setRowIndex(node, rowIndex - 1);
            }
        });
        grid.getRowConstraints().remove(targetRowIndex);
    }
}

public class Pelicula extends Application {

    @Override
    public void start(Stage stage) {
        //loading image for bg
        Image PeliculaLogin = new Image("PeliculaLogin.png");
        BackgroundImage mainBG = new BackgroundImage(PeliculaLogin,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,new BackgroundSize(1.0, 1.0, true, true, false, false));
        Background bg1 = new Background(mainBG);

        Image PeliculaSU = new Image("PeliculaSU.png");
        BackgroundImage SignUpBG = new BackgroundImage(PeliculaSU,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,new BackgroundSize(1.0, 1.0, true, true, false, false));
        Background SUbg = new Background(SignUpBG);

        Image PeliculaApp = new Image("PeliculaApp.png");
        BackgroundImage searchBG = new BackgroundImage(PeliculaApp,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,new BackgroundSize(1.0, 1.0, true, true, false, false));
        Background SearchBG = new Background(searchBG);

        //PELICULA APP - Movie Search
        //creating gridpane for main search page to stack elements on
        GridPane grid3 = new GridPane();
        grid3.setHgap(10);
        grid3.setVgap(10);
        grid3.setPadding(new Insets(0, 10, 0, 10));
        grid3.setBackground(SearchBG);
        Scene movieSearchScene = new Scene(grid3, 1370, 770); 
        movieSearchScene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());

        Text MovieHeader = new Text("Movie Search");
        grid3.add(MovieHeader, 20, 20, 10, 1);
        

        //search criteria
        Label searchByMS = new Label("Search by:");
        RadioButton r1MS = new RadioButton("Keyword");
        RadioButton r2MS = new RadioButton("Release Year");
        RadioButton r3MS = new RadioButton("Age Rating");
        RadioButton r4MS = new RadioButton("IMDb Rating");
        RadioButton r5MS = new RadioButton("Genre");
        RadioButton r6MS = new RadioButton("Actor");
        ToggleGroup tgMS = new ToggleGroup();
        r1MS.setToggleGroup(tgMS);
        r2MS.setToggleGroup(tgMS);
        r3MS.setToggleGroup(tgMS);
        r4MS.setToggleGroup(tgMS);
        r5MS.setToggleGroup(tgMS);
        r6MS.setToggleGroup(tgMS);
        grid3.add(searchByMS, 20, 30, 10, 1);
        grid3.add(r1MS, 30, 30, 12, 1);
        grid3.add(r2MS, 42, 30, 20, 1);
        grid3.add(r3MS, 62, 30, 15, 1);
        grid3.add(r4MS, 77, 30, 10, 1);
        grid3.add(r5MS, 87, 30, 10, 1);
        grid3.add(r6MS, 97, 30, 10, 1);

        r1MS.setOnAction(e -> {
            Label keyword = new Label("Enter Keyword");
            TextField keywordEntered = new TextField();
            Button searchMovies = new Button("Search");
            grid3.add(keyword, 20, 32, 20, 1);
            grid3.add(keywordEntered, 40, 32, 30, 1);
            grid3.add(searchMovies, 70, 32, 10, 1);
            //search by keyword
            searchMovies.setOnAction(e2 -> {
                String showKeyword = keywordEntered.getText();
                String posterDB;
                String titleDB;
                String yearDB;
                String actorDB;
                String genreDB;
                String ageRatingDB;
                String imdbRatingDB;
                String distributorDB;
                String distributorLinkDB;
                String descriptionDB;
         
                Connection conn;
                PreparedStatement sql;

                GridPane searchGrid = new GridPane();
                searchGrid.setHgap(10);
                searchGrid.setVgap(10);
                searchGrid.setPadding(new Insets(0, 10, 0, 10));
                ScrollPane scrollSearch = new ScrollPane(searchGrid);
                Scene searchScene = new Scene(scrollSearch, 1300, 700); 
                searchScene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());
                Stage newWindow = new Stage();
                newWindow.setTitle("Search Results");
                newWindow.setScene(searchScene);
                newWindow.show();

                try{
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "UET@20se39");
                    sql = conn.prepareStatement("SELECT * FROM MOVIE_USER_VIEW");
                    ResultSet rs1 = sql.executeQuery();

                    int i = 13;
                    while (rs1.next()){
                        
                        titleDB = rs1.getString("Movie_Name"); 
                        if(titleDB.contains(showKeyword)){
                            posterDB = rs1.getString("Movie_Poster");
                            yearDB = rs1.getString("Release_Date");
                            actorDB = rs1.getString("Actor_Name");
                            genreDB = rs1.getString("Genre");
                            ageRatingDB = rs1.getString("Age_Rating");
                            imdbRatingDB = rs1.getString("Imdb_Rating");
                            descriptionDB = rs1.getString("Movie_Description");
                            distributorDB = rs1.getString("Distributor_Name");
                            distributorLinkDB = rs1.getString("Distributor_Link");
                    

                            Image image = new Image(posterDB);
                            ImageView imageView = new ImageView(image);
                            imageView.setImage(image);
                            imageView.setFitHeight(300);
                            imageView.setFitWidth(200);
                            imageView.setPreserveRatio(true);

                            Text title = new Text(titleDB);
                            Text year = new Text(yearDB);
                            Text actor = new Text(actorDB);
                            Text genre = new Text(genreDB);
                            Text ageRating = new Text(ageRatingDB);
                            Text imdbRating = new Text(imdbRatingDB);
                            String link = distributorLinkDB;

                            Text dist = new Text(distributorDB);
                            Hyperlink distLink= new Hyperlink();
                            distLink.setText(distributorLinkDB);
                            distLink.setOnAction(e5->{
                                HostServices host = getHostServices();
                                host.showDocument(link);
                            });
                            
                            Text desc = new Text(descriptionDB);
                            desc.setWrappingWidth(500);
                            

                            Label t1 = new Label("Name:");
                            Label t2 = new Label("Year:");
                            Label t3 = new Label("Actor:");
                            Label t4 = new Label("Genre:");
                            Label t5 = new Label("Age Rating:");
                            Label t6 = new Label("IMDb Rating:");
                            Label t7 = new Label("Distributor:");
                            Label t8 = new Label("Distributor Link:");
                            Label t9 = new Label("Description:");

                            searchGrid.add(imageView, 20, i, 20, 11);
                            searchGrid.add(t1, 42, i, 15, 1);
                            searchGrid.add(title, 57, i, 100, 1);
                            searchGrid.add(t2, 42, i+1, 15, 1);
                            searchGrid.add(year, 57, i+1, 100, 1);
                            searchGrid.add(t3, 42, i+2, 15, 1);
                            searchGrid.add(actor, 57, i+2, 100, 1);
                            searchGrid.add(t5, 42, i+3, 15, 1);
                            searchGrid.add(ageRating, 57, i+3, 100, 1);
                            searchGrid.add(t6, 42, i+4, 15, 1);
                            searchGrid.add(imdbRating, 57, i+4, 100, 1);
                            searchGrid.add(t4, 42, i+5, 15, 1);
                            searchGrid.add(genre, 57, i+5, 100, 1);
                            searchGrid.add(t9, 42, i+6, 15, 7);
                            searchGrid.add(desc, 57, i+6, 140, 7);
                            searchGrid.add(t7, 42, i+13, 15, 1);
                            searchGrid.add(dist, 57, i+13, 140, 1);
                            searchGrid.add(t8, 42, i+14, 15, 1);
                            searchGrid.add(distLink, 57, i+14, 140, 1);
                            i += 20;
                        }
                    }
                } 
                catch (Exception ex){
                    System.out.println(ex);
                }  
            });
        });
        r1MS.setOnMousePressed(e -> {
            GridPaneUtils.removeRow(grid3, 32);
        });
        r2MS.setOnAction(e -> {
            Label releaseYear = new Label("Select Year:");
            String ReleaseYear[] = {"2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013",
            "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004",
            "2003", "2002", "2001"};
            ComboBox yearsList = new ComboBox(FXCollections.observableArrayList(ReleaseYear));
            Button searchMovies = new Button("Search");
            grid3.add(releaseYear, 20, 32, 20, 1);
            grid3.add(yearsList, 40, 32, 20, 1);
            grid3.add(searchMovies, 60, 32, 10, 1);

            //search by year
            searchMovies.setOnAction(e2 -> {
                String yearSelected = (String) yearsList.getValue();
                String posterDB;
                String titleDB;
                String yearDB;
                String actorDB;
                String genreDB;
                String ageRatingDB;
                String imdbRatingDB;
                String distributorDB;
                String distributorLinkDB;
                String descriptionDB;
         
                Connection conn;
                PreparedStatement sql;

                GridPane searchGrid = new GridPane();
                searchGrid.setHgap(10);
                searchGrid.setVgap(10);
                searchGrid.setPadding(new Insets(0, 10, 0, 10));
                ScrollPane scrollSearch = new ScrollPane(searchGrid);
                Scene searchScene = new Scene(scrollSearch, 1300, 700); 
                searchScene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());
                Stage newWindow = new Stage();
                newWindow.setTitle("Search Results");
                newWindow.setScene(searchScene);
                newWindow.show();

                try{
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "UET@20se39");
                    sql = conn.prepareStatement("SELECT * FROM MOVIE_USER_VIEW");
                    ResultSet rs1 = sql.executeQuery();

                    int i = 13;
                    while (rs1.next()){
                        yearDB = rs1.getString("Release_Date");
                         
                        if(yearDB.contains(yearSelected)){
                            titleDB = rs1.getString("Movie_Name");
                            posterDB = rs1.getString("Movie_Poster");
                            actorDB = rs1.getString("Actor_Name");
                            genreDB = rs1.getString("Genre");
                            ageRatingDB = rs1.getString("Age_Rating");
                            imdbRatingDB = rs1.getString("Imdb_Rating");
                            descriptionDB = rs1.getString("Movie_Description");
                            distributorDB = rs1.getString("Distributor_Name");
                            distributorLinkDB = rs1.getString("Distributor_Link");
                    
                            Image image = new Image(posterDB);
                            ImageView imageView = new ImageView(image);
                            imageView.setImage(image);
                            imageView.setFitHeight(300);
                            imageView.setFitWidth(200);
                            imageView.setPreserveRatio(true);

                            Text title = new Text(titleDB);
                            Text year = new Text(yearDB);
                            Text actor = new Text(actorDB);
                            Text genre = new Text(genreDB);
                            Text ageRating = new Text(ageRatingDB);
                            Text imdbRating = new Text(imdbRatingDB);

                            String link = distributorLinkDB;

                            Text dist = new Text(distributorDB);
                            Hyperlink distLink= new Hyperlink();
                            distLink.setText(distributorLinkDB);
                            distLink.setOnAction(e5->{
                                HostServices host = getHostServices();
                                host.showDocument(link);
                            });
                        
                            Text desc = new Text(descriptionDB);
                            desc.setWrappingWidth(500);
                            

                            Label t1 = new Label("Name:");
                            Label t2 = new Label("Year:");
                            Label t3 = new Label("Actor:");
                            Label t4 = new Label("Genre:");
                            Label t5 = new Label("Age Rating:");
                            Label t6 = new Label("IMDb Rating:");
                            Label t7 = new Label("Distributor:");
                            Label t8 = new Label("Distributor Link:");
                            Label t9 = new Label("Description:");

                            searchGrid.add(imageView, 20, i, 20, 11);
                            searchGrid.add(t1, 42, i, 15, 1);
                            searchGrid.add(title, 57, i, 100, 1);
                            searchGrid.add(t2, 42, i+1, 15, 1);
                            searchGrid.add(year, 57, i+1, 100, 1);
                            searchGrid.add(t3, 42, i+2, 15, 1);
                            searchGrid.add(actor, 57, i+2, 100, 1);
                            searchGrid.add(t5, 42, i+3, 15, 1);
                            searchGrid.add(ageRating, 57, i+3, 100, 1);
                            searchGrid.add(t6, 42, i+4, 15, 1);
                            searchGrid.add(imdbRating, 57, i+4, 100, 1);
                            searchGrid.add(t4, 42, i+5, 15, 1);
                            searchGrid.add(genre, 57, i+5, 100, 1);
                            searchGrid.add(t9, 42, i+6, 15, 7);
                            searchGrid.add(desc, 57, i+6, 140, 7);
                            searchGrid.add(t7, 42, i+13, 15, 1);
                            searchGrid.add(dist, 57, i+13, 140, 1);
                            searchGrid.add(t8, 42, i+14, 15, 1);
                            searchGrid.add(distLink, 57, i+14, 140, 1);
                            i += 20;
                        }
                    }
                } 
                catch (Exception ex){
                    System.out.println(ex);
                }  
            });
        });
        r2MS.setOnMousePressed(e -> {
            GridPaneUtils.removeRow(grid3, 32);
        });
        r3MS.setOnAction(e -> {
            Label ageRating = new Label("Select Rating:");
            String AgeRating[] = {"Unrated", "R", "TV-14", "TV-G", "TV-MA", "TV-PG", "TV-Y", "TV-Y7"};
            ComboBox ageRatingList = new ComboBox(FXCollections.observableArrayList(AgeRating));
            Button searchMovies = new Button("Search");
            grid3.add(ageRating, 20, 32, 20, 1);
            grid3.add(ageRatingList, 40, 32, 20, 1);
            grid3.add(searchMovies, 60, 32, 10, 1);

            //search by age rating
            searchMovies.setOnAction(e2 -> {
                String ageRatingSelected = (String) ageRatingList.getValue();
                String posterDB;
                String titleDB;
                String yearDB;
                String actorDB;
                String genreDB;
                String ageRatingDB;
                String imdbRatingDB;
                String distributorDB;
                String distributorLinkDB;
                String descriptionDB;
         
                Connection conn;
                PreparedStatement sql;

                GridPane searchGrid = new GridPane();
                searchGrid.setHgap(10);
                searchGrid.setVgap(10);
                searchGrid.setPadding(new Insets(0, 10, 0, 10));
                ScrollPane scrollSearch = new ScrollPane(searchGrid);
                Scene searchScene = new Scene(scrollSearch, 1300, 700); 
                searchScene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());
                Stage newWindow = new Stage();
                newWindow.setTitle("Search Results");
                newWindow.setScene(searchScene);
                newWindow.show();

                try{
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "UET@20se39");
                    sql = conn.prepareStatement("SELECT * FROM MOVIE_USER_VIEW");
                    ResultSet rs1 = sql.executeQuery();

                    int i = 13;
                    while (rs1.next()){
                        ageRatingDB = rs1.getString("Age_Rating");
                        if(ageRatingDB.contains(ageRatingSelected)){
                            titleDB = rs1.getString("Movie_Name");
                            posterDB = rs1.getString("Movie_Poster");
                            actorDB = rs1.getString("Actor_Name");
                            genreDB = rs1.getString("Genre");
                            yearDB = rs1.getString("Release_Date");
                            imdbRatingDB = rs1.getString("Imdb_Rating");
                            descriptionDB = rs1.getString("Movie_Description");
                            distributorDB = rs1.getString("Distributor_Name");
                            distributorLinkDB = rs1.getString("Distributor_Link");
                    
                            Image image = new Image(posterDB);
                            ImageView imageView = new ImageView(image);
                            imageView.setImage(image);
                            imageView.setFitHeight(300);
                            imageView.setFitWidth(200);
                            imageView.setPreserveRatio(true);

                            Text title = new Text(titleDB);
                            Text year = new Text(yearDB);
                            Text actor = new Text(actorDB);
                            Text genre = new Text(genreDB);
                            Text ageRatingM = new Text(ageRatingDB);
                            Text imdbRating = new Text(imdbRatingDB);

                            String link = distributorLinkDB;

                            Text dist = new Text(distributorDB);
                            Hyperlink distLink= new Hyperlink();
                            distLink.setText(distributorLinkDB);
                            distLink.setOnAction(e5->{
                                HostServices host = getHostServices();
                                host.showDocument(link);
                            });
                        
                            Text desc = new Text(descriptionDB);
                            desc.setWrappingWidth(500);
                            

                            Label t1 = new Label("Name:");
                            Label t2 = new Label("Year:");
                            Label t3 = new Label("Actor:");
                            Label t4 = new Label("Genre:");
                            Label t5 = new Label("Age Rating:");
                            Label t6 = new Label("IMDb Rating:");
                            Label t7 = new Label("Distributor:");
                            Label t8 = new Label("Distributor Link:");
                            Label t9 = new Label("Description:");

                            searchGrid.add(imageView, 20, i, 20, 11);
                            searchGrid.add(t1, 42, i, 15, 1);
                            searchGrid.add(title, 57, i, 100, 1);
                            searchGrid.add(t2, 42, i+1, 15, 1);
                            searchGrid.add(year, 57, i+1, 100, 1);
                            searchGrid.add(t3, 42, i+2, 15, 1);
                            searchGrid.add(actor, 57, i+2, 100, 1);
                            searchGrid.add(t5, 42, i+3, 15, 1);
                            searchGrid.add(ageRatingM, 57, i+3, 100, 1);
                            searchGrid.add(t6, 42, i+4, 15, 1);
                            searchGrid.add(imdbRating, 57, i+4, 100, 1);
                            searchGrid.add(t4, 42, i+5, 15, 1);
                            searchGrid.add(genre, 57, i+5, 100, 1);
                            searchGrid.add(t9, 42, i+6, 15, 7);
                            searchGrid.add(desc, 57, i+6, 140, 7);
                            searchGrid.add(t7, 42, i+13, 15, 1);
                            searchGrid.add(dist, 57, i+13, 140, 1);
                            searchGrid.add(t8, 42, i+14, 15, 1);
                            searchGrid.add(distLink, 57, i+14, 140, 1);
                            i += 20;
                        }
                    }
                } 
                catch (Exception ex){
                    System.out.println(ex);
                }  
            });
        });
        r3MS.setOnMousePressed(e -> {
            GridPaneUtils.removeRow(grid3, 32);
        });
        r4MS.setOnAction(e -> {
            Label imdbRating = new Label("Rating Higher Than:");
            String IMDbRating[] = {"9", "8", "7", "6", "5", "4", "3", "2", "1"};
            ComboBox imdbRatingList = new ComboBox(FXCollections.observableArrayList(IMDbRating));
            Button searchMovies = new Button("Search");
            grid3.add(imdbRating, 20, 32, 20, 1);
            grid3.add(imdbRatingList, 40, 32, 20, 1);
            grid3.add(searchMovies, 60, 32, 10, 1);

            //search by imdb rating
            searchMovies.setOnAction(e2 -> {
                String imdbRatingSelectedS = (String) imdbRatingList.getValue();
                int imdbRatingSelected = Integer.parseInt(imdbRatingSelectedS);
                String posterDB;
                String titleDB;
                String yearDB;
                String actorDB;
                String genreDB;
                String ageRatingDB;
                double imdbRatingDB;
                String distributorDB;
                String distributorLinkDB;
                String descriptionDB;
         
                Connection conn;
                PreparedStatement sql;

                GridPane searchGrid = new GridPane();
                searchGrid.setHgap(10);
                searchGrid.setVgap(10);
                searchGrid.setPadding(new Insets(0, 10, 0, 10));
                ScrollPane scrollSearch = new ScrollPane(searchGrid);
                Scene searchScene = new Scene(scrollSearch, 1300, 700); 
                searchScene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());
                Stage newWindow = new Stage();
                newWindow.setTitle("Search Results");
                newWindow.setScene(searchScene);
                newWindow.show();

                try{
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "UET@20se39");
                    sql = conn.prepareStatement("SELECT * FROM MOVIE_USER_VIEW");
                    ResultSet rs1 = sql.executeQuery();

                    int i = 13;
                    while (rs1.next()){
                        imdbRatingDB = rs1.getDouble("Imdb_Rating");

                        if(imdbRatingSelected <= imdbRatingDB){
                            titleDB = rs1.getString("Movie_Name");
                            posterDB = rs1.getString("Movie_Poster");
                            actorDB = rs1.getString("Actor_Name");
                            genreDB = rs1.getString("Genre");
                            yearDB = rs1.getString("Release_Date");
                            ageRatingDB = rs1.getString("Age_Rating");
                            descriptionDB = rs1.getString("Movie_Description");
                            distributorDB = rs1.getString("Distributor_Name");
                            distributorLinkDB = rs1.getString("Distributor_Link");
                    
                            Image image = new Image(posterDB);
                            ImageView imageView = new ImageView(image);
                            imageView.setImage(image);
                            imageView.setFitHeight(300);
                            imageView.setFitWidth(200);
                            imageView.setPreserveRatio(true);

                            Text title = new Text(titleDB);
                            Text year = new Text(yearDB);
                            Text actor = new Text(actorDB);
                            Text genre = new Text(genreDB);
                            Text ageRatingM = new Text(ageRatingDB);
                            Text imdbRatingM = new Text(imdbRatingDB+"");

                            String link = distributorLinkDB;

                            Text dist = new Text(distributorDB);
                            Hyperlink distLink= new Hyperlink();
                            distLink.setText(distributorLinkDB);
                            distLink.setOnAction(e5->{
                                HostServices host = getHostServices();
                                host.showDocument(link);
                            });
                        
                            Text desc = new Text(descriptionDB);
                            desc.setWrappingWidth(500);
                            

                            Label t1 = new Label("Name:");
                            Label t2 = new Label("Year:");
                            Label t3 = new Label("Actor:");
                            Label t4 = new Label("Genre:");
                            Label t5 = new Label("Age Rating:");
                            Label t6 = new Label("IMDb Rating:");
                            Label t7 = new Label("Distributor:");
                            Label t8 = new Label("Distributor Link:");
                            Label t9 = new Label("Description:");

                            searchGrid.add(imageView, 20, i, 20, 11);
                            searchGrid.add(t1, 42, i, 15, 1);
                            searchGrid.add(title, 57, i, 100, 1);
                            searchGrid.add(t2, 42, i+1, 15, 1);
                            searchGrid.add(year, 57, i+1, 100, 1);
                            searchGrid.add(t3, 42, i+2, 15, 1);
                            searchGrid.add(actor, 57, i+2, 100, 1);
                            searchGrid.add(t5, 42, i+3, 15, 1);
                            searchGrid.add(ageRatingM, 57, i+3, 100, 1);
                            searchGrid.add(t6, 42, i+4, 15, 1);
                            searchGrid.add(imdbRatingM, 57, i+4, 100, 1);
                            searchGrid.add(t4, 42, i+5, 15, 1);
                            searchGrid.add(genre, 57, i+5, 100, 1);
                            searchGrid.add(t9, 42, i+6, 15, 7);
                            searchGrid.add(desc, 57, i+6, 140, 7);
                            searchGrid.add(t7, 42, i+13, 15, 1);
                            searchGrid.add(dist, 57, i+13, 140, 1);
                            searchGrid.add(t8, 42, i+14, 15, 1);
                            searchGrid.add(distLink, 57, i+14, 140, 1);
                            i += 20;
                        }
                    }
                } 
                catch (Exception ex){
                    System.out.println(ex);
                }  
            });
        });
        r4MS.setOnMousePressed(e -> {
            GridPaneUtils.removeRow(grid3, 32);
        });

        r5MS.setOnAction(e -> {
            Label genreL = new Label("Select Genre:");
            String Genre[] = {"Action", "Adventure", "Animation", "Crime", "Mystery", "Thriller", "Comedy", "Fantasy"};
            ComboBox genreList = new ComboBox(FXCollections.observableArrayList(Genre));
            Button searchMovies = new Button("Search");
            grid3.add(genreL, 20, 32, 20, 1);
            grid3.add(genreList, 40, 32, 30, 1);
            grid3.add(searchMovies, 70, 32, 10, 1);

            //search by Genre
            searchMovies.setOnAction(e2 -> {
                String genreSelected = (String) genreList.getValue();
                String posterDB;
                String titleDB;
                String yearDB;
                String actorDB;
                String genreDB;
                String ageRatingDB;
                String imdbRatingDB;
                String distributorDB;
                String distributorLinkDB;
                String descriptionDB;
         
                Connection conn;
                PreparedStatement sql;

                GridPane searchGrid = new GridPane();
                searchGrid.setHgap(10);
                searchGrid.setVgap(10);
                searchGrid.setPadding(new Insets(0, 10, 0, 10));
                ScrollPane scrollSearch = new ScrollPane(searchGrid);
                Scene searchScene = new Scene(scrollSearch, 1300, 700); 
                searchScene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());
                Stage newWindow = new Stage();
                newWindow.setTitle("Search Results");
                newWindow.setScene(searchScene);
                newWindow.show();

                try{
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "UET@20se39");
                    sql = conn.prepareStatement("SELECT * FROM MOVIE_USER_VIEW");
                    ResultSet rs1 = sql.executeQuery();

                    int i = 13;
                    while (rs1.next()){
                        genreDB = rs1.getString("Genre");
                        
                        if(genreDB.contains(genreSelected)){
                            titleDB = rs1.getString("Movie_Name");
                            posterDB = rs1.getString("Movie_Poster");
                            actorDB = rs1.getString("Actor_Name");
                            yearDB = rs1.getString("Release_Date");
                            ageRatingDB = rs1.getString("Age_Rating");
                            imdbRatingDB = rs1.getString("Imdb_Rating");
                            descriptionDB = rs1.getString("Movie_Description");
                            distributorDB = rs1.getString("Distributor_Name");
                            distributorLinkDB = rs1.getString("Distributor_Link");
                    
                            Image image = new Image(posterDB);
                            ImageView imageView = new ImageView(image);
                            imageView.setImage(image);
                            imageView.setFitHeight(300);
                            imageView.setFitWidth(200);
                            imageView.setPreserveRatio(true);

                            Text title = new Text(titleDB);
                            Text year = new Text(yearDB);
                            Text actor = new Text(actorDB);
                            Text genre = new Text(genreDB);
                            Text ageRating = new Text(ageRatingDB);
                            Text imdbRating = new Text(imdbRatingDB);

                            String link = distributorLinkDB;

                            Text dist = new Text(distributorDB);
                            Hyperlink distLink= new Hyperlink();
                            distLink.setText(distributorLinkDB);
                            distLink.setOnAction(e5->{
                                HostServices host = getHostServices();
                                host.showDocument(link);
                            });
                        
                            Text desc = new Text(descriptionDB);
                            desc.setWrappingWidth(500);
                            

                            Label t1 = new Label("Name:");
                            Label t2 = new Label("Year:");
                            Label t3 = new Label("Actor:");
                            Label t4 = new Label("Genre:");
                            Label t5 = new Label("Age Rating:");
                            Label t6 = new Label("IMDb Rating:");
                            Label t7 = new Label("Distributor:");
                            Label t8 = new Label("Distributor Link:");
                            Label t9 = new Label("Description:");

                            searchGrid.add(imageView, 20, i, 20, 11);
                            searchGrid.add(t1, 42, i, 15, 1);
                            searchGrid.add(title, 57, i, 100, 1);
                            searchGrid.add(t2, 42, i+1, 15, 1);
                            searchGrid.add(year, 57, i+1, 100, 1);
                            searchGrid.add(t3, 42, i+2, 15, 1);
                            searchGrid.add(actor, 57, i+2, 100, 1);
                            searchGrid.add(t5, 42, i+3, 15, 1);
                            searchGrid.add(ageRating, 57, i+3, 100, 1);
                            searchGrid.add(t6, 42, i+4, 15, 1);
                            searchGrid.add(imdbRating, 57, i+4, 100, 1);
                            searchGrid.add(t4, 42, i+5, 15, 1);
                            searchGrid.add(genre, 57, i+5, 100, 1);
                            searchGrid.add(t9, 42, i+6, 15, 7);
                            searchGrid.add(desc, 57, i+6, 140, 7);
                            searchGrid.add(t7, 42, i+13, 15, 1);
                            searchGrid.add(dist, 57, i+13, 140, 1);
                            searchGrid.add(t8, 42, i+14, 15, 1);
                            searchGrid.add(distLink, 57, i+14, 140, 1);
                            i += 20;
                        }
                    }
                } 
                catch (Exception ex){
                    System.out.println(ex);
                }  
            });
        });
        r5MS.setOnMousePressed(e -> {
            GridPaneUtils.removeRow(grid3, 32);
        });
        r6MS.setOnAction(e -> {
            Label keyword = new Label("Enter Name");
            TextField keywordEntered = new TextField();
            Button searchMovies = new Button("Search");
            grid3.add(keyword, 20, 32, 20, 1);
            grid3.add(keywordEntered, 40, 32, 30, 1);
            grid3.add(searchMovies, 70, 32, 10, 1);
            //search by keyword
            searchMovies.setOnAction(e2 -> {
                String showKeyword = keywordEntered.getText();
                String posterDB;
                String titleDB;
                String yearDB;
                String actorDB;
                String genreDB;
                String ageRatingDB;
                String imdbRatingDB;
                String distributorDB;
                String distributorLinkDB;
                String descriptionDB;
         
                Connection conn;
                PreparedStatement sql;

                GridPane searchGrid = new GridPane();
                searchGrid.setHgap(10);
                searchGrid.setVgap(10);
                searchGrid.setPadding(new Insets(0, 10, 0, 10));
                ScrollPane scrollSearch = new ScrollPane(searchGrid);
                Scene searchScene = new Scene(scrollSearch, 1300, 700); 
                searchScene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());
                Stage newWindow = new Stage();
                newWindow.setTitle("Search Results");
                newWindow.setScene(searchScene);
                newWindow.show();

                try{
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "UET@20se39");
                    sql = conn.prepareStatement("SELECT * FROM MOVIE_USER_VIEW");
                    ResultSet rs1 = sql.executeQuery();

                    int i = 13;
                    while (rs1.next()){
                        actorDB = rs1.getString("Actor_Name");
                        
                        if(actorDB!=null && actorDB.contains(showKeyword)){
                            posterDB = rs1.getString("Movie_Poster");
                            yearDB = rs1.getString("Release_Date");
                            titleDB = rs1.getString("Movie_Name"); 
                            genreDB = rs1.getString("Genre");
                            ageRatingDB = rs1.getString("Age_Rating");
                            imdbRatingDB = rs1.getString("Imdb_Rating");
                            descriptionDB = rs1.getString("Movie_Description");
                            distributorDB = rs1.getString("Distributor_Name");
                            distributorLinkDB = rs1.getString("Distributor_Link");
                    

                            Image image = new Image(posterDB);
                            ImageView imageView = new ImageView(image);
                            imageView.setImage(image);
                            imageView.setFitHeight(300);
                            imageView.setFitWidth(200);
                            imageView.setPreserveRatio(true);

                            Text title = new Text(titleDB);
                            Text year = new Text(yearDB);
                            Text actor = new Text(actorDB);
                            Text genre = new Text(genreDB);
                            Text ageRating = new Text(ageRatingDB);
                            Text imdbRating = new Text(imdbRatingDB);

                            String link = distributorLinkDB;

                            Text dist = new Text(distributorDB);
                            Hyperlink distLink= new Hyperlink();
                            distLink.setText(distributorLinkDB);
                            distLink.setOnAction(e5->{
                                HostServices host = getHostServices();
                                host.showDocument(link);
                            });
                        
                            Text desc = new Text(descriptionDB);
                            desc.setWrappingWidth(500);
                            

                            Label t1 = new Label("Name:");
                            Label t2 = new Label("Year:");
                            Label t3 = new Label("Actor:");
                            Label t4 = new Label("Genre:");
                            Label t5 = new Label("Age Rating:");
                            Label t6 = new Label("IMDb Rating:");
                            Label t7 = new Label("Distributor:");
                            Label t8 = new Label("Distributor Link:");
                            Label t9 = new Label("Description:");

                            searchGrid.add(imageView, 20, i, 20, 11);
                            searchGrid.add(t1, 42, i, 15, 1);
                            searchGrid.add(title, 57, i, 100, 1);
                            searchGrid.add(t2, 42, i+1, 15, 1);
                            searchGrid.add(year, 57, i+1, 100, 1);
                            searchGrid.add(t3, 42, i+2, 15, 1);
                            searchGrid.add(actor, 57, i+2, 100, 1);
                            searchGrid.add(t5, 42, i+3, 15, 1);
                            searchGrid.add(ageRating, 57, i+3, 100, 1);
                            searchGrid.add(t6, 42, i+4, 15, 1);
                            searchGrid.add(imdbRating, 57, i+4, 100, 1);
                            searchGrid.add(t4, 42, i+5, 15, 1);
                            searchGrid.add(genre, 57, i+5, 100, 1);
                            searchGrid.add(t9, 42, i+6, 15, 7);
                            searchGrid.add(desc, 57, i+6, 140, 7);
                            searchGrid.add(t7, 42, i+13, 15, 1);
                            searchGrid.add(dist, 57, i+13, 140, 1);
                            searchGrid.add(t8, 42, i+14, 15, 1);
                            searchGrid.add(distLink, 57, i+14, 140, 1);
                            i += 20;
                        }
                    }
                } 
                catch (Exception ex){
                    System.out.println(ex);
                }  
            });
        });
        r6MS.setOnMousePressed(e -> {
            GridPaneUtils.removeRow(grid3, 32);
        });

        

        //PELICULA APP - Show Search
        //creating gridpane for main search page to stack elements on
        GridPane grid6 = new GridPane();
        grid6.setHgap(10);
        grid6.setVgap(10);
        grid6.setPadding(new Insets(0, 10, 0, 10));
        grid6.setBackground(SearchBG);
        Scene showSearchScene = new Scene(grid6, 1370, 770); 
        showSearchScene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());

        Text ShowHeader = new Text("TV Show Search");
        grid6.add(ShowHeader, 20, 20, 20, 1);

        //search criteria
        Label searchBySS = new Label("Search by:");
        RadioButton r1SS = new RadioButton("Keyword");
        RadioButton r2SS = new RadioButton("Release Year");
        RadioButton r3SS = new RadioButton("Age Rating");
        RadioButton r4SS = new RadioButton("Genre");
        ToggleGroup tgSS = new ToggleGroup();
        r1SS.setToggleGroup(tgSS);
        r2SS.setToggleGroup(tgSS);
        r3SS.setToggleGroup(tgSS);
        r4SS.setToggleGroup(tgSS);
        grid6.add(searchBySS, 20, 30, 10, 1);
        grid6.add(r1SS, 30, 30, 12, 1);
        grid6.add(r2SS, 42, 30, 20, 1);
        grid6.add(r3SS, 62, 30, 15, 1);
        grid6.add(r4SS, 77, 30, 10, 1);

        r1SS.setOnAction(e -> {
            Label keyword = new Label("Enter Keyword");
            TextField keywordEntered = new TextField();
            Button searchShows = new Button("Search");
            grid6.add(keyword, 20, 32, 20, 1);
            grid6.add(keywordEntered, 40, 32, 30, 1);
            grid6.add(searchShows, 70, 32, 10, 1);
            //search by keyword
            searchShows.setOnAction(e2 -> {
                String showKeyword = keywordEntered.getText();
                String titleDB;
                String yearDB;
                String ratingDB;
                String seasonsDB;
                String genreDB;
                String descriptionDB;
                Connection conn;
                PreparedStatement sql;

                GridPane searchGrid = new GridPane();
                searchGrid.setHgap(10);
                searchGrid.setVgap(10);
                searchGrid.setPadding(new Insets(0, 10, 0, 10));
                ScrollPane scrollSearch = new ScrollPane(searchGrid);
                Scene searchScene = new Scene(scrollSearch, 1300, 700); 
                searchScene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());
                Stage newWindow = new Stage();
                newWindow.setTitle("Search Results");
                newWindow.setScene(searchScene);
                newWindow.show();

                try{
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "UET@20se39");
                    sql = conn.prepareStatement("SELECT * FROM SHOW_USER_VIEW");
                    ResultSet rs1 = sql.executeQuery();
                    
                    int i = 13;
                    while (rs1.next()){
                        titleDB = rs1.getString("TVShow_Name"); 
                        yearDB = rs1.getString("Release_Year");
                        ratingDB = rs1.getString("Age_Rating"); 
                        seasonsDB = rs1.getString("No_Of_Seasons");
                        genreDB = rs1.getString("Genre"); 
                        descriptionDB = rs1.getString("TVShow_Description");

                        if(titleDB.contains(showKeyword)){
                            Text title = new Text(titleDB);
                            Text year = new Text(yearDB);
                            Text rating = new Text(ratingDB);
                            Text seasons = new Text(seasonsDB);
                            Text genre = new Text(genreDB);
                            Text desc = new Text(descriptionDB);
                            desc.setWrappingWidth(800);

                            Label t1 = new Label("Name:");
                            Label t2 = new Label("Year:");
                            Label t3 = new Label("Age Rating:");
                            Label t4 = new Label("No. of Seasons:");
                            Label t5 = new Label("Genre:");
                            Label t6 = new Label("Description:");

                            searchGrid.add(t1, 20, i, 10, 1);
                            searchGrid.add(title, 35, i, 100, 1);
                            searchGrid.add(t2, 20, i+1, 10, 1);
                            searchGrid.add(year, 35, i+1, 5, 1);
                            searchGrid.add(t3, 20, i+2, 15, 1);
                            searchGrid.add(rating, 35, i+2, 10, 1);
                            searchGrid.add(t4, 20, i+3, 15, 1);
                            searchGrid.add(seasons, 35, i+3, 10, 1);
                            searchGrid.add(t5, 20, i+5, 10, 1);
                            searchGrid.add(genre, 35, i+5, 100, 1);
                            searchGrid.add(t6, 20, i+6, 15, 1);
                            searchGrid.add(desc, 35, i+6, 140, 1);

                            i += 15;
                        }
                    }
                } 
                catch (Exception ex){
                    System.out.println(ex);
                }  
            });
        });
        r1SS.setOnMousePressed(e -> {
            GridPaneUtils.removeRow(grid6, 32);
        });
        r2SS.setOnAction(e -> {
            Label releaseYear = new Label("Select Year:");
            String ReleaseYear[] = {"2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013",
            "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004",
            "2003", "2002", "2001", "2000", "1999"};
            ComboBox yearsList = new ComboBox(FXCollections.observableArrayList(ReleaseYear));
            Button searchShows = new Button("Search");
            grid6.add(releaseYear, 20, 32, 20, 1);
            grid6.add(yearsList, 40, 32, 20, 1);
            grid6.add(searchShows, 60, 32, 10, 1);

            //search by year
            searchShows.setOnAction(e2 -> {
                String yearSelected = (String) yearsList.getValue();
                String titleDB;
                String yearDB;
                String ratingDB;
                String seasonsDB;
                String genreDB;
                String descriptionDB;
                Connection conn;
                PreparedStatement sql;

                GridPane searchGrid = new GridPane();
                searchGrid.setHgap(10);
                searchGrid.setVgap(10);
                searchGrid.setPadding(new Insets(0, 10, 0, 10));
                ScrollPane scrollSearch = new ScrollPane(searchGrid);
                Scene searchScene = new Scene(scrollSearch, 1300, 700); 
                searchScene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());
                Stage newWindow = new Stage();
                newWindow.setTitle("Search Results");
                newWindow.setScene(searchScene);
                newWindow.show();

                try{
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "UET@20se39");
                    sql = conn.prepareStatement("SELECT * FROM SHOW_USER_VIEW");
                    ResultSet rs1 = sql.executeQuery();

                    int i = 13;
                    while (rs1.next()){
                        titleDB = rs1.getString("TVShow_Name"); 
                        yearDB = rs1.getString("Release_Year");
                        ratingDB = rs1.getString("Age_Rating"); 
                        seasonsDB = rs1.getString("No_Of_Seasons");
                        genreDB = rs1.getString("Genre"); 
                        descriptionDB = rs1.getString("TVShow_Description");

                        if(yearSelected.equals(yearDB)){
                            Text title = new Text(titleDB);
                            Text year = new Text(yearDB);
                            Text rating = new Text(ratingDB);
                            Text seasons = new Text(seasonsDB);
                            Text genre = new Text(genreDB);
                            Text desc = new Text(descriptionDB);
                            desc.setWrappingWidth(800);

                            Label t1 = new Label("Name:");
                            Label t2 = new Label("Year:");
                            Label t3 = new Label("Age Rating:");
                            Label t4 = new Label("No. of Seasons:");
                            Label t5 = new Label("Genre:");
                            Label t6 = new Label("Description:");

                            searchGrid.add(t1, 20, i, 10, 1);
                            searchGrid.add(title, 35, i, 100, 1);
                            searchGrid.add(t2, 20, i+1, 10, 1);
                            searchGrid.add(year, 35, i+1, 5, 1);
                            searchGrid.add(t3, 20, i+2, 15, 1);
                            searchGrid.add(rating, 35, i+2, 10, 1);
                            searchGrid.add(t4, 20, i+3, 15, 1);
                            searchGrid.add(seasons, 35, i+3, 10, 1);
                            searchGrid.add(t5, 20, i+5, 10, 1);
                            searchGrid.add(genre, 35, i+5, 100, 1);
                            searchGrid.add(t6, 20, i+6, 15, 1);
                            searchGrid.add(desc, 35, i+6, 140, 1);

                            i += 15;
                        }
                    }
                } 
                catch (Exception ex){
                    System.out.println(ex);
                }  
            });
        });
        r2SS.setOnMousePressed(e -> {
            GridPaneUtils.removeRow(grid6, 32);
        });
        r3SS.setOnAction(e -> {
            Label ageRating = new Label("Select Rating:");
            String AgeRating[] = {"R", "TV-14", "TV-G", "TV-MA", "TV-PG", "TV-Y", "TV-Y7"};
            ComboBox ageRatingList = new ComboBox(FXCollections.observableArrayList(AgeRating));
            Button searchShows = new Button("Search");
            grid6.add(ageRating, 20, 32, 20, 1);
            grid6.add(ageRatingList, 40, 32, 20, 1);
            grid6.add(searchShows, 60, 32, 10, 1);

            //search by age rating
            searchShows.setOnAction(e2 -> {
                String ratingSelected = (String) ageRatingList.getValue();
                String titleDB;
                String yearDB;
                String ratingDB;
                String seasonsDB;
                String genreDB;
                String descriptionDB;
                Connection conn;
                PreparedStatement sql;

                GridPane searchGrid = new GridPane();
                searchGrid.setHgap(10);
                searchGrid.setVgap(10);
                searchGrid.setPadding(new Insets(0, 10, 0, 10));
                ScrollPane scrollSearch = new ScrollPane(searchGrid);
                Scene searchScene = new Scene(scrollSearch, 1300, 700); 
                searchScene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());
                Stage newWindow = new Stage();
                newWindow.setTitle("Search Results");
                newWindow.setScene(searchScene);
                newWindow.show();

                try{
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "UET@20se39");
                    sql = conn.prepareStatement("SELECT * FROM SHOW_USER_VIEW");
                    ResultSet rs1 = sql.executeQuery();

                    int i = 13;
                    while (rs1.next()){
                        titleDB = rs1.getString("TVShow_Name"); 
                        yearDB = rs1.getString("Release_Year");
                        ratingDB = rs1.getString("Age_Rating"); 
                        seasonsDB = rs1.getString("No_Of_Seasons");
                        genreDB = rs1.getString("Genre"); 
                        descriptionDB = rs1.getString("TVShow_Description");

                        if(ratingSelected.equals(ratingDB)){
                            Text title = new Text(titleDB);
                            Text year = new Text(yearDB);
                            Text rating = new Text(ratingDB);
                            Text seasons = new Text(seasonsDB);
                            Text genre = new Text(genreDB);
                            Text desc = new Text(descriptionDB);
                            desc.setWrappingWidth(800);

                            Label t1 = new Label("Name:");
                            Label t2 = new Label("Year:");
                            Label t3 = new Label("Age Rating:");
                            Label t4 = new Label("No. of Seasons:");
                            Label t5 = new Label("Genre:");
                            Label t6 = new Label("Description:");

                            searchGrid.add(t1, 20, i, 10, 1);
                            searchGrid.add(title, 35, i, 100, 1);
                            searchGrid.add(t2, 20, i+1, 10, 1);
                            searchGrid.add(year, 35, i+1, 5, 1);
                            searchGrid.add(t3, 20, i+2, 15, 1);
                            searchGrid.add(rating, 35, i+2, 10, 1);
                            searchGrid.add(t4, 20, i+3, 15, 1);
                            searchGrid.add(seasons, 35, i+3, 10, 1);
                            searchGrid.add(t5, 20, i+5, 10, 1);
                            searchGrid.add(genre, 35, i+5, 100, 1);
                            searchGrid.add(t6, 20, i+6, 15, 1);
                            searchGrid.add(desc, 35, i+6, 140, 1);

                            i += 15;
                        }
                    }
                } 
                catch (Exception ex){
                    System.out.println(ex);
                }  
            });
        });
        r3SS.setOnMousePressed(e -> {
            GridPaneUtils.removeRow(grid6, 32);
        });
        r4SS.setOnAction(e -> {
            Label genreL = new Label("Select Genre:");
            String Genre[] = {"Anime Series", "Crime TV Shows", "Reality TV", "Romantic TV Shows", "Kids' TV", "Teen TV Shows", "Docuseries", "Science & Nature TV"};
            ComboBox genreList = new ComboBox(FXCollections.observableArrayList(Genre));
            Button searchShows = new Button("Search");
            grid6.add(genreL, 20, 32, 20, 1);
            grid6.add(genreList, 40, 32, 30, 1);
            grid6.add(searchShows, 70, 32, 10, 1);

            //search by genre
            searchShows.setOnAction(e2 -> {
                String genreSelected = (String) genreList.getValue();
                String titleDB;
                String yearDB;
                String ratingDB;
                String seasonsDB;
                String genreDB;
                String descriptionDB;
                Connection conn;
                PreparedStatement sql;

                GridPane searchGrid = new GridPane();
                searchGrid.setHgap(10);
                searchGrid.setVgap(10);
                searchGrid.setPadding(new Insets(0, 10, 0, 10));
                ScrollPane scrollSearch = new ScrollPane(searchGrid);
                Scene searchScene = new Scene(scrollSearch, 1300, 700); 
                searchScene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());
                Stage newWindow = new Stage();
                newWindow.setTitle("Search Results");
                newWindow.setScene(searchScene);
                newWindow.show();

                try{
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "UET@20se39");
                    sql = conn.prepareStatement("SELECT * FROM SHOW_USER_VIEW");
                    ResultSet rs1 = sql.executeQuery();

                    int i = 13;
                    while (rs1.next()){
                        titleDB = rs1.getString("TVShow_Name"); 
                        yearDB = rs1.getString("Release_Year");
                        ratingDB = rs1.getString("Age_Rating"); 
                        seasonsDB = rs1.getString("No_Of_Seasons");
                        genreDB = rs1.getString("Genre"); 
                        descriptionDB = rs1.getString("TVShow_Description");

                        if(genreDB.contains(genreSelected)){
                            Text title = new Text(titleDB);
                            Text year = new Text(yearDB);
                            Text rating = new Text(ratingDB);
                            Text seasons = new Text(seasonsDB);
                            Text genre = new Text(genreDB);
                            Text desc = new Text(descriptionDB);
                            desc.setWrappingWidth(800);

                            Label t1 = new Label("Name:");
                            Label t2 = new Label("Year:");
                            Label t3 = new Label("Age Rating:");
                            Label t4 = new Label("No. of Seasons:");
                            Label t5 = new Label("Genre:");
                            Label t6 = new Label("Description:");

                            searchGrid.add(t1, 20, i, 10, 1);
                            searchGrid.add(title, 35, i, 100, 1);
                            searchGrid.add(t2, 20, i+1, 10, 1);
                            searchGrid.add(year, 35, i+1, 5, 1);
                            searchGrid.add(t3, 20, i+2, 15, 1);
                            searchGrid.add(rating, 35, i+2, 10, 1);
                            searchGrid.add(t4, 20, i+3, 15, 1);
                            searchGrid.add(seasons, 35, i+3, 10, 1);
                            searchGrid.add(t5, 20, i+5, 10, 1);
                            searchGrid.add(genre, 35, i+5, 100, 1);
                            searchGrid.add(t6, 20, i+6, 15, 1);
                            searchGrid.add(desc, 35, i+6, 140, 1);

                            i += 15;
                        }
                    }
                } 
                catch (Exception ex){
                    System.out.println(ex);
                }  
            });
        });
        r4SS.setOnMousePressed(e -> {
            GridPaneUtils.removeRow(grid6, 32);
        });
        
        
    
        //SIGNUP PAGE
        //creating gridpane for login page to stack elements on
        GridPane grid2 = new GridPane();
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(0, 10, 0, 10));
        grid2.setAlignment(Pos.CENTER);
        
        //setting bg
        grid2.setBackground(SUbg);

        //Name, Username, DOB and Password inputs for sign up 
        Label l3 = new Label("Name: ");
        TextField tf4 = new TextField();
        grid2.add(l3, 0, 5, 1, 2); 
        grid2.add(tf4, 1, 5, 1, 2); 
        Label l4 = new Label("Username: ");
        TextField tf5 = new TextField();
        grid2.add(l4, 0, 7, 1, 2); 
        grid2.add(tf5, 1, 7, 1, 2); 
        Label l5 = new Label("Date of Birth: ");
        TextField tf6 = new TextField();
        tf6.setPromptText("dd-mm-yyyy");
        tf6.setFocusTraversable(false);
        grid2.add(l5, 0, 9, 1, 2); 
        grid2.add(tf6, 1, 9, 1, 2);
        Label l6 = new Label("Password: ");
        PasswordField tf7 = new PasswordField();
        grid2.add(l6, 0, 11, 1, 2); 
        grid2.add(tf7, 1, 11, 1, 2);
        Label l7 = new Label("Confirm Password:");
        PasswordField tf8 = new PasswordField();
        grid2.add(l7, 0, 13, 1, 2); 
        grid2.add(tf8, 1, 13, 1, 2);

        //sign up button
        Button b2 = new Button("Sign Up");
        grid2.add(b2, 1, 15, 1, 1);
        b2.setOnAction(e -> {
            String NameSIGN = tf4.getText();
            String userNameSIGN = tf5.getText();
            String DoBSIGN = tf6.getText();
            String passwordSIGN = tf7.getText();
            String conPasswordSIGN = tf8.getText();
            Connection conn;
            CallableStatement sql;
            if(passwordSIGN.equals(conPasswordSIGN)){
                try{
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "UET@20se39");
                    sql = conn.prepareCall("{call AddUser(?, ?, ?, ?)}");
                    sql.setString(1, userNameSIGN);
                    sql.setString(2, NameSIGN);
                    sql.setString(3, DoBSIGN);
                    sql.setString(4, passwordSIGN);
                    sql.executeQuery();
                } 
                catch (Exception ex){
                    System.out.println(ex);
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("USERNAME ERROR");
                    alert.setContentText("Username already in use.");
                    alert.showAndWait();
                    return;
                }
                tf4.clear();
                tf5.clear();
                tf6.clear();
                tf7.clear();
                tf8.clear();
                stage.setScene(movieSearchScene);
            }
            else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("ERROR");
                alert.setContentText("Error! Passwords do not match.");
                alert.showAndWait();
            } 
        });

        Scene signUpScene = new Scene(grid2, 1370, 770); 
        signUpScene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());

        //LOGIN PAGE
        //creating gridpane for login page to stack elements on
        GridPane grid1 = new GridPane();
        grid1.setHgap(10);
        grid1.setVgap(10);
        grid1.setPadding(new Insets(0, 10, 0, 10));
        grid1.setAlignment(Pos.CENTER);
        
        //setting bg
        grid1.setBackground(bg1);

        //adding username
        Label l1 = new Label("Username: ");
        TextField tf1 = new TextField();
        grid1.add(l1, 0, 0, 1, 2); 
        grid1.add(tf1, 1, 0, 1, 2);
        Label l2 = new Label("Password: ");
        PasswordField tf2 = new PasswordField();
        grid1.add(l2, 0, 2, 1, 2); 
        grid1.add(tf2, 1, 2, 1, 2);  

        //login button
        Button b1 = new Button("Login");
        grid1.add(b1, 1, 4, 1, 1);
        b1.setOnAction(e -> {
            String userNameLOG = tf1.getText();
            String passwordLOG = tf2.getText();
            String userNameLOGDB;
            String passwordLOGDB;
            Connection conn;
            PreparedStatement sql;
            try{
                conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "UET@20se39");
                sql = conn.prepareStatement("SELECT USERNAME, USER_PASSWORD FROM P_USER");
                ResultSet rs1 = sql.executeQuery();
                System.out.println(rs1);
                while (rs1.next()){
                    userNameLOGDB = rs1.getString("USERNAME"); //fetch the values present in database
                    passwordLOGDB = rs1.getString("USER_PASSWORD");
                    if(userNameLOGDB.equals(userNameLOG) && passwordLOGDB.equals(passwordLOG)){
                        tf1.clear();
                        tf2.clear();
                        stage.setScene(movieSearchScene);
                        return;
                    }  
                }
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Error");
                alert.setContentText("Error! Username or Password is incorrect.");
                alert.showAndWait();
            } 
            catch (Exception ex){
                System.out.println(ex);
            }  
        });

        //signup text and link to sign up page
        Text t1 = new Text("Don't have an account?");
        grid1.add(t1, 1, 6, 1, 1);
        Hyperlink signuplink = new Hyperlink();
        signuplink.setText("Sign Up :)");
        signuplink.setOnAction(e -> {
            stage.setScene(signUpScene);
        });
        grid1.add(signuplink, 1, 7, 1, 1);
        
        Scene loginScene = new Scene(grid1, 1370, 770); 
        loginScene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());

        //go back to login from sign up
        Hyperlink loginlink = new Hyperlink();
        loginlink.setText("Go back to Login Page");
        loginlink.setOnAction(e -> {
            stage.setScene(loginScene);
        });
        grid2.add(loginlink, 1, 16, 1, 1);

        //log out button on MS scene
        Button logoutMSS = new Button("LOG OUT");
        grid3.add(logoutMSS, 115, 4, 1, 1);
        logoutMSS.setOnAction(e -> {
            stage.setScene(loginScene);
        });

        //log out button on SS scene
        Button logoutSSS = new Button("LOG OUT");
        grid6.add(logoutSSS, 120, 4, 1, 1);
        logoutSSS.setOnAction(e -> {
            stage.setScene(loginScene);
        });


        //Link to go to show search page from movie search page
        Hyperlink showlink = new Hyperlink();
        showlink.setText("Search by TV Show");
        showlink.setOnAction(e -> {
            stage.setScene(showSearchScene);
        });
        grid3.add(showlink, 110, 6, 6, 1); 

        //Link to go to movie search page from show search page
        Hyperlink movielink = new Hyperlink();
        movielink.setText("Search by Movie");
        movielink.setOnAction(e -> {
            stage.setScene(movieSearchScene);
        });
        grid6.add(movielink, 117, 6, 6, 1);

        stage.setTitle("Pelicula");       
        stage.setScene(loginScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
