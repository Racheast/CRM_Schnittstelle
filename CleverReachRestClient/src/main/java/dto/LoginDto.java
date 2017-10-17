package dto;

public class LoginDto {
	private int client_id;
	private String login;
	private String password;
	public int getClient_id() {
		return client_id;
	}
	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public LoginDto(int client_id, String login, String password) {
		super();
		this.client_id = client_id;
		this.login = login;
		this.password = password;
	}
	@Override
	public String toString() {
		return "LoginDto [client_id=" + client_id + ", login=" + login + ", password=" + password + "]";
	}
	
	
	
}
