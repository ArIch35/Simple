function setJSONValueByKey(data,key,value){
    const temp = data;
    temp[key] = value;
    return temp;
}

export default setJSONValueByKey;