import {Route,Routes} from 'react-router-dom';
import {AnimatePresence} from 'framer-motion'
import MainNavigation from './components/nav_bar/MainNavigation';
import AddImagePage from "./pages/AddImagePage";
import classes from './App.module.css';
import { motion } from 'framer-motion';
import {useState} from 'react';
import AllFolderPage from './pages/AllFolderPage';
import Images from './pages/Images'
import FavouritesPage from './pages/FavouritesPage';
function App() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  function handlerContainerDown(){
    setIsMenuOpen(!isMenuOpen);
  }

  return (
    <div className={classes.all_ctr}>
      <AnimatePresence mode='wait'>
      <MainNavigation containerDown={handlerContainerDown}/>
      <motion.div className={classes.main_ctr} 
        initial={{ y: 0 }}
        animate={{ y: isMenuOpen ? 75 : 0 }}
        transition={{ type: 'spring', stiffness: 100 }}
        id="main_ctr">
      
      <Routes>
          <Route path='/' element={<AllFolderPage/>}/>
          <Route path='/add-image' element={<AddImagePage/>}/>
          <Route path="/group/:date" element={<Images type='all'/>} />
          <Route path="/group/favourites/:date" element={<Images type='fav'/>} />
          <Route path='/favourites' element={<FavouritesPage/>}/>
      </Routes>
      </motion.div>
      </AnimatePresence>
      
      
    </div>
  );
}

export default App;
