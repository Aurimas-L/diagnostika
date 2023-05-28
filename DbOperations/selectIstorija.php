<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['vartotojasId']) && isset($_POST['pasirinktasAuto']) && isset($_POST['OBDKodas'])) {
    if ($db->dbConnect()) {
        if (!$db->selectIstorija($_POST ["vartotojasId"], $_POST['pasirinktasAuto'], $_POST['OBDKodas'])) echo "nera";
    } else echo "tinklo klaida";
} else echo "Negauti duomenys";
?>
