package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Event;

public class EventDAO {
    /**
     * コンストラクタ
     */
    public EventDAO() {

    }

    /**
     * Eventレコードをリスト型ですべて取得
     * 
     * @return List<Event> eventList
     */
    public List<Event> getAllEvent() {
        List<Event> eventList = new ArrayList<>();
        String      sql       = "SELECT * FROM events";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                eventList.add(rs2model(rs));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return eventList;
    }

    /**
     * イベントステータスを完了に変更
     * 
     * @param  id
     * @return
     */
    public boolean updateEventStatus(int id) {
        String sql = "UPDATE events SET status_id = 3 WHERE id = ?";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int result = stmt.executeUpdate();
            return result > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 
     * @param  id
     * @return
     */
    public Event getEventById(int id) {
        String sql = "SELECT * FROM events WHERE id = ?";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs2model(rs);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * rs2model
     * 
     * @param  rs
     * @return
     */
    private Event rs2model(ResultSet rs) {
        try {
            int    id   = rs.getInt("id");
            String name = rs.getString("name");

            return new Event(id, name);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
