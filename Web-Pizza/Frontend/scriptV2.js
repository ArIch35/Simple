/* SCRIPT PRAKT 3 */

const del_one_pizza = document.getElementById("delete-one-pizza");
const submit = document.getElementById("warenkorb");

const pizza_box = document.getElementById("pizza_list");
pizza_box.addEventListener("click",optionOnClickListener,false);
pizza_box.addEventListener("dblclick",optionOnDblClickListener,false);

del_one_pizza.addEventListener("click",deleteSomeItem,false);
del_one_pizza.addEventListener("load",optionOnClickListener,false);

const del_all = document.getElementById("delete-all-pizza");
del_all.addEventListener("click",deleteAllItems,false);

const reset_all = document.getElementById("reset_all_button");
reset_all.addEventListener("click",deleteAllItems,false);

function addPizzaObject(obj){
    const pizza_container = document.getElementById("flex-pizza-container");    
    let new_div = this.document.createElement("div");
        
    new_div.appendChild(createNewImage("https://www.pizza-mono.de/wp-content/uploads/2018/12/mono_share_main_01-e1544983660357-600x600.jpg"));
    new_div.appendChild(createNewDesc(obj));
    new_div.className = "flex-pizza-item";
    new_div.addEventListener("click",function(){
        addItem(obj.name,obj.article_id,obj.price);
    },false);
    pizza_container.appendChild(new_div);
}

function createNewImage(src){
    const div_img = this.document.createElement("img");
    div_img.src = src;
    div_img.width = 225;
    div_img.height = 300;
    return div_img;
}

function createNewDesc(obj){
    const div_desc = this.document.createElement("p");
    const desc = obj.name + " : " + obj.price + " €"
    div_desc.innerHTML = desc;
    return div_desc;
}

function addItem(item_name,item_ID,price) {
    modifyTotalPrice(price);
    if(checkIfPizzaInOptions(item_ID)){
        const duplicate_opt = document.getElementById(item_ID);
        modifyDuplicateItem(duplicate_opt,1);
        return;
    }
    const opt = getNewItem(item_name,item_ID,price);
    document.getElementById("pizza_list").appendChild(opt);
    setSubmitButtonDisabled(false);
}

function checkIfPizzaInOptions(item_ID){
    return getListArray().some(id =>{
        if(id == item_ID){
            return true;
        }
    });
}

function modifyTotalPrice(modifier){
    document.getElementById("price").innerText = parseFloat(getNewPrice(modifier));
}

function getNewPrice(modifier){
    const old_price = document.getElementById("price").innerHTML;
    return parseFloat(old_price) + parseFloat(modifier);
}

function getNewItem(item_name,item_ID,item_price){
    const opt = document.createElement('option');
    opt.name = "article";
    opt.innerHTML = item_name;
    opt.id = item_ID;
    opt.value = item_ID +":" + 1;
    opt.dataset.count = 1;
    opt.dataset.itemName = item_name;
    opt.dataset.itemPrice = item_price;
    return opt;
}

function modifyDuplicateItem(option,modifier){
    const new_val = parseInt(option.dataset.count) + modifier;
    option.value = option.id +":" + new_val;
    option.dataset.count = new_val;

    if(new_val == 1){
        option.innerHTML = option.dataset.itemName;
        return;
    }

    option.innerHTML = option.dataset.itemName + " " + new_val + "x";
}

function deleteAllItems(){
    document.getElementById("pizza_list").innerHTML = "";
    document.getElementById("price").innerHTML = "0";
    setDeleteButtonDisabled(true);
    setSubmitButtonDisabled(true);
}

function getListArray(){
    let temp = [];
    const options = document.getElementById("pizza_list").options;
    for(let val of options){
        temp.push(val.id);
    }
    return temp;
}

function setAllOptionStatus(status){
    const options = document.getElementById("pizza_list").options;
    for(let val of options){
        val.selected = status;
    }
}

function deleteSomeItem(){
    const list_pizza = document.getElementById("pizza_list");
    const list_selected = getSelectedOptions();

    for(let opt of list_selected){
        modifyTotalPrice(opt.dataset.itemPrice*-1);
        if(opt.dataset.count <= 1){
            list_pizza.removeChild(opt);
            if(list_selected.length === 1){
                setDeleteButtonDisabled(true);
            }
        }
        modifyDuplicateItem(opt,-1);
    }    
}

function optionOnClickListener(){
    const list_pizza = document.getElementById("pizza_list");
    
    if(list_pizza.length == 0){
        setDeleteButtonDisabled(true);
        return;
    }
    setDeleteButtonDisabled(false);
}

function optionOnDblClickListener(){
    getSelectedOptions().selected = false;
    setDeleteButtonDisabled(true);
}

function setDeleteButtonDisabled(status){
    const delete_button = document.getElementById("delete-one-pizza");
    delete_button.disabled = status;
}

function setSubmitButtonDisabled(status){
    const submit_button = document.getElementById("submit_button");
    submit_button.disabled = status;
}

function getSelectedOptions(){
    const list_pizza = document.getElementById("pizza_list");
    let holder = [];
    for(let opt of list_pizza){
        if(opt.selected){
            holder.push(opt);
        }
    }
    return holder;
}

function checkIfAnyItemSelected(){
    const list_pizza = document.getElementById("pizza_list");
    for(let opt of list_pizza){
        if(opt.selected){
            return true;
        }
    }
    return false;
}

/* SCRIPT PRAKT4*/

const statusPizza = ["bestellt", "im Ofen", "fertig", "unterwegs", "geliefert"];

window.addEventListener('DOMContentLoaded',function(){
    del_one_pizza.disabled = true;
    submit.disabled = true;
    
    //AF 2
    //loadMenuFromFile();
    //AF 3
    loadMenuFromDB();
},false)

submit.addEventListener("submit",function(event){
    setAllOptionStatus(true); 
    
    if(checkFormFields()){
        //Aufgabe 1
        //sendData(callbackOrderIDWebStorageAPI);
        //Aufgabe 2
        sendData(callbackOrderIDHistory);
    }
    event.preventDefault();
    return false;

},false);

function loadMenuFromFile(){
    fetch('article.json', {method: 'get', mode: 'cors'})
    .then(response => response.json())
    .then(data => {
        data.forEach(obj =>{
            addPizzaObject(obj);
        });
    })
    .catch(err => console.error(err));
}

function loadMenuFromDB(){
    fetch(encodeURI('http://localhost/Praktikum/Backend/index.php?table=article'), {method: 'get', mode: 'cors'})
    .then(response => response.json())
    .then(data => {
        data.forEach(obj =>{
            addPizzaObject(obj);
        });
    })
    .catch(err => console.error(err));
}

function checkFormFields(){
    const input_types = document.getElementsByTagName('input');
    const name = document.getElementById("name");
    const email = document.getElementById("email");
    const address = document.getElementById("addresse");
    
    let wunsch_choosed = false;
    let valid = true;

    for(let inp of input_types){
        if(inp.type === 'radio' && inp.checked){
            wunsch_choosed = true;
        }
    }

    if(!wunsch_choosed){
        generateAlertMessage("radio_warning","Keine Packingsoption gewählt!");
        valid = false;
    }else{
        document.getElementById("radio_warning").style.display="none";
    }

    if(!checkNameField(name)){
        valid = false;
    }else{
        hideAlertMessage("name_warning");
    }

    if(!checkEmailField(email)){
        valid = false;
    }else{
        hideAlertMessage("email_warning");
    }

    if(address.value == null || address.value == ''){
        generateAlertMessage("address_warning","Keine Adresse!");
        valid = false;
    }else{
        document.getElementById("address_warning").style.display="none";
    }

    return valid;
}

function checkNameField(name){    
    let valid = true;
    let msgs = "";
    
    if(name.value == null || name.value == ''){
        generateAlertMessage("name_warning","Keine Name!");
        return false;
    }
    
    if(name.value.indexOf(' ') === -1){
        msgs += "Name nicht durch Leerzeichen getrennt!<br>" ;
        generateAlertMessage("name_warning","Name nicht durch Leerzeichen getrennt!");
        valid = false;
    }
    
    if(name.value.search(/\d/) !== -1){
        msgs += "Name enthält Nummer! <br>";
        valid = false;
    }

    var format = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+/;
    if(format.test(name.value)){
        msgs += "Name enthält Sonderzeichen!<br>";
        valid = false;
    }

    if(hasCode(name.value)){
        msgs += "Name enthält Code!<br>";
        valid = false;
    }

    if(!valid){
        generateAlertMessage("name_warning",msgs);
    }
    
    return valid;
}

function checkEmailField(email){
    if(email.value == null || email.value == ''){
        generateAlertMessage("email_warning","Keine Email!");
        return false;
    }

    var model = /\S+@\S+\.\S+/;
    if(!model.test(email.value)){
        generateAlertMessage("email_warning","Ungültige Email!");
        return false;
    }

    if(hasCode(email.value)){
        generateAlertMessage("email_warning","Enthält Code!");
        return false;
    }

    return true;
}

function hasCode(input) {
    var regex = /[<>{}()]/;
    return regex.test(input);
  }

function generateAlertMessage(id,message){
    const elem = document.getElementById(id);
    elem.innerHTML = " " + message;
    elem.style.display="block";
}

function hideAlertMessage(id){
    document.getElementById(id).style.display="none";
}

function sendData(callback){
    const form = document.getElementById("warenkorb");
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
              const response = xhr.responseText;
              orderID = response;
              history.pushState({page: "showForm", sessionID: orderID},"","");
              callback(orderID);
            } else {
              alert("Request failed with status:", xhr.status);
            }
          }
        
    };
    let formData = new FormData(form);

    formData.append("type", "allOrdered");

    xhr.open("post", encodeURI("http://localhost/Praktikum/Backend/test.php"), true);
    xhr.send(formData);
}

function changeMode(){
    const warenkorb = document.getElementById("warenkorb");
    const bestellstatus = document.getElementById("bestellstatus");
    warenkorb.style.display = "none";
    bestellstatus.style.display = "block";
}

function goBack(){
    const warenkorb = document.getElementById("warenkorb");
    const bestellstatus = document.getElementById("bestellstatus");
    warenkorb.style.display = "block";
    bestellstatus.style.display = "none";
}

function generateTableElements(name,status){
    let table = document.getElementById("order-table");
    generateRow(table,name,status);
}

function generateRow(table,name,status){
    let row = table.insertRow(1);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    cell1.className = "table-member";
    cell2.className = "table-member";
    cell1.innerHTML = name;
    cell2.innerHTML = statusPizza[status]; 
}

/* Prakt 5*/
setInterval(loadSessionHistoryAPI, 10000);

let orderID = -1;


function getOrderedPizzaByID(orderID,callback){
    generateGetCall("orderID",orderID,callback);
}

function callbackUpdateTable(response){
    const output = JSON.parse(response);
    let table = document.getElementById("order-table");
    while (table.rows.length > 1) {
        table.deleteRow(1);
      }
    output.forEach(obj => generateTableElements(obj.name,obj.status)); 
}

function generateGetCall(type_get,value_get,callback){
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
    const url = encodeURI("http://localhost/Praktikum/Backend/test.php?data=") + encodeURIComponent(JSON.stringify({type: type_get,value: value_get}));
    xhr.open("get", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

/* Aufgabe 1 */
//Aufgabe 1
window.addEventListener('load',function(){
    loadSessionHistoryAPI();
},false);

function callbackOrderIDWebStorageAPI(orderID){
    sessionStorage.setItem('sessionID', orderID);
    console.log(orderID);
    getOrderedPizzaByID(orderID,callbackUpdateTable);
    changeMode();
}

function loadSessionWebStorageAPI(){
    sessionID = checkEntryExistWebStorageAPI();
    if(sessionID !== null){
        generateGetCall("checkExist",sessionID,callbackEntryExistWebStorageAPI);
    }
}

function checkEntryExistWebStorageAPI(){
    if(sessionStorage.getItem('sessionID') !== null){
        return sessionStorage.getItem("sessionID");  
    }
    return null;
}

function restoreSessionWebStorageAPI(){
    sessionID = checkEntryExistWebStorageAPI();
    if(sessionID !== null){
        callbackOrderIDWebStorageAPI(sessionID); 
    }
}

function callbackEntryExistWebStorageAPI(response){
    if(response == 0){
        sessionStorage.removeItem("sessionID");
    }
    restoreSessionWebStorageAPI();
}

/* Aufgabe 2 */

window.addEventListener('popstate',function(){
    loadSessionHistoryAPI();
},false);

function loadSessionHistoryAPI(){
    if(history.state.page === "showForm"){
        getOrderedPizzaByID(history.state.sessionID,restoreBuyList);
        goBack();
    }
    else{
        getOrderedPizzaByID(history.state.sessionID,callbackUpdateTable);
        changeMode();
    }
    
}

function restoreBuyList(response){
    deleteAllItems();
    const output = JSON.parse(response);
    output.forEach(obj => addItem(obj.name,obj.article_id,obj.price)); 
    setSubmitButtonDisabled(true);
}

function callbackOrderIDHistory(orderID){
    history.pushState({page: "showOrder", sessionID: orderID},"","");
    getOrderedPizzaByID(orderID,callbackUpdateTable);
    changeMode();
}
