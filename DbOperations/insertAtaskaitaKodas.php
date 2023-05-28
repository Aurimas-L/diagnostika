<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['ataskaitaId']) && isset($_POST['obdReiksme'])) {
    if ($db->dbConnect()) {
        if($db->insertAtaskaitaKodas($_POST ["ataskaitaId"], $_POST['obdReiksme'])) echo "kodas pridetas";
        else echo "kodas nepridetas";
    } else echo "tinklo klaida";
} else echo "Negauti duomenys";
?>
