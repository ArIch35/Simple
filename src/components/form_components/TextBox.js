import classes from './InputBox.module.css';
import { useState, useEffect } from 'react';

function TextBox(props) {
  const  [textValue,setTextValue] = useState('');

  useEffect(() => {
    setTextValue('');
  }, [props.reset]);

  function inputHandler(event){
    const newText = event.target.value;
    props.textChangedHandler(newText);
    setTextValue(newText);
  };

  return <div className={classes.holder}>
            <label className={classes.lab} htmlFor={props.id}>{props.name}</label>
            <textarea id={props.id} name={props.id} className={classes.area} onChange={inputHandler} value={textValue}></textarea>
         </div>
}

export default TextBox;