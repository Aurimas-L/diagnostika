<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['vartotojasId']) && isset($_POST ['pavadinimas'])) {
    if ($db->dbConnect()) {
        if($db->deleteAuto($_POST['vartotojasId'],$_POST['pavadinimas'] ))
            echo "automobilis ištrintas";
        else echo "ištrinti nepavyko";
    } else echo "tinklo klaida";
} else echo "klaida duomenyse";

