var mysql = require('mysql');

const query = (sql,props) => {
    return new Promise( ( resolve, reject ) => {
        let connection = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'phototracker'
        });
        if(props){
            return connection.query( sql, props, ( err, rows ) => {
                connection.end();
                if (err) return reject( err );
                resolve( rows );
            });
        }
        connection.query( sql, ( err, rows ) => {
            connection.end();
            if (err) return reject( err );
            resolve( rows );
        });
    });
}

module.exports.query = query;