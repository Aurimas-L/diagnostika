<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['username']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if (!$db->logIn($_POST['username'], $_POST['password']))  echo "Neteisingi duomenys";
    } else echo "tinklo klaida";
} else echo "Visi laukai privalomi";
?>
