import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Class description ...
 * Included in PACKAGE_NAME
 *
 * @author Marvin Piekarek (s0556014)
 * @version 1.0
 * @since 03. Jul 2017
 */
public class Cinema extends Application {

    private TableView<Screening> table;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Group());
        primaryStage.setWidth(800);
        primaryStage.setHeight(380);

        this.table = createTableView();
        refreshTableItems();

        Label label = new Label("cinema screening table");
        label.setFont(new Font("Arial", 20));


        final HBox hbox = new HBox();
        hbox.setHgrow(table, Priority.ALWAYS);
        hbox.setAlignment(Pos.BASELINE_CENTER);
        hbox.setSpacing(5);
        hbox.setPadding(new Insets(10, 10, 10, 10));

        Label date = new Label("Datetime:");
        Label movie = new Label("Movie:");

        DateTimePicker dtp = new DateTimePicker();
        ObservableList<Movie> movies = createInitialMovieItems();
        ComboBox<Movie> combo = new ComboBox<>(movies);
        Button insertButton = new Button("Insert!");
        insertButton.setOnAction(new InsertHandler(combo, dtp));
        hbox.getChildren().addAll(date, dtp, movie, combo, insertButton);

        final VBox vbox = new VBox();
        VBox.setVgrow(table, Priority.ALWAYS);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table, hbox);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Screenings ...");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            try {
                Connector.getInstance().finallyClose();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private TableView<Screening> createTableView() {
        TableView<Screening> table = new TableView<>();
        TableColumn<Screening, Integer> id = new TableColumn<>("ID");
        TableColumn<Screening, LocalDateTime> datetime = new TableColumn<>("Datetime");
        TableColumn<Screening, String> movie = new TableColumn<>("Movie");

        TableColumn<Screening, Integer> movie_id = new TableColumn<>("ID");
        TableColumn<Screening, String> name = new TableColumn<>("Name");
        TableColumn<Screening, String> description = new TableColumn<>("Description");

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        movie_id.setCellValueFactory(new PropertyValueFactory<>("movie_id"));

        datetime.setCellValueFactory(new PropertyValueFactory<>("datetime"));
        datetime.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateTimeStringConverter()));
        datetime.setOnEditCommit(new CinemaHandler.DateEventHandler());


        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(new CinemaHandler.MovieNameHandler());

        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        description.setCellFactory(TextFieldTableCell.forTableColumn());
        description.setOnEditCommit(new CinemaHandler.MovieDescriptionHandler());

        movie.getColumns().addAll(movie_id, name, description);
        table.getColumns().addAll(id, datetime, movie);

        table.setEditable(true);

        table.setPrefSize(780, 250);
        table.setRowFactory(tableView -> {
            final TableRow<Screening> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem removeItem = new MenuItem("Delete");
            removeItem.setOnAction(event -> {
                try {
                    int removed = CinemaHandler.removeScreeningRow(row.getItem().getId());
                    if (removed == 1) table.getItems().remove(row.getItem());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            rowMenu.getItems().addAll(removeItem);

            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu) null));
            return row;
        });

        return table;
    }

    public void refreshTableItems() throws SQLException {
        ObservableList<Screening> items = FXCollections.observableArrayList();
        Statement st = Connector.getInstance().getConn().createStatement();
        st.setFetchSize(10);
        ResultSet rs = st.executeQuery("SELECT * FROM screening CROSS JOIN movie WHERE movie_id = movie.id;");
        while (rs.next()) {
            items.add(new Screening(rs));
        }
        rs.close();
        this.table.setItems(items);
        this.table.refresh();
    }

    private ObservableList<Movie> createInitialMovieItems() throws SQLException {
        ObservableList<Movie> items = FXCollections.observableArrayList();
        Statement st = Connector.getInstance().getConn().createStatement();
        st.setFetchSize(10);
        ResultSet rs = st.executeQuery("SELECT * FROM movie;");
        while (rs.next()) {
            items.add(new Movie(rs.getInt("id"), rs.getString("name"), rs.getString("description")));
        }
        rs.close();
        return items;
    }

    private class InsertHandler implements EventHandler<ActionEvent> {

        private ComboBox<Movie> movies;
        private DateTimePicker dateTimePicker;

        public InsertHandler(ComboBox<Movie> movies, DateTimePicker dateTimePicker) {
            this.movies = movies;
            this.dateTimePicker = dateTimePicker;
        }

        /**
         * Invoked when a specific event of the type for which this handler is
         * registered happens.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(ActionEvent event) {
            if(!verifyInputs()) return;

            Connection st;
            try{
                st = Connector.getInstance().getConn();
                PreparedStatement ps = st.prepareStatement("INSERT INTO screening (datetime, movie_id, theater_id) VALUES (?, ?, ?)");
                ps.setTimestamp(1, Timestamp.valueOf(dateTimePicker.getDateTimeValue()));
                ps.setInt(2, movies.getSelectionModel().getSelectedItem().getMovieId());
                ps.setInt(3, 3);
                int k = ps.executeUpdate();
                st.commit();
                refreshTableItems();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private boolean verifyInputs(){
            if(ChronoUnit.SECONDS.between(dateTimePicker.getDateTimeValue(), LocalDateTime.now()) >= 0){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Your screening date/time should be in the future. Reverting changes ...");
                alert.show();
                return false;
            } else if(movies.getSelectionModel().isEmpty() || movies.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a valid movie before inserting the screening event. Reverting changes ...");
                alert.show();
                return false;
            }

            return true;
        }
    }
}
