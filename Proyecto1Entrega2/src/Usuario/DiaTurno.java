package World;

public class DiaTurno {
    private DiaSemana dia;
    private boolean estaAprobado;

    public DiaTurno(DiaSemana dia, boolean estaAprobado) {
        this.dia = dia;
        this.estaAprobado = estaAprobado;
    }

    public DiaSemana getDia() {
        return dia;
    }

    public boolean estaAsignado() {
        return true; 
    }

    public void asignar() {
    }

    public void liberar() {
    }

    public boolean isAprobado() {
        return estaAprobado;
    }

    public void setAprobado(boolean estado) {
        this.estaAprobado = estado;
    }
}