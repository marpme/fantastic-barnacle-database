import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Class description ...
 * Included in PACKAGE_NAME
 *
 * @author Marvin Piekarek (s0556014)
 * @version 1.0
 * @since 03. Jul 2017
 */
public class CinemaHandler {

    public static int removeScreeningRow(int id) throws SQLException {
        Connection st;
        Alert alert = new Alert(Alert.AlertType.WARNING, "Could update the data inside the database. Reverting changes locally.");
        st = Connector.getInstance().getConn();
        PreparedStatement ps = st.prepareStatement("DELETE FROM screening WHERE id = ?");
        ps.setInt(1, id);
        int k = ps.executeUpdate();
        st.commit();
        return k;
    }

    public static class DateEventHandler implements
            EventHandler<TableColumn.CellEditEvent<Screening, LocalDateTime>> {

        /**
         * Invoked when a specific event of the type for which this handler is
         * registered happens.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(TableColumn.CellEditEvent<Screening, LocalDateTime> event) {
            Connection st;
            Alert alert = new Alert(Alert.AlertType.WARNING, "Could update the data inside the database. Reverting changes locally.");
            try {
                st = Connector.getInstance().getConn();
                PreparedStatement ps = st.prepareStatement("UPDATE screening SET datetime = ? WHERE id = ?");
                ps.setTimestamp(1, Timestamp.valueOf(event.getNewValue()));
                ps.setInt(2, event.getRowValue().getId());
                int k = ps.executeUpdate();
                st.commit();
                if (k == 1) {
                    event.getRowValue().setDatetime(event.getNewValue());
                } else {
                    alert.show();
                    event.getTableView().refresh();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static class MovieNameHandler implements
            EventHandler<TableColumn.CellEditEvent<Screening, String>> {

        /**
         * Invoked when a specific event of the type for which this handler is
         * registered happens.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(TableColumn.CellEditEvent<Screening, String> event) {
            Connection st;
            Alert alert = new Alert(Alert.AlertType.WARNING, "Could update the data inside the database. Reverting changes locally.");
            try {
                st = Connector.getInstance().getConn();
                PreparedStatement ps = st.prepareStatement("UPDATE movie SET name = ? WHERE id = ?");
                ps.setString(1, event.getNewValue());
                ps.setInt(2, event.getRowValue().getMovie_id());
                int k = ps.executeUpdate();
                st.commit();
                if (k == 1) {
                    event.getRowValue().setName(event.getNewValue());
                } else {
                    alert.show();
                    event.getTableView().refresh();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static class MovieDescriptionHandler implements
            EventHandler<TableColumn.CellEditEvent<Screening, String>> {

        /**
         * Invoked when a specific event of the type for which this handler is
         * registered happens.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(TableColumn.CellEditEvent<Screening, String> event) {
            Connection st;
            Alert alert = new Alert(Alert.AlertType.WARNING, "Could update the data inside the database. Reverting changes locally.");
            try {
                st = Connector.getInstance().getConn();
                PreparedStatement ps = st.prepareStatement("UPDATE movie SET description = ? WHERE id = ?");
                ps.setString(1, event.getNewValue());
                ps.setInt(2, event.getRowValue().getMovie_id());
                int k = ps.executeUpdate();
                st.commit();
                if (k == 1) {
                    event.getRowValue().setDescription(event.getNewValue());
                } else {
                    alert.show();
                    event.getTableView().refresh();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
