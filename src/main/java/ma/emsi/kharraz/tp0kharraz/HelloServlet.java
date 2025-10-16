package ma.emsi.kharraz.tp0kharraz;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy 'à' HH:mm:ss");
    private String baseMessage;

    public void init() {
        baseMessage = "Bienvenue sur mon serveur d'expérimentation !";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        LocalDateTime now = LocalDateTime.now();
        String greeting = buildGreeting(now);
        String signature = computeNavigatorSignature(request.getHeader("User-Agent"));
        String originalText = extractOriginalText(request);
        String swappedCaseText = swapAlphabetCase(originalText);

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + greeting + "</h1>");
        out.println("<p>" + baseMessage + "</p>");
        out.println("<p>Nous sommes le <strong>" + DATE_TIME_FORMATTER.format(now) + "</strong>.</p>");
        out.println("<p>Votre empreinte navigateur est : <strong>" + signature + "</strong>." +
                " Elle est calculée à partir d'une somme de caractères très personnelle.</p>");
        out.println("<p>Texte reçu : <strong>" + originalText + "</strong></p>");
        out.println("<p>Après traitement (majuscule ↔ minuscule) : <strong>" + swappedCaseText + "</strong></p>");
        out.println("</body></html>");
    }

    private String extractOriginalText(HttpServletRequest request) {
        String parameter = request.getParameter("text");
        if (parameter == null || parameter.isBlank()) {
            return "BONUS TP Casablanca";
        }
        return parameter;
    }

    private String swapAlphabetCase(String value) {
        StringBuilder builder = new StringBuilder(value.length());
        for (char currentChar : value.toCharArray()) {
            if (Character.isUpperCase(currentChar)) {
                builder.append(Character.toLowerCase(currentChar));
            } else if (Character.isLowerCase(currentChar)) {
                builder.append(Character.toUpperCase(currentChar));
            } else {
                builder.append(currentChar);
            }
        }
        return builder.toString();
    }

    private String buildGreeting(LocalDateTime dateTime) {
        int hour = dateTime.getHour();
        if (hour < 12) {
            return "Bonjour, explorateur matinal !";
        }
        if (hour < 18) {
            return "Bon après-midi, explorateur curieux !";
        }
        return "Bonsoir, explorateur nocturne !";
    }

    private String computeNavigatorSignature(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return "000";
        }
        int sum = userAgent.chars().reduce(0, (accumulator, currentChar) -> (accumulator + currentChar) % 1000);
        return String.format("%03d", sum);
    }

    public void destroy() {
    }
}