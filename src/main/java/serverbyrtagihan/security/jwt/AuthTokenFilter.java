package serverbyrtagihan.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import serverbyrtagihan.impl.CustomerDetailsServiceImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomerDetailsServiceImpl adminDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    private static final String AUTH_HEADER_NAME = "auth-tgh";
    private static final String JWT_PREFIX = "jwt ";

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTH_HEADER_NAME);
        if (headerAuth != null && StringUtils.hasText(headerAuth) && headerAuth.startsWith(JWT_PREFIX)) {
            return headerAuth.substring(JWT_PREFIX.length());
        }
        return null;
    }



}
