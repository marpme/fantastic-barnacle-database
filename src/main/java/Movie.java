import com.sun.istack.internal.Nullable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Class description ...
 * Included in PACKAGE_NAME
 *
 * @author Marvin Piekarek (s0556014)
 * @version 1.0
 * @since 09. Jul 2017
 */
public class Movie {
    private SimpleIntegerProperty movieId = new SimpleIntegerProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    @Nullable
    private SimpleStringProperty description = new SimpleStringProperty("not available");

    public Movie(int movieId, String name, String description) {
        this.movieId.set(movieId);
        this.name.set(name);
        this.description.set(description);
    }

    @Override
    public String toString() {
        return name.get() + " (id=" + movieId.get() + ")";
    }

    public int getMovieId() {
        return movieId.get();
    }

    public SimpleIntegerProperty movieIdProperty() {
        return movieId;
    }
}
