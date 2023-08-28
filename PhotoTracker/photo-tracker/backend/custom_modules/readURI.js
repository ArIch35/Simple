const fs = require('fs');
const {checkIfPathExist} = require('./writeURIToPath');
const {reconstructImagePath} = require("./pathGenerator");
const {convertDateForSQL} = require('./dateConverters');
const {PATH_ROOT_FOLDER} = require("../absoluteLocationPath");

async function readURI(data){
    return new Promise(async (resolve,reject)=>{
        const output = []
        for(const key in data){
            const holder = []
            await Promise.all(data[key].map(async (value) => {
                const { path, type } = value;
                try {
                    const uri = await readURIFromPath(type, path);
                    holder.push(uri);
                } catch (err) {
                    reject(err);
                }
            }));
            output.push({date:key,uris:holder});
        }
        resolve(output);
    });
}

async function readImage(data) {
    return new Promise(async (resolve, reject) => {
        const output = [];
        const promises = []; 

        for (const val of data) {
            promises.push(
                (async () => {
                    try {
                        const { imageID, description, imageDate, title, type } = val;
                        const path = reconstructImagePath(PATH_ROOT_FOLDER, convertDateForSQL(imageDate), imageID, type);
                        const uri = await readURIFromPath(type, path);
                        output.push({ id:imageID, title: title, desc: description, uri: uri });
                    } catch (err) {
                        reject(err);
                    }
                })()
            );
        }

        try {
            await Promise.all(promises);
            resolve(output);
        } catch (error) {
            reject(error);
        }
    });
}


async function readURIFromPath(type,path){
    return new Promise(async (resolve,reject) => {
        try {
            await checkIfPathExist(path);
            fs.readFile(path,(err,data)=>{
                if(err) throw err;
                const base64Data = data.toString('base64');
                resolve(`data:${generateMIME(type)};base64,${base64Data}`);
            })
        } catch (error) {
            reject(error);
        }
    });
}

function generateMIME(type){
    return "image/" + type;
}

module.exports.readURI = readURI;
module.exports.readImage = readImage;