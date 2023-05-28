<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['vartotojasId']) && isset($_POST['galia']) && isset($_POST['gamintojas']) && isset($_POST['modelis']) && isset($_POST['pavadinimas']) && isset($_POST['variklis']) && isset($_POST['vinNumeris'])) {
    if ($db->dbConnect()) {
        if ($db->insertAutomobilis($_POST ["vartotojasId"], $_POST['galia'], $_POST['gamintojas'], $_POST['modelis'], $_POST['pavadinimas'], $_POST['variklis'], $_POST['vinNumeris'])) {
            echo "Automobilis pridetas";
        } else echo "prideti nepavyko";
    } else echo "tinklo klaida";
} else echo "Negauti duomenys";
?>
