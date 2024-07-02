package pe.creativity.Restfull.dto;

import javax.validation.constraints.NotBlank;

public class RegisterDto {
     /*clase para trasmitir datos de solo lectura y serializable
    que trabajara con la capa dominio o entidad
     */

    @NotBlank(message = "El username no puede estar vacio")
    private String username;

    @NotBlank(message = "El password no puede estar vacio")
    private String password;

    @NotBlank(message = "El firstname no puede estar vacio")
    private String firstname;

    @NotBlank(message = "El lastname no puede estar vacio")
    private String lastname;


    public RegisterDto(String username, String password, String firsname, String lastname) {
        this.username = username;
        this.password = password;
        this.firstname = firsname;
        this.lastname = lastname;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
