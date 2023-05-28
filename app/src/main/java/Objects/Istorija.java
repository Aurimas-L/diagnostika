package Objects;

import java.util.Date;

public class Istorija {
    private int vartotojasId;
    private String obdKodas;
    private int automobilisId;
    private Date date;

    public Istorija(int vartotojasId, String obdKodas, int automobilisId, Date date) {
        this.vartotojasId = vartotojasId;
        this.obdKodas = obdKodas;
        this.automobilisId = automobilisId;
        this.date = date;
    }

    public int getVartotojasId() {
        return vartotojasId;
    }

    public void setVartotojasId(int vartotojasId) {
        this.vartotojasId = vartotojasId;
    }

    public String getObdKodas() {
        return obdKodas;
    }

    public void setObdKodas(String obdKodas) {
        this.obdKodas = obdKodas;
    }

    public int getAutomobilisId() {
        return automobilisId;
    }

    public void setAutomobilisId(int automobilisId) {
        this.automobilisId = automobilisId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
