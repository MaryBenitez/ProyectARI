package Cliente;

public class Cliente {

    //Atributos
    private int idCliente;
    private String documento;
    private String nombre;
    private String apellido;
    private String nTarjeta;
    private String tipoTarjeta;
    private int telefono;

    //JSON


    //Constructor
    public Cliente(int idCliente, String documento, String nombre, String apellido, String ntarjeta, String tipoTarjeta, int telefono) {
        this.idCliente = idCliente;
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nTarjeta = ntarjeta;
        this.tipoTarjeta = tipoTarjeta;
        this.telefono = telefono;
    }

    public Cliente(){}
    //Setters and Getters

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNtarjeta() {
        return nTarjeta ;
    }

    public void setNtarjeta(String ntarjeta) {
        nTarjeta = ntarjeta;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public String getTelefono() {
        return String.valueOf(telefono);
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
}
