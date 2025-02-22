package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dao.CartDAO;
import dao.ProductDAO;
import dao.SizeDAO;
import dto.ProductDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Cart;
import model.Product;
import model.Size;

/**
 * Servlet implementation class PaymentServlet
 */
@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet {
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

    /**
     * GETのフォワード先を決めるメソッド
     * 
     * @param  request
     * @return         rd
     */
    private RequestDispatcher getRouting(HttpServletRequest request) {
        //初期化
        String            url  = null;
        RequestDispatcher rd   = null;

        String            type = request.getParameter("type");

        if (type == null || "".equals(type)) {
            url = "WEB-INF/jsp/payment.jsp";
        }

        rd = request.getRequestDispatcher(url);
        return rd;
    }

    /**
     * POSTのフォワード先を決めるメソッド
     * 
     * @param  request
     * @return         rd
     */
    private RequestDispatcher postRouting(HttpServletRequest request) {
        //初期化
        String            url  = null;
        RequestDispatcher rd   = null;

        String            type = request.getParameter("type");

        if (type == null || "".equals(type)) {
            url = "PaymentServlet";
        }

        else if ("paymentConfirm".equals(type)) {
            CartDAO cartDAO           = new CartDAO();
            int     unsettledStatusId = 4;

            try {
                int              tableNumber    = Integer.parseInt(request.getParameter("tableNumber"));
                List<Cart>       cartList       = cartDAO.getAllByStatusIdAndTableNumber(unsettledStatusId, tableNumber);
                List<ProductDTO> productDTOList = new ArrayList<>();
                ProductDAO       productDAO     = new ProductDAO();
                SizeDAO          sizeDAO        = new SizeDAO();

                for (Cart cart : cartList) {
                    Product product            = productDAO.getProduct(cart.getProductId());
                    Size    size               = sizeDAO.getSize(cart.getSizeId());
                    String  formattedOrderTime = cart.getFormattedOrderDate(cart.getOrderTime());
                    int     adjustedPrice      = product.getPrice() + sizeDAO.getDifference(cart.getSizeId());

                    productDTOList.add(new ProductDTO(product, size, cart, formattedOrderTime, adjustedPrice));
                }

                if (productDTOList.size() > 0) {
                    int totalPrice = cartDAO.getPaymentTotalPrice(tableNumber);

                    request.setAttribute("totalPrice", totalPrice);
                    request.setAttribute("tableNumber", tableNumber);
                    request.setAttribute("productDTOList", productDTOList);
                    url = "WEB-INF/jsp/paymentConfirm.jsp";
                }
                else {
                    request.setAttribute("errorMessage", "存在しないテーブル番号または注文情報がありません");
                    url = "WEB-INF/jsp/payment.jsp";
                }
            }
            catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "値は半角数字で入力して下さい");
                url = "WEB-INF/jsp/payment.jsp";
            }

        }

        else if ("paymentComplete".equals(type)) {
            int        tableNumber = Integer.parseInt(request.getParameter("tableNumber"));
            CartDAO    cartDAO     = new CartDAO();
            List<Cart> cartList    = cartDAO.getAllByStatusIdAndTableNumber(4, tableNumber);
            for (Cart cart : cartList) {
                if (cartDAO.updateStatus(cart.getId(), 5)) {
                    url = "WEB-INF/jsp/paymentComplete.jsp";
                }

            }
        }

        rd = request.getRequestDispatcher(url);
        return rd;
    }

}
