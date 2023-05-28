<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['vartotojasId']) && isset($_POST['slaptazodis']) & isset($_POST['naujasSlaptazodis']) & isset ($_POST['prisijungimas'])) {
    if ($db->dbConnect()) {
        if ($db->logIn($_POST ["prisijungimas"], $_POST ["slaptazodis"]))
            if ($db->updateSlaptazodis($_POST ["vartotojasId"], $_POST['naujasSlaptazodis'])) echo "pavyko";
            else echo "klaida";
        else echo "slaptazodis neteisingas";
    } else echo "tinklo klaida";
} else echo "Negauti duomenys";
?>
