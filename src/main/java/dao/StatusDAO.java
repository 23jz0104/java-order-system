package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Status;

public class StatusDAO {

    public StatusDAO() {
    }

    public Status getStatusById(int id) {
        String sql = "SELECT * FROM statuses WHERE id = ?";
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

    public List<Status> getAllStatus() {
        String       sql        = "SELECT * FROM statuses";
        List<Status> statusList = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                statusList.add(rs2model(rs));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Status rs2model(ResultSet rs) {
        try {
            int    id   = rs.getInt("id");
            String name = rs.getString("name");

            return new Status(id, name);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        StatusDAO s = new StatusDAO();
        System.out.println(s.getStatusById(1));
    }
}
