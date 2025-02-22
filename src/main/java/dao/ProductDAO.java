package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Product;

/**
 * productテーブル アクセス用DAO
 */

public class ProductDAO {
    private final int PRODUCT_PER_PAGE = 6;

    /**
     * コンストラクタ
     */
    public ProductDAO() {

    }

    /**
     * Productテーブルのデータすべてをリスト型で取得
     * 
     * @return List<Product>型のリスト
     */
    public List<Product> getAll() {
        List<Product> productList = new ArrayList<>();
        String        sql         = "SELECT * FROM products";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                productList.add(rs2model(rs));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }

    /**
     * 指定されたカテゴリIDの商品をページ単位で取得(最大6個)
     * 
     * @param  categoryId
     * @param  page
     * @return            指定されたカテゴリの商品 List<Category> categoryList
     */
    public List<Product> getAllWithPage(int categoryId, int page) {
        List<Product> productList = new ArrayList<>();

        //ページ番号指定
        int           start       = (page - 1) * PRODUCT_PER_PAGE; //スタート位置
        int           count       = PRODUCT_PER_PAGE;
        String        sql         = "SELECT * FROM products WHERE category_id = ? LIMIT ? OFFSET ?";

        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            stmt.setInt(3, start);
            stmt.setInt(2, count);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productList.add(rs2model(rs));
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return productList;
    }

    /**
     * Productテーブルから指定されたcategory_idのデータをリスト型で取得
     * 
     * @param  categoryId 指定するカテゴリのid
     * @return            List<Product>型のリスト
     */
    public List<Product> getAllWithCategory(int categoryId) {
        List<Product> productList = new ArrayList<>();
        String        sql         = "SELECT * FROM products WHERE category_id = ?";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productList.add(rs2model(rs));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }

    /**
     * Productテーブルの指定されたidの商品をProduct型で取得
     * 存在しなければnull
     * 
     * @param  id
     * @return    Product
     */
    public Product getProduct(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
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
     * 指定されたカテゴリIDの商品数を取得
     * 
     * @param  categoryId
     * @return
     */
    public int countProduct(int categoryId) {
        int    cnt = -1;
        String sql = "SELECT COUNT(*) FROM products WHERE category_id = ?";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cnt = rs.getInt(1);
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cnt;
    }

    /**
     * categoryIdの最大ページ数を取得
     * 
     * @param  categoryId
     * @return            int ページ数
     */
    public int getMaxPage(int categoryId) {
        int productCount = countProduct(categoryId);

        return (productCount % PRODUCT_PER_PAGE == 0) ? productCount / PRODUCT_PER_PAGE : productCount / PRODUCT_PER_PAGE + 1;
    }

    /**
     * データベースの列の値をモデル型に変換
     * 
     * @param  rs 接続情報
     * @return    Productモデル
     */
    private Product rs2model(ResultSet rs) {
        try {
            int    id         = rs.getInt("id");
            String name       = rs.getString("name");
            int    price      = rs.getInt("price");
            int    categoryId = rs.getInt("category_id");
            String image      = rs.getString("image");
            String supplement = rs.getString("supplement");

            return new Product(id, name, price, categoryId, image, supplement);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //テスト用
    public static void main(String[] args) {
        ProductDAO    productDAO  = new ProductDAO();
        List<Product> productList = new ArrayList<>();
//		Product product = new Product();

//		//すべて取得処理
        productList = productDAO.getAll();
        System.out.println(productList);
//		
//		productList.removeAll(productList);

        //ページ番号に応じた商品を最大6個取得
        productList = productDAO.getAllWithPage(4, 1); //番カテゴリのページ目を取得
        System.out.println(productList);
//		
//		//カテゴリの商品数を取得
//		System.out.println(productDAO.countProduct(1));

        //カテゴリの最大ページ数を表示
        System.out.println(productDAO.getMaxPage(1));
    }
}
