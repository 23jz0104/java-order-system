package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dao.CartDAO;
import dao.CategoryDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import dao.SizeDAO;
import dto.ProductDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.Category;
import model.Product;
import model.Size;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        RequestDispatcher rd = getRouting(request);
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        RequestDispatcher rd = postRouting(request);
        rd.forward(request, response);
    }

    /*------------------------------------------------GETとPOSTのルーティング---------------------------------------------*/

    /**
     * GETのフォワード先を決めるメソッド
     * 
     * @param  request
     * @return         RequestDispatcher rd フォワード先
     */
    private RequestDispatcher getRouting(HttpServletRequest request) {

        //初期化
        String            url  = null;
        RequestDispatcher rd   = null;

        //パラメータ取得
        String            type = request.getParameter("type");
        if (type == null) {
            type = "topPage";
        }

        //トップページに遷移
        if ("topPage".equals(type)) {
            setTopPage(request);
            setCartListTotalPrice(request);

            url = "WEB-INF/jsp/topPage.jsp";
        }

        //商品詳細ページに遷移
        else if ("product".equals(type)) {
            setProductInfo(request);

            url = "WEB-INF/jsp/productInfo.jsp";
        }

        //注文履歴ページに遷移
        else if ("orderHistory".equals(type)) {
            setOrderHistory(request);

            url = "WEB-INF/jsp/orderHistory.jsp";
        }
        else if ("checkoutRequest".equals(type)) {
            setTotalPrice(request);

            url = "WEB-INF/jsp/checkoutRequest.jsp";
        }

        rd = request.getRequestDispatcher(url);
        return rd;
    }

    /**
     * POSTのフォワード先を決めるメソッド
     * 
     * @param  request
     * @return         RequestDispatcher rd フォワード先
     */
    private RequestDispatcher postRouting(HttpServletRequest request) {

        //初期化
        RequestDispatcher rd   = null;
        String            url  = null;

        //パラメーター取得
        String            type = request.getParameter("type");

        //カートに商品を追加する処理
        if ("addCart".equals(type)) {
            addToCart(request);
            setTopPage(request);
            setCartListTotalPrice(request);

            url = "WEB-INF/jsp/topPage.jsp";
        }

        //カート内の商品の増減をする処理
        else if ("updateQuantity".equals(type)) {
            updateQuantity(request);
            setTopPage(request);
            setCartListTotalPrice(request);

            url = "WEB-INF/jsp/topPage.jsp";
        }

        //セッションで保持しているカートの中身をDBに登録(注文)する処理
        else if ("addCartToDB".equals(type)) {
            addCartToDB(request);
            setTopPage(request);
            setCartListTotalPrice(request);

            url = "WEB-INF/jsp/topPage.jsp";
        }
        else if ("checkoutRequestConfirmed".equals(type)) {
            checkoutRequestConfirmed(request);

            url = "WEB-INF/jsp/checkoutRequestConfirmed.jsp";
        }

        rd = request.getRequestDispatcher(url);
        return rd;
    }

    /*------------------------------------------------getRoutingメソッドから直接呼び出されるメソッド--------------------------------------------*/

    /**
     * カテゴリ一覧と選択されたカテゴリのメニュー一覧をリクエストスコープにセットするメソッド
     * 
     * @param request
     */
    private void setTopPage(HttpServletRequest request) {
        //初期化
        CategoryDAO    categoryDAO     = new CategoryDAO();
        List<Category> categoryList    = categoryDAO.getAll();
        int            categoryId;

        //パラメーター取得
        String         categoryIdParam = request.getParameter("categoryId");

        //初期ウィザードからのアクセス
        if (categoryIdParam == null) {
            categoryId = 1;
        }
        //商品詳細ページから戻る場合直前にいたカテゴリに復帰
        else {
            categoryId = Integer.parseInt(categoryIdParam);
        }

        //カテゴリとページ数に応じて商品リストをスコープにセット
        setPage(request, categoryId);

        request.setAttribute("categoryId", categoryId);
        request.setAttribute("categoryList", categoryList);
    }

    /**
     * 選択された商品の情報をスコープにList<ProductDTO>型でセット
     * 
     * @param request
     */
    private void setProductInfo(HttpServletRequest request) {
        //初期化
        ProductDAO  productDAO  = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        SizeDAO     sizeDAO     = new SizeDAO();
        List<Size>  sizeList    = new ArrayList<>();
        sizeList = sizeDAO.getAll();

        //パラメーター取得
        int              productId      = Integer.parseInt(request.getParameter("productId"));
        int              categoryId     = Integer.parseInt(request.getParameter("categoryId"));
        int              page           = Integer.parseInt(request.getParameter("page"));

        //DTOに変換するためにそれぞれのidのに応じたProductとCategoryを取得
        Product          product        = productDAO.getProduct(productId);
        Category         category       = categoryDAO.getCategory(categoryId);

        List<ProductDTO> productDTOList = new ArrayList<>();

        //Sizeの数だけ商品を異なるサイズ、金額で表示
        for (Size size : sizeList) {
            int        adjustedPrice = product.getPrice() + size.getDifference();
            ProductDTO productDTO    = new ProductDTO(product, category, size, adjustedPrice);
            productDTOList.add(productDTO);
        }

        request.setAttribute("productDTOList", productDTOList);
        request.setAttribute("page", page);
        request.setAttribute("categoryId", categoryId);
    }

    /**
     * 注文履歴をDBから取得しList<CartDTO>型でスコープにセットするメソッド
     * 
     * @param request
     */
    private void setOrderHistory(HttpServletRequest request) {
        //パラメーター取得
        int         categoryId  = Integer.parseInt(request.getParameter("categoryId"));

        //セッション取得
        HttpSession session     = request.getSession();
        int         tableNumber = (int)session.getAttribute("tableNumber");

        //初期化
        CartDAO     cartDAO     = new CartDAO();
        ProductDAO  productDAO  = new ProductDAO();
        SizeDAO     sizeDAO     = new SizeDAO();

        //List<Cart>型でtableNumberをもとにDBから注文履歴を取得
        List<Cart>  cartList    = new ArrayList<>();
        cartList = cartDAO.getOrderHistory(tableNumber);

        //取得したCartListをもとにDTOに変換
        List<ProductDTO> orderHistoryList = new ArrayList<>();
        ProductDTO       productDTO;

        //cartListをもとにcartの数だけList<ProductDTO>に追加
        for (Cart cart : cartList) {
            Product product       = productDAO.getProduct(cart.getProductId());
            Size    size          = sizeDAO.getSize(cart.getSizeId());
            int     quantity      = cart.getQuantity();
            int     adjustedPrice = productDAO.getProduct(cart.getProductId()).getPrice() + sizeDAO.getDifference(cart.getSizeId());
            productDTO = new ProductDTO(product, size, adjustedPrice, quantity);

            orderHistoryList.add(productDTO);
        }

        int totalPrice = cartDAO.getTotalPrice(tableNumber);

        Collections.reverse(orderHistoryList);

        request.setAttribute("categoryId", categoryId);
        request.setAttribute("totalPrice", totalPrice);
        request.setAttribute("orderHistoryList", orderHistoryList);
    }

    /*------------------------------------------------postRoutingメソッドから直接呼び出されるメソッド--------------------------------------------*/

    /**
     * カートリストを作成し、選択された商品をMap<String, ProductDTO>型でセッションスコープにセット
     * 
     * @param request
     */
    private void addToCart(HttpServletRequest request) {
        //初期化
        Product                 product     = new Product();
        Category                category    = new Category();
        Size                    size        = new Size();
        ProductDAO              productDAO  = new ProductDAO();
        CategoryDAO             categoryDAO = new CategoryDAO();
        SizeDAO                 sizeDAO     = new SizeDAO();
        ProductDTO              productDTO;

        //パラメータ取得
        int                     productId   = Integer.parseInt(request.getParameter("productId"));
        int                     categoryId  = Integer.parseInt(request.getParameter("categoryId"));
        String                  sizeIdParam = request.getParameter("sizeId");
        int                     quantity    = Integer.parseInt(request.getParameter("quantity"));
        int                     sizeId;

        //セッション取得
        HttpSession             session     = request.getSession();
        Map<String, ProductDTO> cartList    = (Map<String, ProductDTO>)session.getAttribute("cartList");

        //カートが存在していなければ新しく作成
        if (cartList == null) {
            cartList = new LinkedHashMap<>();
        }

        //それぞれのidをもとにオブジェクトを取得
        product  = productDAO.getProduct(productId);
        category = categoryDAO.getCategory(categoryId);

        //サイズが存在する商品であれば
        if (sizeIdParam != null) {
            sizeId = Integer.parseInt(sizeIdParam);
            size   = sizeDAO.getSize(sizeId);
        }

        //サイズが存在する商品の金額を取得
        int    adjustedPrice = product.getPrice() + size.getDifference();

        //商品idとサイズidをもとにMapのキーを作成
        String cartListKey   = productId + "-" + sizeIdParam;

        //同じキーの商品が追加された場合、すでにカートに存在する商品の個数を追加
        if (cartList.containsKey(cartListKey)) {
            productDTO = cartList.get(cartListKey);
            productDTO.setQuantity(productDTO.getQuantity() + quantity);
        }

        //同じキーの商品が存在していなければ、新しくオブジェクトを作成してcartListに追加
        else {
            productDTO = new ProductDTO(product, category, size, adjustedPrice, quantity);
            cartList.put(cartListKey, productDTO);
        }

        session.setAttribute("cartList", cartList);
    }

    /**
     * カートリストの値の増減を行うメソッド
     * Map<String, ProductDTO>のキーを使ってProductDTOを検索し、ProductDTOのquantityの値を調整する
     * 
     * @param request
     */
    private void updateQuantity(HttpServletRequest request) {
        //パラメーター取得
        String                  updateQuantity = request.getParameter("updateQuantity");
        String                  cartListKey    = request.getParameter("cartListKey");

        //セッション取得
        HttpSession             session        = request.getSession();
        Map<String, ProductDTO> cartList       = (Map<String, ProductDTO>)session.getAttribute("cartList");

        //cartListKeyをもとに商品を取得
        ProductDTO              productDTO     = cartList.get(cartListKey);

        //プラスが押された時の処理
        if ("increase".equals(updateQuantity)) {
            productDTO.setQuantity(productDTO.getQuantity() + 1);
        }

        //マイナスが押された時の処理
        else if ("decrease".equals(updateQuantity)) {

            //商品の数が0以下になったらcartListKeyのCartオブジェクトを削除
            if (productDTO.getQuantity() - 1 <= 0) {
                cartList.remove(cartListKey);
            }
            productDTO.setQuantity(productDTO.getQuantity() - 1);
        }

        session.setAttribute("cartList", cartList);
    }

    /**
     * セッションスコープにセットされているMap<String, ProductDTOList> cartListをcartsテーブル(DB)に保存
     * 
     * @param request
     */
    private void addCartToDB(HttpServletRequest request) {
        //セッション取得
        HttpSession             session     = request.getSession();
        Map<String, ProductDTO> cartList    = (Map<String, ProductDTO>)session.getAttribute("cartList");

        //パラメーター取得
        int                     tableNumber = (int)session.getAttribute("tableNumber");
        int                     categoryId  = Integer.parseInt(request.getParameter("categoryId"));

        //初期化
        OrderDAO                orderDAO    = new OrderDAO();
        CartDAO                 cartDAO     = new CartDAO();

        //cartsテーブルのorder_idを取得
        int                     orderId     = orderDAO.insertOrder(tableNumber);

        //orderIdを使用しセッションに登録されている商品すべてをDBに追加
        cartDAO.insertCart(orderId, cartList);

        //DB登録後、セッションに登録されていたcartListをセッションから削除
        session.removeAttribute("cartList");

        request.setAttribute("categoryId", categoryId);
    }

    /*------------------------------------------------その他メソッドから------------------------------------------------*/

    /**
     * セッションにセットされているcartListの合計値をリクエストスコープにセットするメソッド
     * 
     * @param request
     */
    private void setCartListTotalPrice(HttpServletRequest request) {
        //セッション取得
        HttpSession              session      = request.getSession();
        Map<Integer, ProductDTO> cartList     = (Map<Integer, ProductDTO>)session.getAttribute("cartList");

        int                      totalPrice   = 0;
        int                      totalProduct = 0;

        //セッションにcartListが存在していれば合計金額と合計件数を取得
        if (cartList != null) {
            for (ProductDTO productDTO : cartList.values()) {
                totalPrice   += productDTO.getAdjustedPrice() * productDTO.getQuantity();
                totalProduct += productDTO.getQuantity();
            }
        }

        request.setAttribute("totalPrice", totalPrice);
        request.setAttribute("totalProduct", totalProduct);
    }

    /**
     * 会計画面で表示する合計金額をtotalPriceとしてスコープにセット
     * 
     * @param request
     */
    private void setTotalPrice(HttpServletRequest request) {
        HttpSession session     = request.getSession();

        //パラメーター取得
        int         tableNumber = (int)session.getAttribute("tableNumber");
        int         categoryId  = Integer.parseInt(request.getParameter("categoryId"));

        //初期化
        CartDAO     cartDAO     = new CartDAO();

        int         totalPrice  = cartDAO.getTotalPrice(tableNumber);

        request.setAttribute("categoryId", categoryId);
        request.setAttribute("totalPrice", totalPrice);
    }

    /**
     * 支払いを確定する処理
     * 
     * @param request
     */
    private void checkoutRequestConfirmed(HttpServletRequest request) {
        HttpSession session     = request.getSession();

        //パラメータ取得
        int         tableNumber = (int)session.getAttribute("tableNumber");

        //初期化
        CartDAO     cartDAO     = new CartDAO();

        int         result      = cartDAO.paymentConfirmed(tableNumber);

        if (result > 0) {
            session.removeAttribute("tableNumber");
            request.setAttribute("message", "ありがとうございました。レジにてテーブル番号をお伝えいただき、お支払いをお願いします。");
        }
    }

    /**
     * カテゴリIDをもとにページ情報をセット
     * 
     * @param request
     * @param categoryId
     */
    private void setPage(HttpServletRequest request, int categoryId) {
        //初期化
        int        page;
        int        maxPage;
        ProductDAO productDAO = new ProductDAO();

        //パラメータ取得
        String     pageParam  = request.getParameter("page");

        //pageParamがなければ1を代入
        if (pageParam == null) {
            page = 1;
        }

        //pageParamを取得できればそのままint型に変換
        else {
            page = Integer.parseInt(pageParam);
        }

        //カテゴリIDをもとに最大ページ数を取得
        maxPage = productDAO.getMaxPage(categoryId);

        //カテゴリIDをもとに指定されたページの商品リストを取得
        List<Product> productList = productDAO.getAllWithPage(categoryId, page);

        request.setAttribute("productList", productList);
        request.setAttribute("page", page);
        request.setAttribute("maxPage", maxPage);
        request.setAttribute("prevPage", page > 1 ? page - 1 : maxPage);
        request.setAttribute("nextPage", page < maxPage ? page + 1 : 1);
    }

}
