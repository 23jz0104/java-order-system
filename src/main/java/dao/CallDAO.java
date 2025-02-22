package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Call;

public class CallDAO {
    /**
     * コンストラクタ
     */
    public CallDAO() {

    }

    /**
     * 呼び出しを追加する
     * 
     * @param  name
     * @param  tableNumber
     * @param  statusId
     * @return
     */
    public boolean insertCalls(int eventId, int tableNumber, int statusId) {
        String sql    = "INSERT INTO calls (event_id, table_number, status_id) VALUES (?, ?, ?)";
        int    result = 0;
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            stmt.setInt(2, tableNumber);
            stmt.setInt(3, statusId);

            result = stmt.executeUpdate();

            return result > 0;

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 最後に追加されたCallsテーブルのレコードをCall型で取得する
     * 
     * @param  tableNumber
     * @return
     */
    public Call getLastCallsByTableNumber(int tableNumber) {
        String sql = "String sql = \"SELECT * FROM calls WHERE table_number = ? ORDER BY id DESC LIMIT 1 ROWS ONLY";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, tableNumber);
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
     * 指定したstatusIdのイベントを取得
     * 
     * @param  statusId
     * @return
     */
    public List<Call> getCallsByStatusId(int statusId) {
        List<Call> eventList = new ArrayList<>();
        String     sql       = "SELECT * FROM calls WHERE status_id = ? ORDER BY created_at";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    eventList.add(rs2model(rs));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return eventList;
    }

    /**
     * スタッフ呼び出しステータス更新
     * 
     * @param  id
     * @param  statusId
     * @return
     */
    public boolean updateCallStatus(int id, int statusId) {
        String sql    = "UPDATE calls SET status_id = ? WHERE id = ?";
        int    result = 0;
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            stmt.setInt(2, id);

            result = stmt.executeUpdate();

            return result > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    /**
     * rs2model
     * 
     * @param  rs
     * @return
     */
    private Call rs2model(ResultSet rs) {
        try {
            int           id          = rs.getInt("id");
            int           eventId     = rs.getInt("event_id");
            int           tableNumber = rs.getInt("table_number");
            int           statusId    = rs.getInt("status_id");
            LocalDateTime createdAt   = rs.getTimestamp("created_at").toLocalDateTime();

            return new Call(id, eventId, tableNumber, statusId, createdAt);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
