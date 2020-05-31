package br.eti.rbritta.sql2sheets.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AuthenticationProperties {

    @Value("${sql2sheets.authentication.type}")
    private AuthenticationType authType;

    @Value("${spring.ldap.urls}")
    private String ldapUrl;

    @Value("${sql2sheets.authentication.ldap.domain}")
    private String ldapDomain;

    @Value("${sql2sheets.authentication.ldap.user.dn-pattern}")
    private String ldapUserDnPattern;

    @Value("${sql2sheets.authentication.ldap.user.password-attribute}")
    private String ldapUserPasswordAttribute;
 
}