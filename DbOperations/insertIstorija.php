<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['vartotojasId']) && isset($_POST['pasirinktasAuto']) && isset($_POST['OBDKodas'])) {
    if ($db->dbConnect()) {
        if (!$db->insertIstorija($_POST ["vartotojasId"], $_POST['pasirinktasAuto'], $_POST['OBDKodas'])) echo "nepavyko";
    } else echo "tinklo klaida";
} else echo "Negauti duomenys";
?>
