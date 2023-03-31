<?php
require("get_database.php");

$output = array();
$cmd = $_GET['cmd'];


function get_user_items(){
    $sql = "SELECT * FROM Item";
    $result = get_database()->query($sql);
    if ($result->num_rows > 0) {
        while($row = $result->fetch_object()) {
          $output[] = $row;
        }
      }
    return $output;
}

if ($cmd == "gI"){
  echo json_encode(get_user_items());
}






?>