<?php
$USER = 'epiz_33855079';
$PASS = 'unjcNlf3sug8';
$DBNAME = 'epiz_33855079_projects';
$HOSTNAME = 'sql109.epizy.com';

function get_database(){
    $conn = mysqli_connect($GLOBALS['HOSTNAME'], $GLOBALS['USER'], $GLOBALS['PASS'], $GLOBALS['DBNAME']);

    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
      }
    return $conn;
}
?>