<?php
require "DataBase.php";
$db = new DataBase();
$ataskaitaId=0;
if (isset($_POST['dataIrLaikas']) && isset($_POST['vartotojasId'])) {
    if ($db->dbConnect()) {
        $ataskaitaId = $db->selectAtaskaitaId($_POST ["dataIrLaikas"], $_POST ["vartotojasId"]);
        if (!$ataskaitaId ==0){
           if($db->selectAtaskaitosKodai($ataskaitaId));
           else echo "kodu nera";
        }else "ataskaita nerasta";
    } else echo "tinklo klaida";
} else echo "Negauti duomenys";
?>
