import classes from './MeetupObject.module.css'
import Card from '../components/ui/Card'
import axios from "axios";
import {useState,useEffect} from 'react'

function MeetupObject(props){
    const [isFavourite,setFavourite] = useState(false);

    function addToFavouriteHandler(){
        if(isFavourite){
            return;
        }
        setFavourite(true);
        axios.post("http://192.168.0.103:2587/favourites/",{id: props.id})
        .then(()=>{
            alert("Added To Favourites");
        }).catch((error) => {
            console.log(error);
            alert("Network Error, Please Try Again!");
        });
    }

    useEffect(() => {
        axios.get("http://localhost:2587/favourites/isFavourite/"+props.id).then((res) => {
            if(res.data.exist) setFavourite(true);
        });
    },[props.id]);

    return(
        <Card>
            <img src={props.img} alt='https://dfstudio-d420.kxcdn.com/wordpress/wp-content/uploads/2019/06/digital_camera_photo-1080x675.jpg'/>
            <h1 className={classes.title}>{props.title}</h1>
            <div className={classes.holder_right}>
                <p className={classes.desc}>{props.address}</p>
                <p className={classes.desc}>{props.desc}</p>
            </div>
            <div className={classes.holder_left}>
                <button className={!isFavourite ? classes.btn : classes.btn_dis} onClick={addToFavouriteHandler}
                disabled = {isFavourite}>
                    {!isFavourite ? "Add To Favourite" : "Already In Favourite"}</button>
            </div>
        </Card>
    );
}

export default MeetupObject;