const express = require('express');
const meetups = require('./meetups');
const favourite = require('./favourites');
const cors = require('cors'); 

const app = express();
app.use(cors());
app.use("/meetups",meetups);
app.use("/favourites",favourite);


app.listen(2587,()=>{
    console.log("Listening on Port 2587");
});

