package co.kr.ppingpong.config.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(getClass()); //
    private final JwtProvider jwtProvider;

//    @Value("${jwt.access_header:null}")
//    private String ACCESS_HEADER;
//
//    @Value("${jwt.token_prefix:null}")
//    private String TOKEN_PREFIX;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isHeaderVerify(request, response)) {
            log.debug("디버그 : 토큰이 존재함");
            String accessToken = request.getHeader("ACCESS_TOKEN").replace("Bearer ", "");
            log.debug("accessToken " + accessToken);
            LoginUser loginUser = jwtProvider.accessTokenVerify(accessToken);
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }

    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response) {
        String accessHeader = request.getHeader("ACCESS_TOKEN");

        if (accessHeader == null || !accessHeader.startsWith("Bearer ")) {
            return false;
        } else {
            return true;
        }
    }
}
