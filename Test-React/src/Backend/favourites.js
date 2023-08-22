/* eslint-disable no-template-curly-in-string */
const {query} = require('./database')
const express = require('express');
const router = express.Router();

//router.use(express.urlencoded({extended:false}));
router.use(express.json()); // Parse JSON data
router.get("/",(req,res) => {
    query("SELECT * FROM meetups WHERE id in (SELECT meetup FROM favourite)").then((value)=>{
        return res.status(200).json(value);
    }).catch((error) => {
        console.log(error);
        return res.status(500);
    })
});

router.post("/",(req,res) => {
    console.log(req.body);
    const { id } = req.body;

    query("INSERT INTO favourite (meetup) VALUES (?)",[id])
    .then(()=>{
        return res.status(200).json({sucess:true});
    }).catch((error) => {
        console.log(error);
        return res.status(500).json({sucess:false});
    })
});

router.delete("/:id",(req,res) => {
    const id = req.params.id;

    query("DELETE FROM favourite WHERE meetup = ?",[id])
    .then(()=>{
        return res.status(200).json({sucess:true});
    }).catch((error) => {
        console.log(error);
        return res.status(500).json({sucess:false});
    })
});

router.get("/isFavourite/:id",(req,res) => {
    const id = req.params.id;

    query("SELECT * FROM favourite WHERE meetup = ?",[id])
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



module.exports = router
