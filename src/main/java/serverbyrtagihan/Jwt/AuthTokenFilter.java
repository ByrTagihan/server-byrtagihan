package serverbyrtagihan.Jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import serverbyrtagihan.Impl.MemberDetailsImpl;
import serverbyrtagihan.Impl.UserDetailsImpl;
import serverbyrtagihan.Modal.TemporaryToken;
import serverbyrtagihan.Repository.ByrTagihanRepository;
import serverbyrtagihan.Repository.MemberLoginRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtProvider jwtUtils;

    @Autowired
    MemberDetailsImpl memberDetails;

    @Autowired
    UserDetailsImpl userDetails;

    @Autowired
    MemberLoginRepository memberLoginRepository;

    @Autowired
    ByrTagihanRepository byrTagihanRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            try {
                String jwt = parseJwt(request);
                System.out.println(jwt);
                if (jwt != null && jwtUtils.checkingTokenJwt(jwt)) {
                    TemporaryToken username = jwtUtils.getSubject(jwt);
                    TemporaryToken token = jwtUtils.getSubject(jwt);
                    UserDetails userDetails1 = userDetails.loadUserByUsername(byrTagihanRepository.findById(token.getRegisterId()).get().getEmail());
                    UserDetails userDetails = memberDetails.loadUserByUsername(memberLoginRepository.findById(username.getMemberId()).get().getUnique_id());
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());
                    UsernamePasswordAuthenticationToken authentication1 =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails1,
                                    null,
                                    userDetails1.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    authentication1.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication1);
                }
            } catch (Exception e) {
                logger.error("Cannot set user authentication: {}", e);
            }

            filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
