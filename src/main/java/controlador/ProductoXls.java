package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelos.Productos;
import servicios.ProductosServices;
import servicios.ProductosServiceImplement;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// Anotación
@WebServlet({"/producto.json", "/productohtml"})
public class ProductoXls extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Obtener los productos desde el servicio
        ProductosServices service = new ProductosServiceImplement();
        List<Productos> productos = service.listar();

        // Verificar si la URL es para JSON
        boolean conJSON = req.getServletPath().endsWith(".json");

        if (conJSON) {
            // Si la URL es para JSON, configuramos los encabezados para la descarga del JSON
            resp.setContentType("application/json");
            resp.setHeader("Content-Disposition", "attachment; filename=productos.json");

            // Crear el JSON

            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{\n  \"productos\": [\n");

            productos.forEach(p -> {
                jsonBuilder.append("    {\n")
                        .append("      \"idProducto\": ").append(p.getIdProducto()).append(",\n")
                        .append("      \"nombre\": \"").append(p.getNombre()).append("\",\n")
                        .append("      \"categoria\": \"").append(p.getCategoria()).append("\",\n")
                        .append("      \"precio\": ").append(p.getPrecio()).append("\n")
                        .append("    }");

                // Añadimos la coma si no es el último elemento
                if (productos.indexOf(p) < productos.size() - 1) {
                    jsonBuilder.append(",\n");
                }
            });

            jsonBuilder.append("\n  ]\n}");


            // Escribir el JSON en la respuesta
            resp.getWriter().write(jsonBuilder.toString());
        } else {
            // Si la URL no es para JSON, generar la página HTML
            resp.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = resp.getWriter()) {
                out.print("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta charset=\"utf-8\">");
                out.println("<title>Listar Producto</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Listar producto</h1>");
                out.println("<p><a href=\"producto.json\">Descargar JSON de Productos</a></p>");
                out.println("<table>");
                out.println("<tr>");
                out.println("<th>ID PRODUCTO</th>");
                out.println("<th>NOMBRE</th>");
                out.println("<th>CATEGORIA</th>");
                out.println("<th>PRECIO</th>");
                out.println("</tr>");

                // Iterar sobre los productos y mostrar en la tabla
                productos.forEach(p -> {
                    out.println("<tr>");
                    out.println("<td>" + p.getIdProducto() + "</td>");
                    out.println("<td>" + p.getNombre() + "</td>");
                    out.println("<td>" + p.getCategoria() + "</td>");
                    out.println("<td>" + p.getPrecio() + "</td>");
                    out.println("</tr>");
                });

                out.println("</table>");
                out.println("</body>");
                out.println("</html>");
            }
        }
    }
}
