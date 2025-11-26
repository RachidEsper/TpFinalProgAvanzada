package webfilter;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String ctx  = req.getContextPath();
        String path = req.getRequestURI().substring(ctx.length());

        // 1) Rutas públicas (no piden login)
        boolean recursoEstatico =
                   path.startsWith("/assets/")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/img/");

        boolean esLogin  = path.equals("/LoginServlet")
                        || path.equals("/views/auth/login.jsp");

        boolean esLogout = path.equals("/LogoutServlet");

        if (recursoEstatico || esLogin || esLogout) {
            chain.doFilter(request, response);
            return;
        }

        // 2) Rutas protegidas: verificar sesión
        HttpSession session = req.getSession(false);
        Object user = (session != null) ? session.getAttribute("user") : null;

        if (user == null) {
            // No logueado → al login
            res.sendRedirect(ctx + "/views/auth/login.jsp");
            return;
        }

        // 3) Usuario logueado → marcar respuesta como NO cacheable
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        res.setHeader("Pragma", "no-cache");                                   // HTTP 1.0
        res.setDateHeader("Expires", 0);                                       // Proxies

        // 4) Seguir al siguiente filtro / servlet
        chain.doFilter(request, response);
    }
}
