package pe.creativity.Restfull.dto;

import javax.validation.constraints.NotBlank;

public class LoginDto {
    /*clase para trasmitir datos de solo lectura y serializable
    que trabajara con la capa dominio o entidad
     */

    @NotBlank(message = "El username no puede estar vacio")
    private String username;

    @NotBlank(message = "El password no puede estar vacio")
    private String password;


    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
