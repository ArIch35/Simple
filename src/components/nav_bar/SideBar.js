import {Link} from 'react-router-dom'
import classes from './SideBar.module.css'
import { motion,AnimatePresence } from 'framer-motion';
function SideBar(props){
    return (
        <AnimatePresence>
          {props.open && (
            <motion.nav
              className={classes.nav_opened}
              id="nav_menu"
              initial={{ opacity: 0, y: -20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20, transition: { duration: 0.3 } }}
              transition={{ type: 'spring', stiffness: 100 }}
            >
              <ul>
                <li>
                  <Link to="/" className={classes.link}> Home </Link>
                </li>
                <li>
                  <Link to="/add-image" className={classes.link}> Add Image </Link>
                </li>
                <li>
                  <Link to="/favourites" className={classes.link}> Favourites </Link>
                </li>
              </ul>
            </motion.nav>
          )}
        </AnimatePresence>
      );
    }    

export default SideBar;