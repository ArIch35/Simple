setInterval(getOrdersFromDB, 10000);

window.addEventListener("DOMContentLoaded",function(){
    getOrdersFromDB();
},false);

function getOrdersFromDB(){
    const container = document.getElementById("container-orders");
    container.innerHTML = "";
    getCourierPizza(callbackCourier);
}

function generateFieldset(address,pizzas,price,ordering_id,status){
    const container = document.getElementById("container-orders");
    const fieldset = document.createElement("fieldset");
    fieldset.className = "flex-fieldset-container";
    fieldset.appendChild(generateTitle("Bestellung: " + ordering_id));
    fieldset.appendChild(generateParagraph(address));
    fieldset.appendChild(generateParagraph("Pizzas: " + pizzas));
    fieldset.appendChild(generateParagraph("Price: " + price + " €"));
    fieldset.appendChild(generateRadioBox(ordering_id,status));
    
    container.appendChild(fieldset);
}

function generateTitle(title){
    const legend = document.createElement("legend");
    legend.className = "fieldset-legend";
    legend.innerHTML = title;
    return legend;
}

function generateParagraph(value){
    const par = document.createElement("p");
    par.innerHTML = value;
    return par;
}

function generateRadioBox(ordering_id,status){
    const div = document.createElement("div");

    if(status == 3){
        generateStatusRadio(div,ordering_id,true,false,false,false);
    }

    else if(status == 4){
        generateStatusRadio(div,ordering_id,false,true,true,false);
    }

    else{
        generateStatusRadio(div,ordering_id,false,false,false,true);
    }

    return div;
}

function generateStatusRadio(div,ordering_id,status1,status2,disable1,disable2){
    div.appendChild(generateRadioButton(ordering_id,"ontheway",status1,disable1,function(){updateStatusOrder(ordering_id,3)}));
    div.appendChild(generateLabel("unterwegs","ontheway"));
    div.appendChild(generateRadioButton(ordering_id,"arrived",status2,disable2,function(){disableOTWRadio(ordering_id,"ontheway")}));
    div.appendChild(generateLabel("geliefert","arrived"));
}

function generateRadioButton(ordering_id,value,status,disable,listener){
    const radio = document.createElement("input");
    radio.type = "radio";
    radio.name = value+":"+ordering_id;
    radio.id = value+":"+ordering_id;
    radio.value = value+":"+ordering_id;

    radio.checked = status;
    radio.disabled = disable;

    radio.addEventListener("change",function(){
        listener(ordering_id,value);
    },false);

    return radio;
}

function disableOTWRadio(ordering_id,value){
    const radio = document.getElementById(value+":"+ordering_id);
    radio.checked = false;
    radio.disabled = true;
    updateStatusOrder(ordering_id,4);
}


function generateLabel(name,value){
    const label = document.createElement("label");
    label.setAttribute("for", value);
    label.innerHTML = name;
    return label;
}

function getCourierPizza(callback){
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
    const url = encodeURI("http://localhost/Praktikum/Backend/test.php?data=") + encodeURIComponent(JSON.stringify({type: "allCourier",value: -1}));
    xhr.open("get", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function callbackCourier(response){
    const output = JSON.parse(response);
    let prev_address = "";
    let prev_id = -1;
    let prev_pizza = "";
    let pizza_list = [];
    let sum = 0;
    let status = 2;
    let highestStatus = 0;

    if (output.length === 0){
        return;
    }

    output.forEach(obj => {

        if(prev_id === -1){
            prev_id = obj.ordering_id;
            prev_address = obj.address;
        }

        if(prev_id !== obj.ordering_id){
            if(status >= 2){
                generateFieldset(prev_address,parsePizza(pizza_list),sum,prev_id,highestStatus);
            }
            prev_id = obj.ordering_id;
            prev_address = obj.address;
            sum = 0;
            pizza_list = [];
            status = 2;
            highestStatus = 0;
        }
        
        if(prev_pizza === "" || prev_pizza !== obj.name || prev_id === obj.ordering_id){
            prev_pizza = obj.name;
           
            pizza_list.push(prev_pizza);
        }

        sum+=parseFloat(obj.price);
        
        if(status > obj.status){
            status = obj.status;
        }

        if(highestStatus <= obj.status){
            highestStatus = obj.status;
        }
    }); 
    if(status >= 2){
        generateFieldset(prev_address,parsePizza(pizza_list),sum,prev_id,highestStatus);
    }
}

function parsePizza(list_pizza){
    let output = list_pizza[0];
    let prev_pizza = list_pizza[0];

    for(let i=0;i<list_pizza.length;i++){
        if(prev_pizza != list_pizza[i]){
            prev_pizza = list_pizza[i];
            output = output + ", " + prev_pizza;
        }
    }

    return output;
}

function updateStatusOrder(ordering_id,value){
    const xhr = new XMLHttpRequest();
    let formData = new FormData();

    formData.append("type", "updateDelivery");
    formData.append("ordering_id", ordering_id);
    formData.append("status",value);

    xhr.open("post", encodeURI("http://localhost/Praktikum/Backend/test.php"), true);
    xhr.send(formData);
}