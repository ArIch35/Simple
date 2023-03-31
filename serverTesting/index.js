
const conn = require('express')();
const PORT = 3306;
const HOST = 'localhost';

conn.get('/test', (req, res) => {
    res.status(200).send({
        val: '1',
        stat: 'sucess'
    })

});


conn.listen(
    PORT,
    HOST,
    () => console.log('alive on http://' + HOST + ':' + PORT)
)


