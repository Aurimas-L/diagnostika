package Objects;

import java.util.Date;

public class Ataskaita {
    private int id;
    private int automobilisId;
    private Date sukūrimo_data;
    private short gedimuKiekis;

    public Ataskaita(int id, int automobilisId, Date sukūrimo_data, short gedimuKiekis) {
        this.id = id;
        this.automobilisId = automobilisId;
        this.sukūrimo_data = sukūrimo_data;
        this.gedimuKiekis = gedimuKiekis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAutomobilisId() {
        return automobilisId;
    }

    public void setAutomobilisId(int automobilisId) {
        this.automobilisId = automobilisId;
    }

    public Date getSukūrimo_data() {
        return sukūrimo_data;
    }

    public void setSukūrimo_data(Date sukūrimo_data) {
        this.sukūrimo_data = sukūrimo_data;
    }

    public short getGedimuKiekis() {
        return gedimuKiekis;
    }

    public void setGedimuKiekis(short gedimuKiekis) {
        this.gedimuKiekis = gedimuKiekis;
    }
}
