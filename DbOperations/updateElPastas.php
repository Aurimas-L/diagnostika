<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['vartotojasId']) && isset($_POST['slaptazodis']) & isset($_POST['elPastas']) & isset ($_POST['prisijungimas'])) {
    if ($db->dbConnect()) {
        if ($db->logIn($_POST ["prisijungimas"], $_POST ["slaptazodis"])) {
            if (!$db->selectElPastas($_POST ["elPastas"])) {
                if ($db->updateElPastas($_POST ["vartotojasId"], $_POST['elPastas'])) echo "pavyko";
                else echo "klaida";
            } else echo "el pasto adresas uzimtas";
        } else echo "slaptazodis neteisingas";
    } else echo "tinklo klaida";
} else echo "Negauti duomenys";
?>
