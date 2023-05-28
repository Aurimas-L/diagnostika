<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['vartotojoId'])) {
    if ($db->dbConnect()) {
        $db->selectVartotojas($_POST['vartotojoId']);
    } else echo "tinklo klaida";
} else echo "klaida duomenyse";
