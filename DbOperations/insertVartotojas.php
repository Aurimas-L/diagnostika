<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['fullname']) && isset($_POST['email']) && isset($_POST['username']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->insertVartotojas($_POST['fullname'], $_POST['email'], $_POST['username'], $_POST['password'])) {
            echo "Registracija pavyko";
        } else echo "Registracija nepavyko";
    } else echo "tinklo klaida";
} else echo "Visi laukai privalomi";
?>
