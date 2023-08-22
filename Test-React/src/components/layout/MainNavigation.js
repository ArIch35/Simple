import {Link} from 'react-router-dom'
import classes from './MainNavigation.module.css'
function MainNavigation(){
    return(
        <header>
            <div className={classes.title}>React Meetups</div>
            <nav>
                <ul>
                    <li><Link to='/' className={classes.link}>All Meetups</Link></li>
                    <li><Link to='/new-meetups' className={classes.link}>New Meetups</Link></li>
                    <li><Link to='/favourites' className={classes.link}>Favourites</Link></li>
                </ul>
            </nav>
        </header>
    );
}

export default MainNavigation;