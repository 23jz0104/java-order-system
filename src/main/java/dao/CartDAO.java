package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.ProductDTO;
import model.Cart;

public class CartDAO {

    /**
     * コンストラクタ
     */
    public CartDAO() {

    }

    /**
     * cartsテーブルのレコードをList<Cart>型で取得
     * 
     * @return List<Cart> cartList
     */
    public List<Cart> getAll() {
        List<Cart> cartList = new ArrayList<>();
        String     sql      = "SELECT * FROM carts";

        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cartList.add(rs2model(rs));
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return cartList;
    }

    /**
     * カートテーブルにレコードを追加
     * 
     * @param  orderId
     * @param  productId
     * @param  sizeId
     * @param  quantity
     * @return           追加できれば int 追加したCartレコードの数 / 使い出来なければ int 0
     */
    public int insertCart(int orderId, Map<String, ProductDTO> cartList) {
        int    result = 0;
        String sql    = "INSERT INTO carts (order_id, product_id, size_id, quantity, status_id, order_time) VALUES (?, ?, ?, ?, 1, ?)";

        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            for (ProductDTO productDTO : cartList.values()) {
                stmt.setInt(1, orderId);
                stmt.setInt(2, productDTO.getProduct().getId());

                if (productDTO.getSize().getId() != 0) {
                    stmt.setInt(3, productDTO.getSize().getId());
                }
                else {
                    stmt.setNull(3, java.sql.Types.INTEGER);
                }

                stmt.setInt(4, productDTO.getQuantity());
                stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

                result += stmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return result;
    }

    /**
     * tableNumberを引数に注文履歴を取得
     * 
     * @param  tableNumber
     * @return
     */
    public List<Cart> getOrderHistory(int tableNumber) {
        List<Cart> cartList = new ArrayList<>();
        String     sql      = "SELECT c.* FROM carts c "
                + "JOIN orders o ON c.order_id = o.id "
                + "WHERE c.status_id IN (1, 2, 3) AND o.table_number = ? ORDER BY c.id DESC";

        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, tableNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cartList.add(rs2model(rs));
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "getOrderHistory");
        }

        return cartList;
    }

    /**
     * 会計リクエスト前の合計金額をテーブルナンバーをもとに取得
     * 
     * @param  tableNumber
     * @return
     */
    public int getTotalPrice(int tableNumber) {
        int    totalPrice = 0;
        String sql        = "SELECT p.price, c.size_id, c.quantity FROM carts c "
                + "JOIN orders o ON c.order_id = o.id "
                + "JOIN products p ON c.product_id = p.id "
                + "LEFT JOIN sizes s ON c.size_id = s.id "
                + "WHERE c.status_id IN (1, 2, 3) AND o.table_number = ?";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, tableNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int price    = rs.getInt("price");
                    int sizeId   = rs.getInt("size_id");
                    int quantity = rs.getInt("quantity");
                    totalPrice += (price + new SizeDAO().getDifference(sizeId)) * quantity;
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return totalPrice;
    }

    /**
     * 会計リクエスト後の合計金額をテーブルナンバーをもとに取得
     * 
     * @param  tableNumber
     * @return
     */
    public int getPaymentTotalPrice(int tableNumber) {
        int    totalPrice = 0;
        String sql        = "SELECT p.price, c.size_id, c.quantity FROM carts c "
                + "JOIN orders o ON c.order_id = o.id "
                + "JOIN products p ON c.product_id = p.id "
                + "LEFT JOIN sizes s ON c.size_id = s.id "
                + "WHERE c.status_id = 4 AND o.table_number = ?";
        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, tableNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int price    = rs.getInt("price");
                    int sizeId   = rs.getInt("size_id");
                    int quantity = rs.getInt("quantity");
                    totalPrice += (price + new SizeDAO().getDifference(sizeId)) * quantity;
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return totalPrice;
    }

    /**
     * テーブル番号の商品を支払い確定(status_idを4)にする処理
     * 
     * @param  tableNumber
     * @return             変更できれば : 1 / 失敗の場合は : 0
     */
    public int paymentConfirmed(int tableNumber) {
        int    result = 0;

        String sql    = "UPDATE carts "
                + "SET status_id = 4 "
                + "WHERE order_id IN ("
                + "SELECT id FROM orders WHERE table_number = ? AND status_id != 5)";

        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, tableNumber);

            result = stmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * ステータスを調理完了に変更するメソッド
     * 
     * @param  id
     * @return
     */
    public boolean setCookingStatusToCompleted(int id) {
        int    result = 0;
        String sql    = "UPDATE carts SET status_id = 2 WHERE id = ?";

        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);

            result = stmt.executeUpdate();
            return result > 0;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    /**
     * ステータスを指定したIDに変更するメソッド.
     * 
     * @param  id
     * @return
     */
    public boolean updateStatus(int id, int statusId) {
        int    result = 0;
        String sql    = "UPDATE carts SET status_id = ? WHERE id = ?";

        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            stmt.setInt(2, id);

            result = stmt.executeUpdate();
            return result > 0;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public List<Cart> getItemsByOrderId(int orderId) {
        List<Cart> cartList = new ArrayList<>();
        String     sql      = "SELECT * FROM carts WHERE order_id = ?";

        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cartList.add(rs2model(rs));
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cartList;
    }

    /**
     * 指定したステータスIDのレコードをcartsテーブルから取得
     * 
     * @param  statusId
     * @return
     */
    public List<Cart> getAllByStatusId(int statusId) {
        List<Cart> cartList = new ArrayList<>();
        String     sql      = "SELECT * FROM carts WHERE status_id = ? ORDER BY order_time";

        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cartList.add(rs2model(rs));
                }
            }
        }
        catch (SQLException e) {
            System.out.println("getAllByStatusId(int statusId)でエラー" + e.getMessage());
        }

        return cartList;
    }

    /**
     * 指定したステータスIDとテーブルナンバーの商品をリスト型で取得
     * 
     * @param  statusId
     * @return
     */
    public List<Cart> getAllByStatusIdAndTableNumber(int statusId, int tableNumber) {
        List<Cart> cartList = new ArrayList<>();
        String     sql      = "SELECT c.* FROM carts c JOIN orders o ON c.order_id = o.id " +
                "WHERE c.status_id = ? AND o.table_number = ? ORDER BY order_time";

        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            stmt.setInt(2, tableNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cartList.add(rs2model(rs));
                }
            }
        }
        catch (SQLException e) {
            System.out.println("getAllByStatusIdAndTableNumber(int statusId, int tableNumber)でエラー" + e.getMessage());
        }

        return cartList;
    }

    //いらないかも
    public Map<Integer, List<Cart>> getAllItemsByStatusId(int statusId) {
        Map<Integer, List<Cart>> cartMap = new HashMap<>();
        Cart                     cart;
        String                   sql     = "SELECT * FROM carts WHERE order_id IN (SELECT DISTINCT order_id FROM carts WHERE status_id = ?)";

        try (Connection con = DBManager.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cart = rs2model(rs);
                    int orderId = cart.getOrderId();

                    //orderIdのkeyが存在すればそのkeyのMapのList<Cart>にCartを追加 / なければ新しく配列を作成しCartを代入
                    cartMap.computeIfAbsent(orderId, (key) -> new ArrayList<>()).add(cart);
                }
            }
        }
        catch (SQLException e) {
            System.out.println("getAllByStatusId(int statusId)でエラー" + e.getMessage());
        }

        return cartMap;
    }

    /**
     * a
     * 
     * @param  rs 接続情報
     * @return    Cartモデル
     */
    private Cart rs2model(ResultSet rs) {
        try {
            int           id        = rs.getInt("id");
            int           orderId   = rs.getInt("order_id");
            int           productId = rs.getInt("product_id");
            int           sizeId    = rs.getInt("size_id");
            int           quantity  = rs.getInt("quantity");
            int           statusId  = rs.getInt("status_id");
            LocalDateTime orderTime = rs.getTimestamp("order_time").toLocalDateTime();

            return new Cart(id, orderId, productId, sizeId, quantity, statusId, orderTime);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //テスト用
    public static void main(String[] args) {
        CartDAO cartDAO    = new CartDAO();

//		//商品挿入用
//		int orderId = 12;
//		int result;
//		
//		Product product = new ProductDAO().getProduct(6); //6番はライス
//		Category category = new CategoryDAO().getCategory(2); //2番はサイド
//		Size size = new Size();
//		ProductDTO productDTO = new ProductDTO(product, category, size, 1); 
//		Map<String, ProductDTO> cartList = new HashMap<>();
//		String cartListKey = product.getId() + "-" + size.getId();
//		cartList.put(cartListKey, productDTO);
//		
//		result = cartDAO.insertCart(orderId, cartList);
//		
//		System.out.println(result + "件のデータを挿入しました");
//		System.out.println("orderId = " + orderId + " " +  productDTO);

//		//注文履歴取得用
//		List<Cart> cartList = cartDAO.getOrderHistory(1);
//		
//		for(Cart cart : cartList) {
//			System.out.println(cart);
//		}
        int     totalPrice = cartDAO.getTotalPrice(1);
        System.out.println(totalPrice);
//        List<Cart> cartList = cartDAO.getAll();
//        System.out.println(cartList);
    }
}
