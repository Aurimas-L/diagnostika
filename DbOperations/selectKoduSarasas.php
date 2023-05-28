<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['kodai'])) {
    if ($db->dbConnect()) {
        if (!$db->selectKoduSarasas($_POST['kodai']))  echo "Kodai neregistruoti";
    }
} else echo "klaida duomenyse";
