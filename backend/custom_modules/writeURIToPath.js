const fs = require('fs');
const sharp = require('sharp');
const {convertDateForFile} = require("./dateConverters");
const {generateImagePath} = require("./pathGenerator");
const {PATH_ROOT_FOLDER} = require("../absoluteLocationPath");

async function writeURIToPath(uri,date,id){
    return new Promise(async (resolve,reject) => {
        try {
            await checkIfPathExist(PATH_ROOT_FOLDER);
            const path = generateFolderByDate(PATH_ROOT_FOLDER, date);
            const location = await writeFile(uri, path, id);
            resolve(location);
        } catch (err) {
            reject(err);
        }
    })
    
}
        
async function checkIfPathExist(path){
    return new Promise(async (resolve,reject) =>{
        try{
            await fs.promises.access(path);
            resolve();
        }catch(error){
            reject(error);
        }
    })
}

function generateFolderByDate(root,date){
    const new_path = root+"/"+convertDateForFile(date);
    checkIfPathExist(new_path).then().catch(() => {
        fs.mkdirSync(new_path);
    });
    return new_path;
}

function getFileType(input){
    return input.split("/")[1].split(";")[0];
}

function writeFile(uri,path,id){
    return new Promise((resolve,reject) =>{
        const [type,base64Data] = uri.split(',');
        const fileType = getFileType(type);
        const fileloc = generateImagePath(path,id,fileType);
        const imageBuffer = Buffer.from(base64Data, 'base64');
        sharp(imageBuffer)
        .toFormat(fileType)
        .toFile(fileloc, (err, info) => {
        if (err) reject(err);
        resolve(fileType);
      });
    });
}

module.exports.writeURIToPath = writeURIToPath;
module.exports.checkIfPathExist = checkIfPathExist;

