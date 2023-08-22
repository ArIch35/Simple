/* eslint-disable no-template-curly-in-string */
const {query} = require('./database')
const express = require('express');
const router = express.Router();

router.use(express.urlencoded({extended:false}));
router.use(express.json()); // Parse JSON data
router.get("/",(req,res) => {
    query("SELECT * FROM meetups").then((value)=>{
        return res.status(200).json(value);
    }).catch((error) => {
        console.log(error);
        return res.status(500);
    })
});

router.post("/",(req,res) => {
    console.log(req.body);
    const { id, title, img_src, address, desc } = req.body;

    query("INSERT INTO meetups VALUES (?,?,?,?,?)",[id,title,img_src,address,desc])
    .then(()=>{
        return res.status(200).json({sucess:true});
    }).catch((error) => {
        console.log(error);
        return res.status(500).json({sucess:false});
    })
});



module.exports = router
