package br.eti.rbritta.sql2sheets.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationProperties authProperties;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors()
                .and().csrf().disable()
                .headers().frameOptions().disable()
                .and();

        if (authProperties.getAuthType().none()) {
            http.authorizeRequests().anyRequest().permitAll();
            return;
        }

        http.authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().permitAll();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        switch (authProperties.getAuthType()) {
            case NONE -> {}
            case LDAP -> configureLdapAuthentication(auth);
            case IN_MEMORY -> configureInMemoryAuthentication(auth);
            default -> throw new Exception("Undefined Authentication Type");
        }
    }

    private void configureLdapAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .userDnPatterns(authProperties.getLdapUserDnPattern())
                .contextSource().url(authProperties.getLdapUrl() + "/" + authProperties.getLdapDomain())
                .managerPassword(authProperties.getLdapUserPasswordAttribute());
    }

    private void configureInMemoryAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .roles("ADMIN")
                .password("{noop}admin");
    }

}