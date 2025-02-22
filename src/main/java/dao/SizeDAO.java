package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Size;

public class SizeDAO {
    /**
     * コンストラクタ
     */
    public SizeDAO() {
    }

    /**
     * Sizeをすべて取得
     * 
     * @return List<Size>モデル
     */
    public List<Size> getAll() {
        List<Size> sizeList = new ArrayList<>();
        String     sql      = "SELECT * FROM sizes";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                sizeList.add(rs2model(rs));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return sizeList;
    }

    /**
     * Sizeモデルを取得
     * 
     * @param  sizeId
     * @return        Sizeモデル
     */
    public Size getSize(int sizeId) {
        String sql = "SELECT * FROM Sizes WHERE id = ?";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, sizeId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs2model(rs);
                }
            }
        }

        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * サイズに応じた差額を取得
     * 
     * @param  sizeId
     * @return        int型でサイズに応した金額 / 見つからなければ0
     */
    public int getDifference(int sizeId) {
        String sql = "SELECT difference FROM Sizes WHERE id = ?";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, sizeId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("difference");
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    /**
     * データベースの列の値をモデル型に変換
     * 
     * @param  rs 接続情報
     * @return    Sizeモデル
     */
    private Size rs2model(ResultSet rs) {
        try {
            int    id         = rs.getInt("id");
            String name       = rs.getString("name");
            int    difference = rs.getInt("difference");

            return new Size(id, name, difference);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //テスト用
    public static void main(String[] args) {
        List<Size> sizeList = new ArrayList<>();

        sizeList = new SizeDAO().getAll();

        System.out.println(sizeList);
    }
}
