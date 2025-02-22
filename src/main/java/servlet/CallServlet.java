package servlet;

import java.io.IOException;
import java.util.List;

import dao.CallDAO;
import dao.EventDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Call;
import model.Event;

/**
 * Servlet implementation class CallServlet
 */
@WebServlet("/CallServlet")
public class CallServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * doGet
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        EventDAO    eventDAO        = new EventDAO();
        List<Event> eventList       = eventDAO.getAllEvent();
        String      categoryIdParam = request.getParameter("categoryId");
        int         categoryId      = Integer.parseInt(categoryIdParam);

        request.setAttribute("eventList", eventList);
        request.setAttribute("categoryId", categoryId);
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/jsp/callStatus.jsp");
        rd.forward(request, response);
    }

    /**
     * doPost
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        String type = request.getParameter("type");

        if ("call".equals(type)) {
            int         eventId     = Integer.parseInt(request.getParameter("eventId"));
            HttpSession session     = request.getSession();
            int         tableNumber = (int)session.getAttribute("tableNumber");
            CallDAO     callDAO     = new CallDAO();
            if (callDAO.insertCalls(eventId, tableNumber, 2)) {
                Call call = callDAO.getLastCallsByTableNumber(tableNumber);
                request.setAttribute("call", call);
                RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/jsp/call.jsp");
                rd.forward(request, response);
            }
        }
        else if ("updateCallStatus".equals(type)) {
            System.out.println("test");
            int     id       = Integer.parseInt(request.getParameter("id"));
            int     statusId = Integer.parseInt(request.getParameter("statusId"));

            CallDAO callDAO  = new CallDAO();
            if (callDAO.updateCallStatus(id, statusId)) {
                System.out.println("callsテーブルステータス更新成功");
                response.sendRedirect("CookServlet?type=delivery");
            }
        }

        else if ("cancel".equals(type)) {
            int     id      = Integer.parseInt(request.getParameter("id"));
            CallDAO callDAO = new CallDAO();
            if (callDAO.updateCallStatus(id, 3)) {
                response.sendRedirect("OrderServlet");
            }
        }
    }

}
