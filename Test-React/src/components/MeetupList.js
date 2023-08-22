import MeetupObject from '../components/MeetupObject'
import FavouriteObject from './FavouriteObject'
import classes from './MeetupList.module.css'

function MeetupList(props){
    return <div className={classes.container_meetups}>
             {props.list.map((element) => {
                if(props.type === "meet") return <MeetupObject id={element.id} title={element.title} img={element.image} address={element.address} desc={element.description}/>
                return <FavouriteObject id={element.id} title={element.title} img={element.image} address={element.address} desc={element.description}/>
                })}
            </div>
}

export default MeetupList;