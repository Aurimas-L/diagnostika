<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['automobilioId']) && isset($_POST['vartotojasId'])) {
    if ($db->dbConnect()) {
        if (!$db->selectAtaskaitos($_POST ["automobilioId"], $_POST['vartotojasId'])) echo "nera";
    } else echo "tinklo klaida";
} else echo "Negauti duomenys";
?>
