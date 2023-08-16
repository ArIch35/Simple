<?php
header("Access-Control-Allow-Origin: *");
header("Content-type: application/json; charset=UTF-8");
header("Cache-Control: no-store, no-cache, must-revalidate");

$host = (gethostbyname("mariadb") != "mariadb") ? "mariadb" : "localhost";
$database = new MySQLi($host, "public", "public", "pizzaservice");

if ($database->connect_errno) echo "Connect failed: ".$database->connect_error;
else if (!$database->set_charset("utf8")) echo "Charset failed: ".$database->error;

$sql_query = "";
if (!isset($_GET['table']) || $_GET['table'] == 'article') $sql_query = "select * from article";
else if ($_GET['table'] == 'ordering') $sql_query = "select * from ordering";
else if ($_GET['table'] == 'ordered_article') $sql_query = "select * from ordered_article";
if (!($recordset = $database->query($sql_query))) echo "Query failed: ".$sql_query;
else {
    $res = array();
    while ($record = $recordset->fetch_assoc()) { $res[] = $record; }
    $recordset->free();
    echo json_encode($res);
}
if (!$database->close()) echo "Close failed: ".$database->error;