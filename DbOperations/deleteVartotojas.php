<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['vartotojasId']) && isset($_POST ['prisijungimas']) && isset($_POST ['slaptazodis'])) {
    if ($db->dbConnect()) {
        if ($db->logIn($_POST['prisijungimas'], $_POST['slaptazodis'])) {
            if (
                $db->deleteIstorija($_POST['vartotojasId']) &&
                $db->deleteAtaskaitos($_POST['vartotojasId']) &&
                $db->deleteAutomobiliai($_POST['vartotojasId']) &&
                $db->deleteVartotojas($_POST['vartotojasId'])
            ) echo "vartotojas ištrintas";
            else echo "ištrinti nepavyko";
        } else echo "slaptazodis neteisings";
    } else echo "tinklo klaida";
} else echo "Negauti duomenys";

