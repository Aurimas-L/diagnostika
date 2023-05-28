package Objects;

public class KodaiAtaskaitoje {
    private String obdKodas;
    private int ataskaitaId;

    public KodaiAtaskaitoje(String obdKodas, int ataskaitaId) {
        this.obdKodas = obdKodas;
        this.ataskaitaId = ataskaitaId;
    }

    public String getObdKodas() {
        return obdKodas;
    }

    public void setObdKodas(String obdKodas) {
        this.obdKodas = obdKodas;
    }

    public int getAtaskaitaId() {
        return ataskaitaId;
    }

    public void setAtaskaitaId(int ataskaitaId) {
        this.ataskaitaId = ataskaitaId;
    }
}
