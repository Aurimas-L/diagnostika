<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['vartotojasId'])) {
    if ($db->dbConnect()) {
        $db->selectAutomobiliuPav($_POST['vartotojasId']);
    } else echo "tinklo klaida";
} else echo "klaida duomenyse";

