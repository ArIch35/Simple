import classes from './DropBoxArea.module.css'
import { useState } from 'react';
import DropArea from './DropArea';

function DropBoxArea(props){
    const [fileName, setFileName] = useState("No File Selected")
    const [selectedImage, setSelectedImage] = useState('');
    
    function deleteHandler(){
        if(isDisabled()) return;
        setFileName("No File Selected");
        setSelectedImage('');
    }

    function sendHandler(){
        if(isDisabled()) return;
        props.handlerImage(selectedImage,fileName);
    }

    function checkFileCondition(file){
        if (!file){
            setFileName("Please Try Again");
            return false;
        } 
        if (!file.type.startsWith('image/')){
            setFileName("Not an Image! Try Again");
            return false;
        } 
        return true;
    }

    function readFile(file){
        if(!checkFileCondition(file)) return;
        const reader = new FileReader();
        reader.onload = (e) => {
            setFileName(file.name);
            setSelectedImage(e.target.result);
        };
        reader.readAsDataURL(file);
    }

    function isDisabled(){
        return selectedImage === '';
    }

    const buttonStatus = selectedImage === '' ? classes.btn_dis : classes.btn;

    return <div className={classes.box_cntr} >
        <DropArea filename={fileName} imageData={selectedImage} readFile={readFile}/>
        <div className={classes.btn_cntr}>
            <button className={buttonStatus} onClick={deleteHandler}>Delete</button>
            <button className={buttonStatus} onClick={sendHandler}>Send</button>
        </div>
    </div>
}

export default DropBoxArea;