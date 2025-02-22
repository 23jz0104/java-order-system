package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Category;

public class CategoryDAO {

    /**
     * コンストラクタ
     */
    public CategoryDAO() {

    }

    /**
     * Productテーブルのデータすべてをリスト型で取得
     * 
     * @return List<Product>型のリスト
     */
    public List<Category> getAll() {
        List<Category> categoryList = new ArrayList<>();
        Category       category     = new Category();
        String         sql          = "SELECT * FROM categories";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                category = rs2model(rs);
                categoryList.add(category);
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryList;
    }

    /**
     * 指定したカテゴリを取得
     * 
     * @param  categoryId
     * @return            Category
     */
    public Category getCategory(int categoryId) {
        String sql = "SELECT * FROM categories WHERE id = ?";

        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);

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
     * データベースの列の値をモデル型に変換
     * 
     * @param  rs 接続情報
     * @return    Productモデル
     */
    private Category rs2model(ResultSet rs) {
        try {
            int    id   = rs.getInt("id");
            String name = rs.getString("name");

            return new Category(id, name);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //テスト用
    public static void main(String[] args) {
        List<Category> categoryList = new ArrayList<>();
        categoryList = new CategoryDAO().getAll();

        for (Category category : categoryList) {
            System.out.println(category.toString());
        }
    }
}
