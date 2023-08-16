const statusPizza = ["bestellt", "im Ofen", "fertig", "unterwegs", "geliefert","geloest"];

setInterval(fillOrderTable, 10000);

window.addEventListener("DOMContentLoaded",function(){
    fillOrderTable();
},false);

function getAllOrderedPizza(status,callback){
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
              const response = xhr.responseText;
              callback(response);
            } else {
              alert("Request failed with status:", xhr.status);
            }
          }
        
    };
    const url = encodeURI("http://localhost/Praktikum/Backend/test.php?data=") + encodeURIComponent(JSON.stringify({type: "allOrdered",value: status}));
    xhr.open("get", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function fillOrderTable(){
    getAllOrderedPizza(2,callbackBackerPizza);
}

function callbackBackerPizza(response){
    const output = JSON.parse(response);
    let table = document.getElementById("order-table");
    while (table.rows.length > 1) {
        table.deleteRow(1);
      }
    output.forEach(obj => {
        generateTableElements(obj.ordering_id,obj.ordered_article_id,obj.name,obj.status)
    }); 
}

function generateTableElements(order_id,ordered_article_id,name,status){
    let table = document.getElementById("order-table");
    generateRow(table,order_id,ordered_article_id,name,status);
}

function generateRow(table,order_id,ordered_article_id,name,status){
    let row = table.insertRow(1);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    var cell4 = row.insertCell(3);
    cell1.className = "table-member";
    cell2.className = "table-member";
    cell3.className = "table-member";
    cell4.className = "table-member";
    cell1.innerHTML = order_id;
    cell2.innerHTML = name;
    cell3.innerHTML = statusPizza[status];

    row.setAttribute("data-row-id", ordered_article_id);
    row.setAttribute("data-status", status);
    
    
    cell4.appendChild(generateButton(ordered_article_id));
}

function generateButton(ordered_article_id){
    const button = document.createElement("button");
    button.textContent = ">";
    button.className = "button-backer";
    button.addEventListener("click",function(){
        buttonClicked(ordered_article_id);
    },false);
    return button;
}

function buttonClicked(ordered_article_id){
    const new_status = updateRow(ordered_article_id);;
    updateStatusDatabase(ordered_article_id,new_status);
}

function updateRow(ordered_article_id){
    const row = document.querySelectorAll("[data-row-id='"+ordered_article_id+"']");
    let new_status = parseInt(row[0].getAttribute("data-status")) + 1;
    row[0].setAttribute("data-status",new_status);

    row[0].cells[2].innerHTML = statusPizza[new_status];
    
    if(new_status >= 2){
        const button = row[0].cells[3].querySelector("button");
        button.disabled = true;
    }
    return new_status;
}

function updateStatusDatabase(ordered_article_id,status){
    const xhr = new XMLHttpRequest();
    let formData = new FormData();

    formData.append("type", "updateStatus");
    formData.append("ordered_article_id", ordered_article_id);
    formData.append("status", status);

    xhr.open("post", encodeURI("http://localhost/Praktikum/Backend/test.php"), true);
    xhr.send(formData);
}

