<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['vartotojasId']) && isset($_POST ['pavadinimas'])) {
    if ($db->dbConnect()) {
        $db->selectAutoInformacijaByVIdPav($_POST['vartotojasId'],$_POST['pavadinimas'] );
    } else echo "tinklo klaida";
} else echo "klaida duomenyse";

