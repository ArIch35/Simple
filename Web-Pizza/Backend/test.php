<?php
$host = (gethostbyname("mariadb") != "mariadb") ? "mariadb" : "localhost";
$database = new MySQLi($host, "public", "public", "pizzaservice");
function getIDAmountPair($obj){
    $arr = explode(":",$obj);
    $output = [];
    for ($x=0;$x<$arr[1];$x++){
        $output[] = $arr[0];
    }
    return $output;
}
function getOrderedPizza(){
    $output = [];
    $article = $_POST['pizza-list'];
    foreach ($article as $art){
        $output = array_merge($output,getIDAmountPair($art));
    }
    return $output;
}
function getCustomerInfo(){
    $name = $_POST['name'];
    $email = $_POST['email'];
    $address = $_POST['addresse'];
    return $address.", ".$email.", ".$name;
}

function insertDataToOrdering(){
    global $database;
    if (!isset($_POST["name"]) || !isset($_POST["email"]) || !isset($_POST["addresse"])){
        return -1;
    }

    $customerInfo = getCustomerInfo();
    $customerInfo = $database->real_escape_string($customerInfo);

    $query = "INSERT INTO ordering(address) VALUES ('$customerInfo')";

    if($database->query($query)){
        return $database->insert_id;
    }
    return -1;
}

function insertDataToOrderedArticle($id){
    global $database;
    $orderedPizzas = getOrderedPizza();

    foreach ($orderedPizzas as $pizza_id){
        $query = "INSERT INTO ordered_article(ordering_id,article_id,status) VALUES ($id,$pizza_id,0)";
        $database->query($query);
    }
}

function getAllRecordsAsJSON($query){
    global $database;
    if (!($recordset = $database->query($query))) return "Query failed: ".$query;
    else {
        $res = array();
        while ($record = $recordset->fetch_assoc()) { $res[] = $record; }
        $recordset->free();
        return json_encode($res);
    }
}
function getAllOrderedPizza($id){
    $query = "SELECT * FROM article NATURAL JOIN ordered_article WHERE ordering_id = $id";

    echo getAllRecordsAsJSON($query);
}

function checkIfEntryExist($id){
    global $database;

    $query = "SELECT name,status FROM article NATURAL JOIN ordered_article WHERE ordering_id = $id";
    $result = $database->query($query);

    if(mysqli_num_rows($result)>0) echo 1;
    echo 0;
}

function getAllBackerPizza($status){
    $query = "SELECT ordering_id,ordered_article_id,name,status FROM ordered_article NATURAL JOIN article WHERE status < $status ORDER BY status DESC";

    echo getAllRecordsAsJSON($query);
}

function insertNewPizza(){
    $orderID = insertDataToOrdering();
    insertDataToOrderedArticle($orderID);
    echo $orderID;
}

function updateBakingStatus(){
    if(!isset($_POST['ordered_article_id']) || !isset($_POST['status'])){
        return;
    }

    global $database;

    $ordered_article_id = $_POST['ordered_article_id'];
    $status = $_POST['status'];

    $query = "UPDATE ordered_article SET status = $status WHERE ordered_article_id = $ordered_article_id";
    $database->query($query);
}

function updateDeliveryStatus(){
    if(!isset($_POST['ordering_id']) || !isset($_POST['status'])){
        return;
    }
    global $database;

    $ordering_id = $_POST['ordering_id'];
    $status = $_POST['status'];

    $query = "UPDATE ordered_article SET status = $status WHERE ordering_id = $ordering_id";
    $database->query($query);
}

function getCourierPizza(){
    $query = "SELECT address,name,price,ordering_id,status FROM ordered_article NATURAL JOIN ordering NATURAL JOIN article WHERE status < 4 GROUP BY ordering_id,ordered_article_id";

    echo getAllRecordsAsJSON($query);
}

if ($_SERVER["REQUEST_METHOD"]=="POST"){

    if($_POST['type'] ==  "allOrdered"){
        insertNewPizza();
    }

    if($_POST['type'] ==  "updateStatus"){
        updateBakingStatus();
    }

    if($_POST['type'] ==  "updateDelivery"){
        updateDeliveryStatus();
    }

}

if($_SERVER["REQUEST_METHOD"]=="GET"){
    header("Content-Type: application/json");
    $data = json_decode(stripslashes($_GET["data"]));

    if ($data->type == "orderID"){
        $orderID = $data->value;
        getAllOrderedPizza($orderID);
    }

    if ($data->type == "checkExist"){
        $orderID = $data->value;
        checkIfEntryExist($orderID);
    }

    if ($data->type == "allOrdered"){
        getAllBackerPizza($data->value);
    }

    if ($data->type == "allCourier"){
        getCourierPizza();
    }

}
