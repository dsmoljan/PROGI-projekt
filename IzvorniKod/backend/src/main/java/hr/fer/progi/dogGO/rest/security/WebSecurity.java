package hr.fer.progi.dogGO.rest.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.annotation.Resource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter{

        @Resource(name = "associationUserDetailsService")
        private AssociationUserDetailsService associationUserDetailsService;

        @Resource(name = "walkerUserDetailsService")
        private WalkerUserDetailsService walkerUserDetailsService;

        @Resource(name = "adminUserDetailsService")
        private AdminUserDetailsService adminUserDetailsService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
        	http.httpBasic().and().cors();
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http.authorizeRequests()
                    .antMatchers(  "/registration/**","/walker/ranking", "/walker/ranking*", "/association*",
                            "/login/**", "/association/all", "/walker/all", "/dog/**").permitAll();
            http.authorizeRequests()
                    .anyRequest()
                    .authenticated();
            http.csrf().disable();
            http.headers().frameOptions().sameOrigin();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(associationUserDetailsService);
            auth.userDetailsService(walkerUserDetailsService);
            auth.userDetailsService(adminUserDetailsService);
        }
}
