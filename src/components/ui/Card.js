import classes from './Card.module.css'

function Card(props){
    return <div className={!props.style ? classes.card: props.style}>{props.children}</div>
}

export default Card;