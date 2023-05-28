<?php
require "DataBaseConfig.php";

class DataBase
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    function logIn($username, $password)
    {
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        $this->sql = "select * from vartotojas where PRISIJUNGIMAS = '" . $username . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        $login = false;
        if (mysqli_num_rows($result) != 0) {
            $dbusername = $row['PRISIJUNGIMAS'];
            $dbpassword = $row['SLAPTAZODIS'];
            $dbvartotojasId = $row['VARTOTOJAS_ID'];

            if ($dbusername == $username && password_verify($password, $dbpassword)) {
                $login = true;
                echo $dbvartotojasId;
            }
        }
        return $login;
    }

    function insertVartotojas($fullname, $email, $username, $password)
    {
        $fullname = $this->prepareData($fullname);
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        $email = $this->prepareData($email);
        $password = password_hash($password, PASSWORD_DEFAULT);
        $this->sql =
            "INSERT INTO  vartotojas (VARDAS, PRISIJUNGIMAS, ELEKTRONINIO_PASTO_ADRESAS, SLAPTAZODIS) VALUES ('" . $fullname . "','" . $username . "','" . $email . "','" . $password . "')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }

    function selectVartotojas($vartotojasId)
    {
        $query = "Select * from vartotojas where VARTOTOJAS_ID = " . $vartotojasId . "";
        $res = mysqli_query($this->connect, $query);

        $json_data[] = array();
        while ($row = mysqli_fetch_assoc($res)) {
            $json_data[] = $row;
        }
        echo json_encode($json_data);


    }

    function selectKodas($OBDKODAS)
    {
        $query = "Select * from diagnostikos_kodas where OBD_REIKSME = '" . $OBDKODAS . "'";
        $result = mysqli_query($this->connect, $query);
        $yra = false;

        if (mysqli_num_rows($result) != 0) {
            $json_data[] = array();
            while ($row = mysqli_fetch_assoc($result)) {
                $json_data[] = $row;
            }

            echo json_encode($json_data);

            $yra = true;
        }
        return $yra;
    }

    function selectKoduSarasas($KODAI)
    {
        $query = "Select * from diagnostikos_kodas where OBD_REIKSME in (" . $KODAI . ")";
        $result = mysqli_query($this->connect, $query);
        $yra = false;

        if (mysqli_num_rows($result) != 0) {
            $json_data[] = array();
            while ($row = mysqli_fetch_assoc($result)) {
                $json_data[] = $row;
            }

            echo json_encode($json_data);

            $yra = true;
        }
        return $yra;
    }

    function selectAutomobiliuPav($vartotojasId)
    {
        $query = "Select PAVADINIMAS from automobilis where VARTOTOJAS_ID = '" . $vartotojasId . "'";
        $result = mysqli_query($this->connect, $query);

        if (mysqli_num_rows($result) != 0) {
            $json_data[] = array();
            while ($row = mysqli_fetch_assoc($result)) {
                $json_data[] = $row;
            }

            echo json_encode($json_data);
            $yra = true;

        } else $yra = false;
        return $yra;
    }

    function insertAutomobilis($vartotojasId, $galia, $gamintojas, $modelis, $pavadinimas, $variklis, $vinNumeris)
    {

        $this->query =
            "INSERT INTO automobilis (VARTOTOJAS_ID, PAVADINIMAS, GAMINTOJAS, MODELIS, VARIKLIS, GALIA, VIN_NUMERIS) VALUES ( '" . $vartotojasId . "','" . $pavadinimas . "','" . $gamintojas . "','" . $modelis . "','" . $variklis . "', '" . $galia . "','" . $vinNumeris . "')";
        if (mysqli_query($this->connect, $this->query)) {
            return true;
        } else return false;

    }

    function selectAutoInformacijaByVIdPav($vartotojasId, $pavadinimas)
    {
        $query = "Select * from automobilis where VARTOTOJAS_ID = '" . $vartotojasId . "' and PAVADINIMAS = '" . $pavadinimas . "'";
        $result = mysqli_query($this->connect, $query);

        if (mysqli_num_rows($result) != 0) {
            $json_data[] = array();
            while ($row = mysqli_fetch_assoc($result)) {
                $json_data[] = $row;
            }

            echo json_encode($json_data);
            $yra = true;

        } else $yra = false;
        return $yra;

    }

    function updateAuto($vartotojasId, $galia, $gamintojas, $modelis, $pavadinimas, $variklis, $vinNumeris, $pavadinimasSenas)
    {

        $this->query =
            "UPDATE automobilis SET PAVADINIMAS = '" . $pavadinimas . "', GAMINTOJAS = '" . $gamintojas . "', MODELIS = '" . $modelis . "', VARIKLIS = '" . $variklis . "', GALIA = '" . $galia . "', VIN_NUMERIS = '" . $vinNumeris . "' WHERE VARTOTOJAS_ID = '" . $vartotojasId . "' AND PAVADINIMAS = '" . $pavadinimasSenas . "'";
        if (mysqli_query($this->connect, $this->query)) {
            return true;
        } else return false;

    }

    function deleteAuto($vartotojasId, $pavadinimas)
    {
        $this->query = "DELETE FROM automobilis WHERE PAVADINIMAS = '" . $pavadinimas . "' AND VARTOTOJAS_ID = '" . $vartotojasId . "'";
        if (mysqli_query($this->connect, $this->query)) {
            return true;
        } else return false;
    }

    public function selectSusijeKodai($OBDKodas)
    {
        $query = "Select * from susije_kodai where OBD_REIKSME = '" . $OBDKodas . "'";
        $result = mysqli_query($this->connect, $query);
        $yra = false;

        if (mysqli_num_rows($result) != 0) {
            $json_data[] = array();
            while ($row = mysqli_fetch_assoc($result)) {
                $json_data[] = $row;
            }

            echo json_encode($json_data);
            $yra = true;
        }
        return $yra;
    }

    public function insertIstorija($vartotojasId, $pasirinktasAuto, $OBDKodas)
    {
        $this->query =
            "INSERT INTO istorija (VARTOTOJAS_ID, OBD_REIKSME, AUTOMOBILIS_ID, DATA) VALUES ( '" . $vartotojasId . "','" . $OBDKodas . "','" . $pasirinktasAuto . "', CURDATE() )";
        if (mysqli_query($this->connect, $this->query)) {
            return true;
        } else return false;
    }

    public function selectAutomobilioIstorija($automobilisId)
    {
        $query = "Select * from istorija WHERE AUTOMOBILIS_ID = '" . $automobilisId . "'";
        $result = mysqli_query($this->connect, $query);
        $yra = false;

        if (mysqli_num_rows($result) != 0) {
            $json_data[] = array();
            while ($row = mysqli_fetch_assoc($result)) {
                $json_data[] = $row;
            }
            echo json_encode($json_data);
            $yra = true;
        }
        return $yra;

    }

    public function selectIstorija($vartotojasId, $pasirinktasAuto, $OBDKodas)
    {
        $query = "Select istorija.* from istorija inner join automobilis on istorija.AUTOMOBILIS_ID = automobilis.AUTOMOBILIO_ID  WHERE automobilis.PAVADINIMAS = '" . $pasirinktasAuto . "' AND istorija.VARTOTOJAS_ID = '" . $vartotojasId . "' AND istorija.OBD_REIKSME = '" . $OBDKodas . "'";
        $result = mysqli_query($this->connect, $query);
        $yra = false;

        if (mysqli_num_rows($result) != 0) {
            $json_data[] = array();
            while ($row = mysqli_fetch_assoc($result)) {
                $json_data[] = $row;
            }
            echo json_encode($json_data);
            $yra = true;
        }
        return $yra;
    }

    public function updateSlaptazodis($vartotojasId, $slaptazodis)
    {
        $slaptazodis = password_hash($slaptazodis, PASSWORD_DEFAULT);
        $this->query = "UPDATE vartotojas SET SLAPTAZODIS = '" . $slaptazodis . "' WHERE VARTOTOJAS_ID = '" . $vartotojasId . "'";
        if (mysqli_query($this->connect, $this->query)) {
            return true;
        } else return false;
    }

    public function selectElPastas($elPastas)
    {
        $query = "Select * from vartotojas where ELEKTRONINIO_PASTO_ADRESAS = '" . $elPastas . "'";
        $res = mysqli_query($this->connect, $query);
        $yra = false;

        while ($row = mysqli_fetch_assoc($res)) {
            $yra = true;
        }

        return $yra;
    }

    public function updateElPastas($vartotojasId, $elPastas)
    {
        $this->query = "UPDATE vartotojas SET ELEKTRONINIO_PASTO_ADRESAS = '" . $elPastas . "' WHERE VARTOTOJAS_ID = '" . $vartotojasId . "'";
        if (mysqli_query($this->connect, $this->query)) {
            return true;
        } else return false;
    }

    public function selectAutomobilisId($vartotojasId, $pasirinktasAuto)
    {
        $query = "Select AUTOMOBILIO_ID from automobilis where VARTOTOJAS_ID = '" . $vartotojasId . "' and PAVADINIMAS = '" . $pasirinktasAuto . "'";
        $result = mysqli_query($this->connect, $query);
        $row = mysqli_fetch_assoc($result);
        $automobilioId = $row ['AUTOMOBILIO_ID'];

        return $automobilioId;
    }

    public function insertAtaskaita($automobilisID, $vartotojasId, $gedimuKiekis)
    {
        $result = 0;
        $query =
            "INSERT INTO ataskaita (AUTOMOBILIO_ID , VARTOTOJAS_ID , SUKURIMO_LAIKAS, GEDIMU_KIEKIS) VALUES ( '" . $automobilisID . "','" . $vartotojasId . "', NOW() ,'" . $gedimuKiekis . "')";
        if (mysqli_query($this->connect, $query))
            $result = mysqli_insert_id($this->connect);
        return $result;
    }

    public function insertAtaskaitaKodas($ataskaitaId, $obdReiksme)
    {
        $query = "INSERT INTO kodai_ataskaitoje (ATASKAITA_ID, OBD_REIKSME) VALUES ( '" . $ataskaitaId . "'," . $obdReiksme . ")";
        if (mysqli_query($this->connect, $query)) {
            return true;
        } else return false;
    }

    public function selectAtaskaitos($automobilioId, $vartotojasId)
    {
        $query = "Select * from ataskaita where VARTOTOJAS_ID = '" . $vartotojasId . "' and AUTOMOBILIO_ID = '" . $automobilioId . "'";
        $result = mysqli_query($this->connect, $query);
        $yra = false;
        if (mysqli_num_rows($result) != 0) {
            $json_data[] = array();
            while ($row = mysqli_fetch_assoc($result)) {
                $json_data[] = $row;
            }
            echo json_encode($json_data);
            $yra = true;
        }
        return $yra;
    }

    public function selectAtaskaitaId($dataIrLaikas, $vartotojasId)
    {

        $query = "Select ATASKAITA_ID from ataskaita where VARTOTOJAS_ID = '" . $vartotojasId . "' and SUKURIMO_LAIKAS = '" . $dataIrLaikas . "'";
        $result = mysqli_query($this->connect, $query);
        $row = mysqli_fetch_assoc($result);
        $ataskaitaId = $row ['ATASKAITA_ID'];

        return $ataskaitaId;

    }

    public function selectAtaskaitosKodai($ataskaitaId)
    {

        $query = "Select * from kodai_ataskaitoje WHERE ATASKAITA_ID = '" . $ataskaitaId . "'";
        $result = mysqli_query($this->connect, $query);
        $yra = false;

        if (mysqli_num_rows($result) != 0) {
            $json_data[] = array();
            while ($row = mysqli_fetch_assoc($result)) {
                $json_data[] = $row;
            }
            echo json_encode($json_data);
            $yra = true;
        }
        return $yra;
    }

    public function deleteIstorija($vartotojasId)
    {
        $this->query = "DELETE FROM istorija WHERE VARTOTOJAS_ID = '" . $vartotojasId . "'";
        if (mysqli_query($this->connect, $this->query)) {
            return true;
        } else return false;
    }

    public function deleteAtaskaitos($vartotojasId)
    {
        $query = "Select ATASKAITA_ID from ataskaita where VARTOTOJAS_ID = '" . $vartotojasId . "'";
        $result = mysqli_query($this->connect, $query);
        while ($row = mysqli_fetch_assoc($result)) {
            $ataskaitaId = $row ['ATASKAITA_ID'];
            $query2 = "DELETE FROM kodai_Ataskaitoje WHERE ATASKAITA_ID = '" . $ataskaitaId . "'";
            mysqli_query($this->connect, $query2);
        }
        $query3 = "DELETE FROM ataskaita WHERE VARTOTOJAS_ID = '" . $vartotojasId . "'";
        mysqli_query($this->connect, $query3);
    }

    public function deleteAutomobiliai($vartotojasId)
    {
        $this->query = "DELETE FROM automobilis WHERE VARTOTOJAS_ID = '" . $vartotojasId . "'";
        if (mysqli_query($this->connect, $this->query)) {
            return true;
        } else return false;
    }

    public function deleteVartotojas($vartotojasId)
    {
        $this->query = "DELETE FROM vartotojas WHERE VARTOTOJAS_ID = '" . $vartotojasId . "'";
        if (mysqli_query($this->connect, $this->query)) {
            return true;
        } else return false;
    }
}


?>
