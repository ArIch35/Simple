import classes from './HoveredImagePopup.module.css'
import PopupImage from './PopupImage'
function HoveredImagePopup(props){
    function decideShownImages(){
        switch(props.images.length){
            case 1: return <div>
                <PopupImage uri={props.images[0]}/> 
                </div>
            case 2: return <div>
                <PopupImage uri={props.images[0]}/> 
                <PopupImage uri={props.images[1]}/>
                </div>
            default: return <div>Hello</div>
        }
    }
    return <div className={classes.popup_ctr}>
        <h1>Sneak Peak</h1>
        {decideShownImages()}
    </div>
}
export default HoveredImagePopup;