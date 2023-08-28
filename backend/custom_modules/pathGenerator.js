function generateImagePath(path,id,fileType){
    return path + "/" + id + "." + fileType;
}

function reconstructImagePath(path,date,id,fileType){
    const {convertDateForReconstruction} = require('./dateConverters');
    return path + "/" + convertDateForReconstruction(date) +"/" + id + "." + fileType;
}

module.exports.generateImagePath = generateImagePath;
module.exports.reconstructImagePath = reconstructImagePath;