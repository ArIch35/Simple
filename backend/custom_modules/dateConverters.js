function convertDateForFile(date){
    return date.split('/').join('_');
}

function convertDateForSQL(date){
    return date.split('/').reverse().join('-');
}

function convertDateForReconstruction(date){
    return date.split('-').reverse().join('_');
}

module.exports.convertDateForFile = convertDateForFile;
module.exports.convertDateForSQL = convertDateForSQL;
module.exports.convertDateForReconstruction = convertDateForReconstruction;
