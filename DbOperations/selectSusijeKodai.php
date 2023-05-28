<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['OBDKodas'])) {
    if ($db->dbConnect()) {
        if (!$db->selectSusijeKodai($_POST['OBDKodas']))  echo "Susijusiu kodu nerasta";
    }
} else echo "klaida duomenyse";
