function convertDateOfClient(date){
    return date.split('-').reverse().join('/');
}

module.exports.convertDateOfClient = convertDateOfClient;