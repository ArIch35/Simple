import classes from './Circle.module.css';

function Circle(props) {
  return <div className={!props.finished ? classes.circle : classes.circle_complete}>
    {!props.finished ? props.value : <div className={classes.check_symbol}></div>}</div>;
}

export default Circle;