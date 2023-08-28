import classes from './AddImagePage.module.css'
import {motion} from 'framer-motion'
import {useState} from 'react'
import Card from '../components/ui/Card';

import DropBoxArea from '../components/drop_components/DropBoxArea';
import StatusBar from '../components/status_components/StatusBar';
import InputArea from '../components/form_components/InputArea';
import setJSONValueByKey from '../custom_modules/setJSONValueByKey';
import generateRandomString from '../custom_modules/generateRandomString';
import { convertDateOfClient } from '../custom_modules/dateConverters';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function AddImage(){
    const history = useNavigate();
    const [stepOneDone,setStepOneDone] = useState(false);
    const [stepTwoDone,setStepTwoDone] = useState(false);

    const [imageTitle,setImageTitle] = useState('');
    const [dataJSON, setDataJSON] =useState({
        id: '',
        title: '',
        date: '',
        desc: '',
        uri: ''
    });
    
    function handlerFileImage(data,title){
        setStepOneDone(true);
        setDataJSON(setJSONValueByKey(dataJSON,'uri',data));
        setImageTitle(title);
    }

    function handlerFileData(data){
        setStepTwoDone(true);
        setDataJSON(setJSONValueByKey(dataJSON,'title',data.title));

        let temp_date = data.date;
        if(temp_date === ""){
            const currentDate = new Date();
            temp_date = currentDate.toLocaleDateString();
        }
        temp_date = convertDateOfClient(temp_date);
        console.log(temp_date)

        setDataJSON(setJSONValueByKey(dataJSON,'date',temp_date));
        setDataJSON(setJSONValueByKey(dataJSON,'desc',data.desc));
        setDataJSON(setJSONValueByKey(dataJSON,'id',generateRandomString(16)));

        sendData();
    }

    function decideComponent(){
        if(stepTwoDone) return;
        if(stepOneDone) return <InputArea handlerJSON={handlerFileData} title={imageTitle}/>
        return <DropBoxArea handlerImage={handlerFileImage}/>
    }

    function sendData(){
        axios.post("http://192.168.0.103:2587/",dataJSON)
        .then(()=>{
            history('/');
        }).catch((error) => {
            console.log(error);
            alert("Network Error, Please Try Again!");
        });
    }

    return(
        <motion.div className={classes.main_ctr} initial={{opacity:0}} animate={{opacity:1}} exit={{opacity:0}} transition={{duration:0.75,ease:'easeIn'}} >
            <Card style={classes.card}>
                <StatusBar finishedStepOne={stepOneDone} finishedStepTwo={stepTwoDone}/>
                {decideComponent()}
            </Card>
            
        </motion.div>
    );
}

export default AddImage;