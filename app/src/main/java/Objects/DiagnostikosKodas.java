package Objects;

import java.util.Date;

public class DiagnostikosKodas {

    private String OBDKodas;
    private String kritiskumas;
    private String aprasymas;
    private String priezastis;
    private String tvarkymas;
    private String susijeGedimai;
    private int kaina;
    private String sukurtas;
    private String atnaujintas;
    private String servisoPaskirtis;

    private short paskirtis;

    public DiagnostikosKodas(String OBDKodas, String kritiskumas, String aprasymas, String priezastis, String tvarkymas, String susijeGedimai, int kaina, String sukurtas, String atnaujintas, short servisoPaskirtis) {
        this.OBDKodas = OBDKodas;
        this.kritiskumas = kritiskumas;
        this.aprasymas = aprasymas;
        this.priezastis = priezastis;
        this.tvarkymas = tvarkymas;
        this.susijeGedimai = susijeGedimai;
        this.kaina = kaina;
        this.sukurtas = sukurtas;
        this.atnaujintas = atnaujintas;
        this.paskirtis = servisoPaskirtis;
    }

    public DiagnostikosKodas(String klaida) {
        this.OBDKodas = klaida;
        this.kritiskumas = null;
        this.aprasymas = null;
        this.priezastis = null;
        this.tvarkymas = null;
        this.susijeGedimai = null;
        this.kaina = 0;
        this.sukurtas = null;
        this.atnaujintas = null;
        this.paskirtis = 0;
    }

    public String getOBDKodas() {
        return OBDKodas;
    }

    public void setOBDKodas(String OBDKodas) {
        this.OBDKodas = OBDKodas;
    }

    public String getKritiskumas() {
        return kritiskumas;
    }

    public void setKritiskumas(String kritiskumas) {
        this.kritiskumas = kritiskumas;
    }

    public String getAprasymas() {
        return aprasymas;
    }

    public void setAprasymas(String aprasymas) {
        this.aprasymas = aprasymas;
    }

    public String getPriezastis() {
        return priezastis;
    }

    public void setPriezastis(String priezastis) {
        this.priezastis = priezastis;
    }

    public String getTvarkymas() {
        return tvarkymas;
    }

    public void setTvarkymas(String tvarkymas) {
        this.tvarkymas = tvarkymas;
    }

    public String getSusijeGedimai() {
        return susijeGedimai;
    }

    public void setSusijeGedimai(String susijeGedimai) {
        this.susijeGedimai = susijeGedimai;
    }

    public int getKaina() {
        return kaina;
    }

    public void setKaina(int kaina) {
        this.kaina = kaina;
    }

    public String getSukurtas() {
        return sukurtas;
    }

    public void setSukurtas(String sukurtas) {
        this.sukurtas = sukurtas;
    }

    public String getAtnaujintas() {
        return atnaujintas;
    }

    public void setAtnaujintas(String atnaujintas) {
        this.atnaujintas = atnaujintas;
    }

    public String getServisoPaskirtis() {
        return servisoPaskirtis;
    }

    public void setServisoPaskirtis(String servisoPaskirtis) {
        this.servisoPaskirtis = servisoPaskirtis;
    }
}
