import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Class description ...
 * Included in PACKAGE_NAME
 *
 * @author Marvin Piekarek (s0556014)
 * @version 1.0
 * @since 03. Jul 2017
 */
public class Screening {
    SimpleIntegerProperty id;
    SimpleObjectProperty<LocalDateTime> datetime;
    SimpleIntegerProperty movie_id;
    SimpleStringProperty name;
    SimpleStringProperty description;

    public Screening(ResultSet rs) throws SQLException {
        this.id = new SimpleIntegerProperty(rs.getInt("id"));
        this.datetime = new SimpleObjectProperty<LocalDateTime>(rs.getTimestamp("datetime").toLocalDateTime());
        this.movie_id = new SimpleIntegerProperty(rs.getInt("movie_id"));
        this.name = new SimpleStringProperty(rs.getString("name"));
        this.description = new SimpleStringProperty(rs.getString("description"));
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public LocalDateTime getDatetime() {
        return datetime.get();
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime.set(datetime);
    }

    public SimpleObjectProperty<LocalDateTime> datetimeProperty() {
        return datetime;
    }

    public int getMovie_id() {
        return movie_id.get();
    }

    public SimpleIntegerProperty movie_idProperty() {
        return movie_id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get().equals("") ? "Not available." : description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }
}
