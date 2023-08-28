import {motion} from 'framer-motion'
import {useState,useEffect} from 'react'
import { useParams } from 'react-router-dom';
import FolderList from '../components/folder_component/FolderList'
import Loader from '../components/ui/Loader';
import axios from "axios";

function Images(props){  
  const { date } = useParams();
  const [isLoading, setLoading] = useState(true);
  const [dataArray, setDataArray] = useState([]);

  useEffect(() => {
    setLoading(true);
    const getQueryType = props.type === 'all' ? axios.get("http://localhost:2587/"+date) : axios.get("http://localhost:2587/favourites/"+date);
    getQueryType.then((res) => {
      setLoading(false);
      setDataArray(res.data);
    }).catch((err)=>{
      console.error(err);
    })
  }, [date,props.type])

  if(isLoading){
    return <Loader/>
  }

  function getDate(){
    const dateObj = new Date(date);
    const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    return dateObj.toLocaleDateString('en-US', options);
  }
  
    return <motion.div initial={{opacity:0}} animate={{opacity:1}} exit={{opacity:0}} transition={{duration:0.75,ease:'easeIn'}}>
             <h1>Images from {getDate()}</h1>
             <FolderList list={dataArray} type='image' imgType={props.type} date={date}/>
         </motion.div>
}

export default Images;