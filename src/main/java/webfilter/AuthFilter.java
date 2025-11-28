package webfilter;

import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Usuario;   // 👈 importante

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

        // 1) RUTAS PÚBLICAS (no requieren login)
        boolean recursoEstatico =
                   path.startsWith("/assets/")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/img/");

        boolean esLogin  = path.equals("/LoginServlet")
                        || path.equals("/views/auth/login.jsp");

        boolean esLogout = path.equals("/LogoutServlet");

        boolean esRaiz   = path.equals("/") || path.isEmpty(); // opcional

        if (recursoEstatico || esLogin || esLogout || esRaiz) {
            chain.doFilter(request, response);
            return;
        }

        // 2) RUTAS PROTEGIDAS: verificar que haya sesión
        HttpSession session = req.getSession(false);
        Usuario user = (session != null)
                ? (Usuario) session.getAttribute("user")
                : null;

        if (user == null) {
            // No logueado → al login
            res.sendRedirect(ctx + "/views/auth/login.jsp");
            return;
        }

        // 3) Usuario logueado → calcular rol
        boolean isAdmin = false;
        if (user.getTipo() != null) {
            isAdmin = (user.getTipo().getIdTipoUsuario() == 1);
        }

        boolean adminOnly =
                path.startsWith("/PedidoCrudServlet")
             || path.startsWith("/PedidoEditarServlet")
             || path.startsWith("/PedidoEliminarServlet")
             || path.startsWith("/ProductoCrearServlet")
             || path.startsWith("/ProductoCrudServlet")
             || path.startsWith("/ProductoEditarServlet")
             || path.startsWith("/ProductoEliminarServlet")
             || path.startsWith("/UsuarioCrearServlet")
             || path.startsWith("/UsuarioCrudServlet")
             || path.startsWith("/UsuarioEditarServlet")
             || path.startsWith("/UsuarioEliminarServlet")
             || path.startsWith("/UsuarioListarServlet");

        if (adminOnly && !isAdmin) {
        	res.sendRedirect(ctx + "/views/base.jsp");
            return;
        }

        // 5) Marcar la respuesta como NO cacheable (para que no vuelva con el back del navegador)
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        res.setHeader("Pragma", "no-cache");                                   // HTTP 1.0
        res.setDateHeader("Expires", 0);                                       // Proxies

        // 6) Seguir con la cadena
        chain.doFilter(request, response);
    }
}
