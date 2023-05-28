package Objects;

public class Vartotojas {
    private long vartotojoId;
    private String vardas;
    private String pavarde;
    private String elektroninioPastoAdresas;
    private String slaptazodis;

    public Vartotojas(int vartotojoId, String vardas, String pavarde, String elPastas, String slaptazodis) {
        this.vartotojoId = vartotojoId;
        this.vardas = vardas;
        this.pavarde = pavarde;
        this.elektroninioPastoAdresas = elPastas;
        this.slaptazodis = slaptazodis;
    }

    public long getVartotojoId() {
        return vartotojoId;
    }

    public void setVartotojoId(long vartotojoId) {
        this.vartotojoId = vartotojoId;
    }

    public String getVardas() {
        return vardas;
    }

    public void setVardas(String vardas) {
        this.vardas = vardas;
    }

    public String getPavarde() {
        return pavarde;
    }

    public void setPavarde(String pavarde) {
        this.pavarde = pavarde;
    }

    public String getElektroninioPastoAdresas() {
        return elektroninioPastoAdresas;
    }

    public void setElektroninioPastoAdresas(String elektroninioPastoAdresas) {
        this.elektroninioPastoAdresas = elektroninioPastoAdresas;
    }

    public String getSlaptazodis() {
        return slaptazodis;
    }

    public void setSlaptazodis(String slaptazodis) {
        this.slaptazodis = slaptazodis;
    }
}
