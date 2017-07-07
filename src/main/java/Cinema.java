import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

/**
 * Class description ...
 * Included in PACKAGE_NAME
 *
 * @author Marvin Piekarek (s0556014)
 * @version 1.0
 * @since 03. Jul 2017
 */
public class Cinema extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Group());
        primaryStage.setWidth(800);
        primaryStage.setHeight(320);

        ObservableList<Screening> items = createInitialTableItems();
        TableView<Screening> table = createTableView(items);

        Label label = new Label("cinema screening table");
        label.setFont(new Font("Arial", 20));

        final VBox vbox = new VBox();
        VBox.setVgrow(table, Priority.ALWAYS);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table);

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
    private TableView<Screening> createTableView(ObservableList<Screening> items) {
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

        table.setItems(items);
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

    private ObservableList<Screening> createInitialTableItems() throws SQLException {
        ObservableList<Screening> items = FXCollections.observableArrayList();
        Statement st = Connector.getInstance().getConn().createStatement();
        st.setFetchSize(10);
        ResultSet rs = st.executeQuery("SELECT * FROM screening NATURAL JOIN movie;");
        while (rs.next()) {
            items.add(new Screening(rs));
        }
        rs.close();
        return items;
    }

}
