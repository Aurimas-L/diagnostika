<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['OBDkodas'])) {
    if ($db->dbConnect()) {
        if (!$db->selectKodas($_POST['OBDkodas']))  echo "Kodas " . $_POST['OBDkodas'] . " neregistruotas";
    }
} else echo "klaida duomenyse";
