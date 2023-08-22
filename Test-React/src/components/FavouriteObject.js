import classes from './MeetupObject.module.css'
import Card from '../components/ui/Card'
import axios from "axios";
import {useState} from 'react'

function FavouriteObject(props){
    const [isFavourite,setFavourite] = useState(true);

    function removeFromFavouriteHandler(){
        setFavourite(false);
        axios.delete("http://192.168.0.103:2587/favourites/"+props.id)
        .then(()=>{
            alert("Removed From Favourite");
        }).catch((error) => {
            console.log(error);
            alert("Network Error, Please Try Again!");
        });
    }

    if(!isFavourite){
        return;
    }

    return(
        <Card>
            <img src={props.img} alt='https://dfstudio-d420.kxcdn.com/wordpress/wp-content/uploads/2019/06/digital_camera_photo-1080x675.jpg'/>
            <h1 className={classes.title}>{props.title}</h1>
            <div className={classes.holder_right}>
                <p className={classes.desc}>{props.address}</p>
                <p className={classes.desc}>{props.desc}</p>
            </div>
            <div className={classes.holder_left}>
                <button className={classes.btn} onClick={removeFromFavouriteHandler}>Remove From Favourite</button>
            </div>
        </Card>
    );
}

export default FavouriteObject;