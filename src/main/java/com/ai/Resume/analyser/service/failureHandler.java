@Override
public void onAuthenticationFailure(HttpServletRequest request,
                                    HttpServletResponse response,
                                    AuthenticationException exception) throws IOException {

    boolean isApi = request.getHeader("Accept") != null && request.getHeader("Accept").contains("application/json");

    if (isApi) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\":\"Invalid credentials\"}");
        response.getWriter().flush();
    } else {
        response.sendRedirect("/login"); // Thymeleaf
    }
}
