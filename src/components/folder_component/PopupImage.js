import classes from './PopupImage.module.css'

function PopupImage(props){
    return <img src={props.uri} alt='hello' className={classes.img}/>
}
export default PopupImage;