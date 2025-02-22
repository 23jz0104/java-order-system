package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import model.Order;

public class OrderDAO {

    /**
     * コンストラクタ
     */
    public OrderDAO() {
    }

    /**
     * Orderテーブルのすべてのレコードを取得
     * 
     * @return List<Order>型のリスト
     */
    public List<Order> getAll() {
        List<Order> orderList = new ArrayList<>();
        String      sql       = "SELECT * FROM orders";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                orderList.add(rs2model(rs));
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return orderList;
    }

    /**
     * オーダーをtableIdのテーブルに追加するメソッド
     * 
     * @param  tableId
     * @return         成功すればid
     */
    public int insertOrder(int tableId) {
        int    result  = 0;
        int    orderId = 0;

        String sql     = "INSERT INTO orders(table_number) VALUES (?)";

        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, tableId);
            result = stmt.executeUpdate();

            if (result > 0) {
                orderId = getLastInsertOrderId();
            }

        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return orderId;
    }

    /**
     * 最後に挿入されたidを取得するメソッド
     * 
     * @return 取得出来たらid
     */
    public int getLastInsertOrderId() {
        int    orderId = 0;

        String sql     = "SELECT id FROM orders ORDER BY id DESC LIMIT 1";

        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                orderId = rs.getInt("id");
            }
        }

        catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return orderId;
    }

    /**
     * OrderIdをもとにtableNumberを取得
     * 
     * @param  id
     * @return
     */
    public int getTableNumber(int id) {
        String sql = "SELECT table_number FROM orders WHERE id = ?";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("table_number");
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    /**
     * 指定のオーダーidに指定のステータスidがほかに存在しているかどうか 確認するメソッド
     * 
     * @param  orderId
     * @param  statusId
     * @return
     */
    public boolean hasInSameItems(int orderId, int statusId) {
        String sql    = "SELECT COUNT(*) FROM orders WHERE order_id = ? AND status_id = ?";
        int    result = 0;
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, statusId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt(1);
                    return result > 0;
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    /**
     * データベースの列の値をモデル型に変換
     * 
     * @param  rs 接続情報
     * @return    Orderモデル
     */
    private Order rs2model(ResultSet rs) {
        try {
            int  id          = rs.getInt("id");
            Time orderTime   = rs.getTime("order_time");
            int  tableNumber = rs.getInt("table_number");

            return new Order(id, orderTime, tableNumber);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //テスト用
    public static void main(String[] args) {
        OrderDAO orderDAO = new OrderDAO();

        int      result   = orderDAO.insertOrder(1);
        System.out.println(result);

//		System.out.println(orderDAO.getAll());
    }

}
