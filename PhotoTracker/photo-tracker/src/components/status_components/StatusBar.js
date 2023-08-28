import classes from './StatusBar.module.css';
import Circle from './Circle';
import Line from './Line';

function StatusBar(props) {
  return <div className={classes.stat_ctr}>
    <Circle value='1' finished={props.finishedStepOne}/>
    <Line/>
    <Circle value='2' finished={props.finishedStepTwo}/>
  </div>
}

export default StatusBar;