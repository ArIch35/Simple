import classes from './ImageObject.module.css'
import {useState,useEffect} from 'react'
import axios from "axios";
import { useNavigate } from 'react-router-dom';


function ImageObject(props){
    const [isFavourite,setFavourite] = useState(false);
    const [removedFromFavourite,setRemovedFromFavourite] = useState(false);
    const navigate = useNavigate();

    function addToFavouriteHandler(){
        if(isFavourite){
            return;
        }
        axios.post("http://192.168.0.103:2587/favourites/",{id: props.id})
        .then(()=>{
            alert("Added To Favourites");
            setFavourite(true);
        }).catch((error) => {
            console.log(error);
            alert("Network Error, Please Try Again!");
        });
    }

    function removeFromFavouriteHandler(){
        if(!isFavourite){
            return;
        }
        setFavourite(false);
        axios.delete("http://192.168.0.103:2587/favourites/"+props.id)
        .then(()=>{
            alert("Removed From Favourite");
            getFolderFavouriteTotal();
        }).catch((error) => {
            console.log(error);
            alert("Network Error, Please Try Again!");
        });
    }

    function getFolderFavouriteTotal(){
        axios.get("http://localhost:2587/favourites/count/"+props.date)
        .then((res)=>{
            if(res.data[0].total>0){
                return setRemovedFromFavourite(true);
            }
            navigate('/favourites');
        }).catch((error) => {
            console.log(error);
            alert("Network Error, Please Try Again!");
        });
    }

    useEffect(() => {
        axios.get("http://localhost:2587/isFavourite/"+props.id).then((res) => {
            if(res.data.exist) return setFavourite(true);
            setFavourite(false);
        });
    },[props.id]);

    if(!removedFromFavourite)
    return <div className={classes.itm_ctr}>
        <img src={props.img} className={classes.img} alt='img'/>
        <h2>{props.title}</h2>
        <p>{props.desc}</p>
        <div className={classes.holder_left}>
            {props.type === 'all' ? 
            <button className={!isFavourite ? classes.btn : classes.btn_dis} onClick={addToFavouriteHandler}
                disabled = {isFavourite}>
            {!isFavourite ? "Add To Favourite" : "Already In Favourite"}</button> :
            <button className={classes.btn} onClick={removeFromFavouriteHandler}>Remove From Favourite</button>
            }
            </div>
    </div>
}

export default ImageObject;