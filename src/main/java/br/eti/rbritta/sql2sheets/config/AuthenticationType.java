package br.eti.rbritta.sql2sheets.config;

public enum AuthenticationType {

	NONE, IN_MEMORY, LDAP;

	public boolean none() {
		return this.equals(NONE);
	}
}