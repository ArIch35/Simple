import {motion} from 'framer-motion'
import {useState,useEffect} from 'react'
import { useNavigate } from 'react-router-dom';
import FolderList from '../components/folder_component/FolderList'
import Loader from '../components/ui/Loader';
import axios from "axios";

function FavouritesPage(){  
  const [isLoading, setLoading] = useState(true);
  const [dataArray, setDataArray] = useState([]);
  const navigate = useNavigate();

  function itemClickedHandler(date){
    navigate(`/group/favourites/${date}`);
  }
  
  useEffect(() => {
    setLoading(true);
    axios.get("http://localhost:2587/favourites").then((res) => {
      setLoading(false);
      setDataArray(res.data);
    }).catch((err)=>{
      console.error(err);
    })
  }, [])

  if(isLoading){
    return <Loader/>
  }

    return(
        <motion.div initial={{opacity:0}} animate={{opacity:1}} exit={{opacity:0}} transition={{duration:0.75,ease:'easeIn'}}>
            <h1>Favourites Groups</h1>
            <FolderList list={dataArray} type='folder' itemClicked={itemClickedHandler} folderType='fav'/>
        </motion.div>
    );
}

export default FavouritesPage;