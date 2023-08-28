import classes from './FolderObject.module.css'
import { useState } from 'react';
import HoveredImagePopup from './HoveredImagePopup';
import axios from 'axios';


function FolderObject(props){
    const [showPopup,setShowPopup] = useState(false);
    const [images,setImages] = useState([]);
    const [haveRequestedImage,setRequestedImage] = useState(false);

    function handlerMouseOver(){
        setShowPopup(true);
        if(props.folderType === 'all') return getImagesPopup();
        getFavouriteImagesPopup()
    }

    function getImagesPopup(){
        if(haveRequestedImage) return;
        axios.get("http://localhost:2587/popup/"+props.date).then((res) => {
            setImages(res.data[0].uris);
            setRequestedImage(true);
        }).catch((err)=>{
            console.error(err);
        })
    }

    function getFavouriteImagesPopup(){
        if(haveRequestedImage) return;
        axios.get("http://localhost:2587/favourites/popup/"+props.date).then((res) => {
            setImages(res.data[0].uris);
            setRequestedImage(true);
        }).catch((err)=>{
            console.error(err);
        })
    }

    function handlerMouseOut(){
        setShowPopup(false);
    }

    function clickHandler(){
        props.itemClicked(props.date)
    }

    return <div className={classes.itm_ctr} onMouseOver={handlerMouseOver} onMouseOut={handlerMouseOut} onClick={clickHandler}>
        {showPopup ? <HoveredImagePopup images={images}/> : null}
        <img src={props.img} className={classes.img} alt='img'/>
        <h2>{props.date}</h2>
        
    </div>
}

export default FolderObject;