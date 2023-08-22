import {Route,Routes} from 'react-router-dom';
import AllMeetups from './pages/AllMeetups';
import NewMeetups from './pages/NewMeetups';
import Favourites from './pages/Favourites';
import MainNavigation from './components/layout/MainNavigation';
import {AnimatePresence} from 'framer-motion'

function App() {
  return (
    <div>
      <MainNavigation/>
      <AnimatePresence mode='wait'>
        <Routes>
          <Route path='/' element={<AllMeetups/>}/>
          <Route path='/new-meetups' element={<NewMeetups/>}/>
          <Route path='/favourites' element={<Favourites/>}/>
        </Routes>
      </AnimatePresence>
      
    </div>
  );
}

export default App;
