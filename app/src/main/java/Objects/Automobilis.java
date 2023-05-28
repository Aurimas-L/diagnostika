package Objects;

public class Automobilis {
    private int id;
    private int vartotojasId;
    private String pavadinimas;
    private String gamintojas;
    private String modelis;
    private String variklis;
    private String galia;
    private String vinNumeris;

    public Automobilis(int id, int vartotojasId, String pavadinimas, String gamintojas, String modelis, String variklis, String galia, String vin_numeris) {
        this.id = id;
        this.vartotojasId = vartotojasId;
        this.pavadinimas = pavadinimas;
        this.gamintojas = gamintojas;
        this.modelis = modelis;
        this.variklis = variklis;
        this.galia = galia;
        this.vinNumeris = vin_numeris;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVartotojasId() {
        return vartotojasId;
    }

    public void setVartotojasId(int vartotojasId) {
        this.vartotojasId = vartotojasId;
    }

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

    public String getGamintojas() {
        return gamintojas;
    }

    public void setGamintojas(String gamintojas) {
        this.gamintojas = gamintojas;
    }

    public String getModelis() {
        return modelis;
    }

    public void setModelis(String modelis) {
        this.modelis = modelis;
    }

    public String getVariklis() {
        return variklis;
    }

    public void setVariklis(String variklis) {
        this.variklis = variklis;
    }

    public String getGalia() {
        return galia;
    }

    public void setGalia(String galia) {
        this.galia = galia;
    }

    public String getVinNumeris() {
        return vinNumeris;
    }

    public void setVinNumeris(String vinNumeris) {
        this.vinNumeris = vinNumeris;
    }
}
