import {motion} from 'framer-motion'
import {useState,useEffect} from 'react'
import MeetupList from '../components/MeetupList';
import Loader from '../components/ui/Loader';
import axios from "axios";

function AllMeetups(){  
  const [isLoading, setLoading] = useState(true);
  const [dataArray, setDataArray] = useState([]);
  useEffect(() => {
    setLoading(true);
    axios.get("http://localhost:2587/meetups").then((res) => {
      setLoading(false);
      setDataArray(res.data);
    });
  }, [])

  if(isLoading){
    return <Loader/>
  }

    return(
        <motion.div initial={{opacity:0}} animate={{opacity:1}} exit={{opacity:0}} transition={{duration:0.75,ease:'easeIn'}}>
            <h1>All Meetups</h1>
            <MeetupList list={dataArray} type='meet'/>
        </motion.div>
    );
}

export default AllMeetups;