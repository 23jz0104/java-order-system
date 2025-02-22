package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.CallDAO;
import dao.CartDAO;
import dao.CategoryDAO;
import dao.EventDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import dao.SizeDAO;
import dao.StatusDAO;
import dto.CallDTO;
import dto.ProductDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Call;
import model.Cart;
import model.Category;
import model.Event;
import model.Product;
import model.Size;
import model.Status;

/**
 * Servlet implementation class CookServlet
 * Carts内のstatusのパラメータが1
 * 
 */
@WebServlet("/CookServlet")
public class CookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        request.setCharacterEncoding("utf-8");

        RequestDispatcher rd = getRouting(request);
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        request.setCharacterEncoding("utf-8");

        RequestDispatcher rd = postRouting(request);
        rd.forward(request, response);
    }

    /*------------------------------------------------GETとPOSTのルーティング--------------------------------------------*/

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
            type = "cook";
        }

        // トップページに遷移 
        if ("cook".equals(type)) {
            setOrderHistory(request, 1);

            url = "WEB-INF/jsp/cook.jsp";
        }

        //商品詳細ページに遷移
        else if ("delivery".equals(type)) {
            setOrderHistory(request, 2);

            url = "WEB-INF/jsp/delivery.jsp";
        }

        //注文履歴ページに遷移
        else if ("orderHistory".equals(type)) {
            setOrderHistory(request, 3);

            url = "WEB-INF/jsp/orderHistoryEmployee.jsp";
        }

        rd = request.getRequestDispatcher(url);
        return rd;
    }

    /**
     * POSTのフォワード先を決めるメソッド
     * 
     * @param  request リクエスト
     * @return         RequestDispatcher rd フォワード先
     */
    private RequestDispatcher postRouting(HttpServletRequest request) {

        //初期化
        RequestDispatcher rd           = null;
        String            url          = null;
        int               targetStatus = 0;

        //パラメーター取得
        String            type         = request.getParameter("type");

        //商品を調理完了に変更
        if ("setCookingStatusToCompleted".equals(type)) {
            targetStatus = 2;
            updateStatus(request, targetStatus);

            url = "WEB-INF/jsp/cook.jsp";
        }
        //商品を配膳済みに変更
        else if ("setDeliveryStatusToCompleted".equals(type)) {
            targetStatus = 3;
            updateStatus(request, targetStatus);

            url = "WEB-INF/jsp/delivery.jsp";
        }
        else if ("setEventStatusToCompleted".equals(type)) {
            updateEventStatusComplete(request);

            url = "WEB-INF/jsp/delivery.jsp";
        }

        rd = request.getRequestDispatcher(url);
        return rd;
    }

    private void setOrderHistory(HttpServletRequest request, int statusId) {
        int                      tableNumber;
        CartDAO                  cartDAO        = new CartDAO();
        ProductDAO               productDAO     = new ProductDAO();
        CategoryDAO              categoryDAO    = new CategoryDAO();
        SizeDAO                  sizeDAO        = new SizeDAO();
        OrderDAO                 orderDAO       = new OrderDAO();
        StatusDAO                statusDAO      = new StatusDAO();
        CallDAO                  callDAO        = new CallDAO();
        EventDAO                 eventDAO       = new EventDAO();

        Map<Integer, List<Cart>> cartMap        = cartDAO.getAllItemsByStatusId(statusId);
        List<ProductDTO>         productDTOList = new ArrayList<>();

        for (Map.Entry<Integer, List<Cart>> entry : cartMap.entrySet()) {
            List<Cart> cartList = entry.getValue();

            for (Cart cart : cartList) {
                Product  product  = productDAO.getProduct(cart.getProductId());
                Category category = categoryDAO.getCategory(product.getCategoryId());
                Size     size     = sizeDAO.getSize(cart.getSizeId());
                Status   status   = statusDAO.getStatusById(cart.getStatusId());
                tableNumber = orderDAO.getTableNumber(cart.getOrderId());
                String formattedOrderDate = cart.getFormattedOrderDate(cart.getOrderTime());
                productDTOList.add(new ProductDTO(product, category, size, cart, status, formattedOrderDate, tableNumber));
            }
        }

        List<Call>    callList    = callDAO.getCallsByStatusId(statusId);
        List<CallDTO> callDTOList = new ArrayList<>();

        for (Call call : callList) {
            Event  event              = eventDAO.getEventById(call.getEventId());
            Status status             = statusDAO.getStatusById(call.getStatusId());
            String formattedOrderDate = call.getFormattedOrderDate(call.getCreatedAt());

            callDTOList.add(new CallDTO(call, event, status, formattedOrderDate));
        }

        request.setAttribute("productDTOList", productDTOList);
        request.setAttribute("callDTOList", callDTOList);

    }

    /**
     * イベントのステータスを対応済みに変更
     * 
     * @param request
     */
    private void updateEventStatusComplete(HttpServletRequest request) {
        int eventId = Integer.parseInt(request.getParameter("eventId"));
        if (new EventDAO().updateEventStatus(eventId)) {
            setOrderHistory(request, 2);
        }
    }

    /**
     * status_idを変更
     * 
     * @param request
     */
    private void updateStatus(HttpServletRequest request, int targetStatus) {
        CartDAO cartDAO  = new CartDAO();
        int     id       = Integer.parseInt(request.getParameter("id"));
        int     statusId = Integer.parseInt(request.getParameter("statusId"));

        boolean result   = cartDAO.updateStatus(id, targetStatus);
        System.out.println(result);

        if (result) {
            setOrderHistory(request, statusId);
        }
    }
}
