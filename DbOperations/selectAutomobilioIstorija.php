<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['automobilisId']) ) {
    if ($db->dbConnect()) {
        if(!$db->selectAutomobilioIstorija( $_POST['automobilisId'])) echo "nera";
    } else echo "tinklo klaida";
} else echo "klaida duomenyse";

