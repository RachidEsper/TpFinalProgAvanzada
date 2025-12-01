package web.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TipoUsuario;
import model.Usuario;

import java.io.IOException;

import dao.implementation.UsuarioDAOImpl;
import dao.interfaces.IUsuarioDAO;
import service.implementation.UsuarioServiceImpl;
import service.interfaces.IUsuarioService;
/**
 * Servlet implementation class UsuarioCrearServlet
 */
@WebServlet("/UsuarioCrearServlet")
public class UsuarioCrearServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UsuarioCrearServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("contentPage", "/views/admin/registrousuarios.jsp");
        request.getRequestDispatcher("/views/base.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String password = request.getParameter("password");
        String idTipoUsuarioStr = request.getParameter("idTipoUsuario");

        try {
            int idTipoUsuario = 0;
            if (idTipoUsuarioStr != null && !idTipoUsuarioStr.trim().isEmpty()) {
                idTipoUsuario = Integer.parseInt(idTipoUsuarioStr);
            }

            IUsuarioService usuarioService = new UsuarioServiceImpl();
            Usuario usuario = usuarioService.createUsuario(
                    nombre,
                    email,
                    password,
                    telefono,
                    idTipoUsuario
            );

            System.out.println("Usuario creado: " + usuario.getNombre());

            // Redirigir al servlet de listado, no al JSP
            request.getSession().setAttribute("flashSuccess", "Usuario registrado correctamente.");
            response.sendRedirect(request.getContextPath() + "/UsuarioListarServlet");

        } catch (NumberFormatException e) {
            // Tipo de usuario inválido
            request.setAttribute("globalError", "El tipo de usuario no es válido.");
            request.setAttribute("nombre", nombre);
            request.setAttribute("email", email);
            request.setAttribute("telefono", telefono);
            request.setAttribute("idTipoUsuario", idTipoUsuarioStr);

            //  Volver al layout con el formulario dentro
            request.setAttribute("contentPage", "/views/admin/registrousuarios.jsp");
            request.getRequestDispatcher("/views/base.jsp").forward(request, response);

        } catch (exception.BusinessException e) {
            // Error de negocio desde el service
            request.setAttribute("globalError", e.getMessage());
            request.setAttribute("nombre", nombre);
            request.setAttribute("email", email);
            request.setAttribute("telefono", telefono);
            request.setAttribute("idTipoUsuario", idTipoUsuarioStr);

            request.setAttribute("contentPage", "/views/admin/registrousuarios.jsp");
            request.getRequestDispatcher("/views/base.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("globalError", "Error interno del servidor. Por favor, intente nuevamente.");
            request.setAttribute("nombre", nombre);
            request.setAttribute("email", email);
            request.setAttribute("telefono", telefono);
            request.setAttribute("idTipoUsuario", idTipoUsuarioStr);

            request.setAttribute("contentPage", "/views/admin/registrousuarios.jsp");
            request.getRequestDispatcher("/views/base.jsp").forward(request, response);
        }
    }
}
