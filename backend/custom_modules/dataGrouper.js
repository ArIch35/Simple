const {reconstructImagePath} = require("./pathGenerator");
const {PATH_ROOT_FOLDER} = require("../absoluteLocationPath");
const {convertDateForSQL} = require('./dateConverters');

function dataGrouper(data){
    const holder = {}
    data.forEach(element => {
        const key = element.imageDate;
        if(!holder[key]){
            holder[key] = []
        }
        holder[key].push({path:reconstructImagePath(PATH_ROOT_FOLDER,convertDateForSQL(element.imageDate),element.imageID,element.type),type:element.type});
    });
    return holder;
}

module.exports.dataGrouper = dataGrouper;