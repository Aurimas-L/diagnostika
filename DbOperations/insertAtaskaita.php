<?php
require "DataBase.php";
$db = new DataBase();
$automobilisID = 0;
$ataskaitaId = 0;
if (isset($_POST['pasirinktasAuto']) && isset($_POST['vartotojasId']) && isset($_POST['gedimuKiekis'])) {
    if ($db->dbConnect()) {
        $automobilisID = $db->selectAutomobilisId($_POST ["vartotojasId"], $_POST['pasirinktasAuto']);
        if (!$automobilisID == 0) {
            $ataskaitaId = $db->insertAtaskaita($automobilisID, $_POST['vartotojasId'], $_POST['gedimuKiekis']);
            if (!$ataskaitaId == 0) echo $ataskaitaId;
            else echo "nepavyko";
        }else echo "klaida";
    } else echo "tinklo klaida";
} else echo "Negauti duomenys";
?>
