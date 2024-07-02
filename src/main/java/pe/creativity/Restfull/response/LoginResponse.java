package pe.creativity.Restfull.response;

public class LoginResponse {
    private String token;
    private long expires_in;
    private String token_type;

    public LoginResponse() {
    }

    public LoginResponse(String token, long expiresIn, String token_type) {
        this.token = token;
        this.expires_in = expiresIn;
        this.token_type = token_type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", expires_in=" + expires_in +
                ", token_type='" + token_type + '\'' +
                '}';
    }
}
