package br.leg.alrr.common.filter;

import br.leg.alrr.common.business.TipoUsuario;
import br.leg.alrr.common.model.acesso.Usuario;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/pages/*")
public class NavegacaoFilter implements Filter {

    public NavegacaoFilter() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession httpSession = ((HttpServletRequest) request).getSession();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (httpServletRequest.getRequestURI().indexOf("login.xhtml") <= -1) {

            Usuario u = (Usuario) httpSession.getAttribute("usuario");
            if (u == null) {
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login.xhtml");
            } else {
                
                Usuario usuario = (Usuario) httpSession.getAttribute("usuario");
                
                if (usuario.getTipo().equals(TipoUsuario.RELATORIO) && (httpServletRequest.getRequestURI().contains("superadmin") || httpServletRequest.getRequestURI().contains("admin") || httpServletRequest.getRequestURI().contains("user"))) {
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/user/home.xhtml");
                } else if (usuario.getTipo().equals(TipoUsuario.OPERADOR) && (httpServletRequest.getRequestURI().contains("admin") || httpServletRequest.getRequestURI().contains("superadmin"))) {
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/user/home.xhtml");
                } else if (usuario.getTipo().equals(TipoUsuario.ADMIN) && httpServletRequest.getRequestURI().contains("superadmin")) {
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/user/home.xhtml");
                } else {
                    chain.doFilter(request, response);
                }

            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

}
