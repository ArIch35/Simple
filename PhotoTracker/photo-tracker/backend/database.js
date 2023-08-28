var mysql = require('mysql');

function createDatabaseIfNotExist(){
    const connection = mysql.createConnection({
      host: 'localhost',
      user: 'root',
      password: ''
    });
  
    const databaseName = 'phototracker';
  
    connection.connect((err) => {
      if (err) {
        console.error('Error connecting to MySQL:', err);
        return;
      }
  
      connection.query(`CREATE DATABASE IF NOT EXISTS ${databaseName}`, (err) => {
        if (err) {
          console.error('Error creating database:', err);
        } else {
          console.log(`Database ${databaseName} is ready.`);
          createTablesIfNotExist();
        }
      });
    });
  };
  
   const createTablesIfNotExist = () => {
    const connection = mysql.createConnection({
      host: 'localhost',
      user: 'root',
      password: '',
      database: 'phototracker'
    });
  
    const createImageTableQuery = "CREATE TABLE IF NOT EXISTS images ( imageID varchar(16) NOT NULL,description varchar(10000) DEFAULT NULL,imageDate varchar(32) DEFAULT NULL,title varchar(256) DEFAULT NULL,type varchar(16) DEFAULT NULL,PRIMARY KEY (imageID))";
  
    connection.query(createImageTableQuery, (err) => {
      if (err) {
        console.error('Error creating table:', err);
      } else {
        console.log('Table is ready.');
      }
    });

    const createFavTableQuery = "CREATE TABLE favourites (id int(11) NOT NULL AUTO_INCREMENT,imageID varchar(20) NOT NULL,PRIMARY KEY (id))";
  
    connection.query(createFavTableQuery, (err) => {
      if (err) {
        console.error('Error creating table:', err);
      } else {
        console.log('Table is ready.');
      }
  
      connection.end();
    });
  };

function checkIfDatabaseExist(){
    return new Promise(( resolve, reject ) => {
        let connection = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'phototracker'
        });
        connection.connect((err)=>{
            if(err) reject(err);
            resolve();
        });
    });
}

const query = (sql,props) => {
    return new Promise( async ( resolve, reject ) => {
        checkIfDatabaseExist().then(()=>{
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
        }).catch(()=>{
            createDatabaseIfNotExist();
        });   
    });
}

module.exports.query = query;