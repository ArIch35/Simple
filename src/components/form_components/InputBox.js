import classes from './InputBox.module.css';
import { useState, useEffect } from 'react';

function InputBox(props) {
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
            <input type={props.type} id={props.id} name={props.id} className={classes.inp} onChange={inputHandler} value={textValue}></input>
         </div>
}

export default InputBox;