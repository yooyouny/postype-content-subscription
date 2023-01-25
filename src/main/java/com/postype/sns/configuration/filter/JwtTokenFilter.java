package com.postype.sns.configuration.filter;

import com.postype.sns.application.contoller.dto.MemberDto;
import com.postype.sns.domain.member.service.MemberService;
import com.postype.sns.utill.JwtTokenUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter{

	private final String key;
	private final MemberService memberService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		//get header
		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if(header == null || !header.startsWith("Bearer ")) {
			log.error("Authorization Header does not start with Bearer {}", request.getRequestURI());
			filterChain.doFilter(request, response);
			return;
		}

		try {
			final String token = header.split(" ")[1].trim();

			if(JwtTokenUtils.isExpired(token, key)) {
				log.error("key is expired");
				filterChain.doFilter(request, response);
				return;
			}

			String memberId = JwtTokenUtils.getMemberId(token, key);
			MemberDto member = memberService.loadMemberByMemberId(memberId);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				member, null, member.getAuthorities()
			);

			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		}catch(RuntimeException exception){
			log.error("Error occurs while validating, {}", exception.toString());
			filterChain.doFilter(request, response);
			return;
		}

		filterChain.doFilter(request, response); //뒤의 작업으로 넘기는
	}
}
