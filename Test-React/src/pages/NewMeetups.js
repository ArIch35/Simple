import {motion} from 'framer-motion'
import { useNavigate } from "react-router-dom"
import axios from "axios";

import classes from './NewMeetups.module.css'
import MeetupForm from '../components/MeetupForm';
function NewMeetups(){
    const history = useNavigate();
    function meetupHandler(data){
        axios.post("http://192.168.0.103:2587/meetups",data)
        .then(()=>{
            history('/');
        }).catch((error) => {
            console.log(error);
            alert("Network Error, Please Try Again!");
        });
    }
    return(
        <motion.section initial={{opacity:0}} animate={{opacity:1}} exit={{opacity:0}} transition={{duration:0.75,ease:'easeIn'}}>
            <h1>Add New Meetup</h1>
            <MeetupForm addMeetupHandler={meetupHandler}/>
        </motion.section>
    );
}

export default NewMeetups;