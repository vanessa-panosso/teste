package vanessa.panosso.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.awt.image.PixelConverter.Bgrx;

/**
 * Servlet implementation class GeradorVetor
 */
@WebServlet(name = "Mostrar Servlet", urlPatterns = "/gerar")
public class GeradorVetor extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private List<BigDecimal> valores = new ArrayList<>();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GeradorVetor() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
	    String valor = request.getParameter("valor");
	    String somar = request.getParameter("tipo");

	    Integer numero = Integer.parseInt(valor);
	    writer.println("<table> <tr><th>Valor</th></tr>");
	    for (int i = 1; i < 11; i++) {
	    	 if (i % 3 == 0) {
	    		 BigDecimal valorTotal = new BigDecimal(i * 0.3 * numero);
	    		 writer.println("<tr><td>" + valorTotal.setScale(0, BigDecimal.ROUND_HALF_EVEN)  + "</td></tr>");
	    		 valores.add(valorTotal);
	    	 } else {
	    		 BigDecimal valorTotal = new BigDecimal(i * 0.1 * numero);
	    		 valores.add(valorTotal);

	    		 writer.println("<tr><td>" + valorTotal + "</td></tr>");
	    	 }
	    }
		writer.println("</table>");
		
		String somaTotal = somarValores(somar, numero);
		
		writer.println(somaTotal);
	}

	private String somarValores(String somar, Integer numero) {
		StringBuilder retorno = new StringBuilder();
		switch (somar) {
		case "impar":
			BigDecimal valor = BigDecimal.ZERO;
			for (int i = 0; i < 10 ; i++) {
				if (i % 2 == 0 || i == 0) {
					valor = valor.add(valores.get(i));
					retorno.append(valores.get(i) + " + " );
				}
			}
			retorno.append(" = " + valor);
			break;
		case "par":
			BigDecimal valorPar = BigDecimal.ZERO;
			for (int i = 0; i < 10 ; i++) {
				if (i % 2 != 0 && i != 0) {
					valorPar = valorPar.add(valores.get(i));
					retorno.append(valores.get(i) + " + " );
				}
			}
			retorno.append(" = " + valorPar);
			break;
		default:
			return "";
		}
		return retorno.toString();
	}


}
