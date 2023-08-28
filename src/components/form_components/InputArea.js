import classes from './InputArea.module.css';
import InputBox from './InputBox';
import TextBox from './TextBox';
import setJSONValueByKey from '../../custom_modules/setJSONValueByKey';
import { useState } from 'react';

function InputArea(props) {
    const [jsonInput, setJsonData] = useState({
        title: '',
        date: '',
        desc: ''
    });

    const [buttonIsDisabled, setButtonStatus] = useState(true);
    const [resetInput, setReset] = useState(false);

    function titleHandler(title){
        setJsonData(setJSONValueByKey(jsonInput,'title',title))
        setButtonStatus(false);
    }

    function dateHandler(date){
        setJsonData(setJSONValueByKey(jsonInput,'date',date));
    }

    function descHandler(desc){
        setJsonData(setJSONValueByKey(jsonInput,'desc',desc));
    }

    function deleteHandler(){
        if(buttonIsDisabled) return;
        setJsonData(setJSONValueByKey(jsonInput,'title',''));
        setJsonData(setJSONValueByKey(jsonInput,'date',''));
        setJsonData(setJSONValueByKey(jsonInput,'desc',''));
        setReset(!resetInput)
    }

    function sendHandler(){
        if(buttonIsDisabled) return;
        props.handlerJSON(jsonInput);
    }

    const buttonStatus = buttonIsDisabled ? classes.btn_dis : classes.btn;

    return <div className={classes.box_cntr}>
        <div className={classes.inp_cntr}>
            <InputBox type='text' id='title' name='Title' reset={resetInput} textChangedHandler={titleHandler}/>
            <InputBox type='date' id='title' name='Date' reset={resetInput} textChangedHandler={dateHandler}/>
            <TextBox id='desc' name='Description' reset={resetInput} textChangedHandler={descHandler}/>
        </div>
        <div className={classes.btn_cntr}>
            <button className={buttonStatus} onClick={deleteHandler}>Delete</button>
            <button className={buttonStatus} onClick={sendHandler}>Send</button>
        </div>
    </div>
}

export default InputArea;