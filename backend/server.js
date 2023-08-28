const express = require('express');
const {query} = require('./database')
const {convertDateForSQL} = require('./custom_modules/dateConverters');
const {writeURIToPath} = require('./custom_modules/writeURIToPath');
const {readURI,readImage} = require("./custom_modules/readURI");
const {dataGrouper} = require('./custom_modules/dataGrouper');
const cors = require('cors'); 

const app = express();
app.use(cors({
    maxBodyLength: 1024 * 1024 * 10
  }));
app.use(express.json({limit: '50mb'}));
app.use(express.urlencoded({limit: '50mb'}));
app.post("/",(req,res) => {
    const { id,title,date,desc,uri } = req.body;
    console.log(date)
    writeURIToPath(uri,date,id).then((result) => {
        
        query("INSERT INTO images(imageID,title,imageDate,description,type) VALUES (?,?,?,?,?)",[id,title,convertDateForSQL(date),desc,result])
        .then(()=>{
            return res.status(200).json({sucess:true});
        }).catch((error) => {
            console.log(error);
            return res.status(500).json({sucess:false});
    })
    }).catch((err) => {
        console.error(err);
        return err;
    });  
})

app.get("/",(req,res)=>{
    query("SELECT imageDate, imageID, type FROM (SELECT imageDate, imageID, type,ROW_NUMBER() OVER (PARTITION BY imageDate ORDER BY imageID) AS row_num FROM images) ranked WHERE row_num <= 1;")
    .then((value)=>{
        const output = dataGrouper(value);
        readURI(output).then((data)=>{
            return res.status(200).json(data);
        }).catch((err)=>{
            throw(err)
        });
    }).catch((error) => {
        console.log(error);
        return res.status(500);
    })
});

app.get("/favourites",(req,res)=>{
    query("SELECT imageDate, imageID, type FROM (SELECT imageDate, imageID, type,ROW_NUMBER() OVER (PARTITION BY imageDate ORDER BY imageID) AS row_num FROM images) ranked WHERE row_num <= 1 and imageID in (SELECT imageID FROM favourites);")
    .then((value)=>{
        const output = dataGrouper(value);
        readURI(output).then((data)=>{
            return res.status(200).json(data);
        }).catch((err)=>{
            throw(err)
        });
    }).catch((error) => {
        console.log(error);
        return res.status(500);
    })
});

app.get("/:date",(req,res) => {
    const date = req.params.date;

    query("SELECT * FROM images WHERE imageDate = ?",[date])
    .then((value)=>{
        readImage(value).then((data)=>{
            return res.status(200).json(data);
        }).catch((err)=>{
            throw(err)
        });
    }).catch((error) => {
        console.log(error);
        return res.status(500);
    })
});

app.get("/favourites/:date",(req,res) => {
    const date = req.params.date;

    query("SELECT * FROM images WHERE imageDate = ? and imageID in (SELECT imageID FROM favourites)",[date])
    .then((value)=>{
        readImage(value).then((data)=>{
            return res.status(200).json(data);
        }).catch((err)=>{
            throw(err)
        });
    }).catch((error) => {
        console.log(error);
        return res.status(500);
    })
});

app.get("/favourites/count/:date",(req,res) => {
    const date = req.params.date;

    query("SELECT COUNT(*) as total FROM images WHERE imageDate = ? and imageID in (SELECT imageID FROM favourites)",[date])
    .then((value)=>{
        return res.status(200).json(value);
    }).catch((error) => {
        console.log(error);
        return res.status(500);
    })
});

app.get("/favourites/popup/:date",(req,res) => {
    const date = req.params.date;

    query("SELECT * FROM images WHERE imageDate = ? and imageID IN (SELECT imageID from favourites) LIMIT 3",[date])
    .then((value)=>{
        const output = dataGrouper(value);
        readURI(output).then((data)=>{
            return res.status(200).json(data);
        }).catch((err)=>{
            throw(err)
        });
    }).catch((error) => {
        console.log(error);
        return res.status(500);
    })
});

app.delete("/favourites/:id",(req,res) => {
    const id = req.params.id;

    query("DELETE FROM favourites WHERE imageID = ?",[id])
    .then(()=>{
        return res.status(200).json({sucess:true});
    }).catch((error) => {
        console.log(error);
        return res.status(500).json({sucess:false});
    })
});

app.get("/popup/:date",(req,res) => {
    const date = req.params.date;

    query("SELECT * FROM images WHERE imageDate = ? LIMIT 3",[date])
    .then((value)=>{
        const output = dataGrouper(value);
        readURI(output).then((data)=>{
            return res.status(200).json(data);
        }).catch((err)=>{
            throw(err)
        });
    }).catch((error) => {
        console.log(error);
        return res.status(500);
    })
});

app.get("/isFavourite/:id",(req,res) => {
    const id = req.params.id;

    query("SELECT * FROM favourites WHERE imageID = ?",[id])
    .then((row)=>{
        if (row && row.length) {
            console.log(id + ' Exist!');
            return res.status(200).json({sucess:true,exist: true});
        }
        console.log(id + ' Dont Exist!');
        return res.status(200).json({sucess:true,exist: false});
    }).catch((error) => {
        console.log(error);
        return res.status(500).json({sucess:false});
    })
});

app.post("/favourites/",(req,res) => {
    console.log(req.body);
    const { id } = req.body;

    query("INSERT INTO favourites (imageID) VALUES (?)",[id])
    .then(()=>{
        return res.status(200).json({sucess:true});
    }).catch((error) => {
        console.log(error);
        return res.status(500).json({sucess:false});
    })
});


app.listen(2587,()=>{
    console.log("Listening on Port 2587");
});

