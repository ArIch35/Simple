import classes from './DropArea.module.css'
import DropAreaDesc from './DropAreaDesc';
import { useState } from 'react';

function DropArea(props){
    const [isHighlighted,setHighlighted] = useState(false);

    function preventHandler(event){
        event.preventDefault();
    }

    function dragEnterHandler(event){
        preventHandler(event);
        setHighlighted(true);
    }

    function dragLeaveHandler(){
        setHighlighted(false);
    }

    function dropHandler(event){
        preventHandler(event);
        setHighlighted(false);
        const files = event.dataTransfer.files;
        for (const file of files) {
            props.readFile(file);
        }
    }

    function handleImageChange(event){
        const file = event.target.files[0];
        props.readFile(file);
    }

    return <div className={!isHighlighted ? classes.drop: classes.drop_higlight} 
    onDragEnter={dragEnterHandler} 
    onDragOver={preventHandler}
    onDragLeave={dragLeaveHandler}
    onDrop={dropHandler}>
        <input type="file" id="imageInput" accept="image/*" onChange={handleImageChange} style={{ display: 'none' }}/>
        <DropAreaDesc filename={props.filename} higlighted={isHighlighted}/>
    </div>
}

export default DropArea;