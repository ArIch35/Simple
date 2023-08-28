
import classes from './MainNavigation.module.css';
import { useState } from 'react';
import SideBar from './SideBar';
import {motion,AnimatePresence} from 'framer-motion';
function MainNavigation(props){
    const [sidebarIsOpened,setSidebarToOpen] = useState(false);

    function iconClickedHandler(){
        setSidebarToOpen(!sidebarIsOpened);
        props.containerDown();
    }

    return(
        <div className={classes.ctr_all}>
            <header className={classes.ctr_title}>
                <AnimatePresence>
                    <motion.img className={classes.sidebar} 
                    initial={{ rotate: 0 }}
                    animate={{ rotate: sidebarIsOpened ? 90 : 0 }}
                    transition={{ duration: 0.4, ease: 'easeInOut' }}
                    src={require('../../icons/sidebar.png')} alt='bar' onClick={iconClickedHandler} id='fix'/>
                </AnimatePresence>
                <div className={classes.title}>Photo Tracker</div>
            </header> 
            {sidebarIsOpened ? <SideBar open={true}/> : <SideBar open={false}/>}
        </div>
         
    );
}

export default MainNavigation;