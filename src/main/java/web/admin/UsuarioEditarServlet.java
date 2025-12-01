package web.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.TipoUsuario;
import model.Usuario;
import service.implementation.UsuarioServiceImpl;
import service.interfaces.IUsuarioService;

@WebServlet("/UsuarioEditarServlet")
public class UsuarioEditarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IUsuarioService usuarioService = new UsuarioServiceImpl();

    public UsuarioEditarServlet() {
        super();
    }

    /**
     * GET → muestra el formulario de edición con los datos del usuario.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String ctx = request.getContextPath();

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            // ID inválido → volvemos al listado
            HttpSession session = request.getSession();
            session.setAttribute("flashError", "ID de usuario inválido.");
            response.sendRedirect(ctx + "/UsuarioListarServlet");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            HttpSession session = request.getSession();
            session.setAttribute("flashError", "Formato de ID inválido.");
            response.sendRedirect(ctx + "/UsuarioListarServlet");
            return;
        }

        Usuario u = usuarioService.findById(id);

        if (u == null) {
            HttpSession session = request.getSession();
            session.setAttribute("flashError", "El usuario no existe.");
            response.sendRedirect(ctx + "/UsuarioListarServlet");
            return;
        }

        // Enviamos el usuario al JSP
        request.setAttribute("usuario", u);
        request.setAttribute("contentPage", "/views/admin/usuarioeditar.jsp");
        request.getRequestDispatcher("/views/base.jsp").forward(request, response);
    }

    /**
     * POST → procesa el formulario, valida y actualiza el usuario.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String ctx = request.getContextPath();

        // 1) Leer parámetros del form
        String idParam   = request.getParameter("id");
        String nombre    = request.getParameter("nombre");
        String email     = request.getParameter("email");
        String telefono  = request.getParameter("telefono");
        String tipoParam = request.getParameter("idTipoUsuario");
        String pass      = request.getParameter("password");

        // 2) Delegar validación y actualización al service
        Map<String, String> errors = new HashMap<>();

        Usuario actualizado = usuarioService.actualizarUsuarioDesdeFormulario(
                idParam,
                nombre,
                email,
                telefono,
                tipoParam,
                pass,
                errors
        );

        // 3) Si hubo problemas → volver al formulario
        if (actualizado == null) {

            if (!errors.isEmpty()) {
                // Mensaje global
                String global = errors.get("global");
                if (global == null) {
                    global = "Revisá los campos marcados.";
                }
                request.setAttribute("globalError", global);
                request.setAttribute("errors", errors);

                // Usuario "form" para repoblar campos con lo que el usuario escribió
                Usuario uForm = new Usuario();
                try {
                    uForm.setIdUsuario(Integer.parseInt(idParam));
                } catch (Exception ignored) {}

                uForm.setNombre(nombre);
                uForm.setEmail(email);
                uForm.setTelefono(telefono);

                if (tipoParam != null && !tipoParam.isBlank()) {
                    try {
                        TipoUsuario t = new TipoUsuario();
                        t.setIdTipoUsuario(Integer.parseInt(tipoParam));
                        uForm.setTipo(t);
                    } catch (NumberFormatException ignored) {}
                }

                request.setAttribute("usuario", uForm);
                request.setAttribute("contentPage", "/views/admin/usuarioeditar.jsp");
                request.getRequestDispatcher("/views/base.jsp").forward(request, response);
                return;
            }

            // Si no hay errores específicos pero no se actualizó → fallo raro
            HttpSession session = request.getSession();
            session.setAttribute("flashError", "No se pudo actualizar el usuario.");
            response.sendRedirect(ctx + "/UsuarioListarServlet");
            return;
        }

        // 4) OK → mensaje flash + redirect al listado
        HttpSession session = request.getSession();
        session.setAttribute("flashSuccess", "Usuario actualizado correctamente.");
        response.sendRedirect(ctx + "/UsuarioListarServlet");
    }
}
